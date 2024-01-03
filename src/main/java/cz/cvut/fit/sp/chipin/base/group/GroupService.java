package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.group.mapper.*;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.member.GroupRole;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberService;
import cz.cvut.fit.sp.chipin.base.transaction.*;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.*;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.UnequalTransactionMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final MemberService memberService;
    private final TransactionService transactionService;
    private final GroupMapper groupMapper;
    private final TransactionMapper transactionMapper;

    public GroupCreateGroupResponse createGroup(GroupCreateGroupRequest request, String userId) throws Exception {
        User user = userService.getUser(userId);

        Group group = groupMapper.createGroupRequestToEntity(request);
        group.setHexCode(generateRandomHexCode());
        groupRepository.save(group);

        Member member = new Member(user, group, GroupRole.ADMIN, 0f, 0f, 0f);
        memberService.save(member);

        group.addMembership(member);
        user.addMembership(member);

        groupRepository.save(group);
        userService.save(user);

        logService.create("Created the group", group, user);
        group.setLogs(logService.readLogs(group.getId()));
        groupRepository.save(group);

        return groupMapper.entityToCreateGroupResponse(group);
    }

    public GroupReadGroupResponse readGroup(Long id) throws Exception {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new Exception("Group not found"));
        return groupMapper.entityToReadGroupResponse(group);
    }

    private String generateRandomHexCode() {
        Random random = new Random();
        String hexCode = Long.toHexString(random.nextLong(0xffffff + 1));
        if (groupRepository.findGroupByHexCode(hexCode).isEmpty()) {
            return hexCode;
        } else {
            return generateRandomHexCode();
        }
    }

    public String readHexCode(Long groupId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));
        return group.getHexCode();
    }

    public String addMember(String userId, String hexCode) throws Exception {
        User user = userService.getUser(userId);
        Group group = groupRepository.findGroupByHexCode(hexCode).orElseThrow(() -> new Exception("Group not found"));

        for (Member member : group.getMembers()) {
            if (member.getUser().equals(user)) {
                return "User is already a member of this group";
            }
        }

        Member member = new Member(user, group, GroupRole.USER, 0f, 0f, 0f);
        group.addMembership(member);
        user.addMembership(member);

        memberService.save(member);
        userService.save(user);
        groupRepository.save(group);

        logService.create("joined the group.", group, user);
        return "User joined";
    }

    public void acceptTxCreate(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.setPaid(payerMember.getPaid() + transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUser().getId(),
                            transaction.getGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.setSpent(member.getSpent() + amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.getAmount());
        }

        debtService.recalculate(spent, transaction.getPayer(), transaction.getGroup());
    }

    public void acceptTxDelete(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.decreasePaid(transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUser().getId(),
                            transaction.getGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getGroup());
    }

    public void settleDebt(Long groupId, String lenderId, String borrowerId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }
        Optional<Member> lender = memberService.readMember(lenderId, groupId);
        if (lender.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<Member> borrower = memberService.readMember(borrowerId, groupId);
        if (borrower.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<Debt> debt = debtService.readDebt(groupId, lenderId, borrowerId);
        if (debt.isEmpty()) {
            throw new Exception("Debt not found");
        }

        List<MemberAbstractRequest> amounts = new ArrayList<>();
        amounts.add(new UnequalTransactionMember(lenderId, debt.get().getAmount()));

        TransactionCreateTransactionRequest request = new TransactionCreateTransactionRequest(
                borrower.get().getUser().getName() + " repaid "
                        + lender.get().getUser().getName() + "'s " + "debt",
                debt.get().getAmount(), borrower.get().getUser().getId(), TransactionType.UNEQUALLY, amounts);


        try {
            if (allSpendersFromGroup(request.getSpenders(), groupId)) {
                Transaction transaction = transactionService.create(request,
                        borrower.get().getUser(), group.get());
                acceptTxCreate(transaction);
                logService.create(transaction.getName(), transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public TransactionCreateTransactionResponse createTransaction(TransactionCreateTransactionRequest request, Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found.");
        }
        Optional<Member> payer = memberService.readMember(request.getPayerId(), groupId);
        if (payer.isEmpty()) {
            throw new Exception("Payer not found.");
        }

        Transaction transaction = new Transaction();
        try {
            if (allSpendersFromGroup(request.getSpenders(), groupId)) {
                transaction = transactionService.create(request, payer.get().getUser(), group.get());
                acceptTxCreate(transaction);

                logService.create("made a payment: " + transaction.getAmount(),
                        transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return transactionMapper.entityToCreateTransactionResponse(transaction);
    }

    public Group read(Long id) throws Exception {
        return groupRepository.findById(id)
                .orElseThrow(() -> new Exception("Group not found"));
    }

    public TransactionReadGroupTransactionResponse readGroupTransaction(Long transactionId, Long groupId) throws Exception {
        return transactionService.readGroupTransaction(transactionId, groupId);
    }

    public GroupReadGroupTransactionsResponse readGroupTransactions(Long groupId) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        return groupMapper.entityToReadGroupTransactionsResponse(group);
    }

    public GroupReadGroupTransactionsResponse readGroupTransactionsByCategories(Long groupId, List<Category> categories) throws Exception {
        List<Transaction> transactions1 = read(groupId)
                .getTransactions()
                .stream()
                .filter(transaction -> categories.contains(transaction.getCategory()))
                .toList();

        List<Transaction> transactions = transactionService.readAllByCategories(groupId, categories);
        return groupMapper.transactionsToReadGroupTransactionsResponse(0, transactions);
    }

    public TransactionUpdateTransactionResponse updateTransaction(TransactionUpdateRequest transactionUpdateRequest,
                                                                  Long groupId, Long transactionId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found.");
        }
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }
        if (!Objects.equals(transaction.get().getGroup().getId(), groupId)) {
            throw new Exception("Transaction does not belong to this group.");
        }
        Optional<Member> prevPayer = memberService.readMember(transaction.get().getPayer().getId(), groupId);
        if (prevPayer.isEmpty()) {
            throw new Exception("Payer not found.");
        }
        Optional<Member> nextPayer = memberService.readMember(transactionUpdateRequest.getPayerId(), groupId);
        if (nextPayer.isEmpty()) {
            throw new Exception("Payer not found.");
        }

        try {
            if (allSpendersFromGroup(transactionUpdateRequest.getSpenders(), groupId)) {
                acceptTxDelete(transaction.get());
                transactionService.update(transaction.get(), transactionUpdateRequest, nextPayer.get().getUser());
                acceptTxCreate(transaction.get());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return transactionMapper.entityToUpdateTransactionResponse(transaction.get());
    }

    public void deleteTransaction(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }
        Optional<Member> payer = memberService.readMember(transaction.get().getPayer().getId(), groupId);
        if (payer.isEmpty()) {
            throw new Exception("Payer not found.");
        }

        acceptTxDelete(transaction.get());
        transactionService.delete(transaction.get());
        // change to the user who will actually delete the transaction
        logService.create("deleted transaction", payer.get().getGroup(), payer.get().getUser());
    }

    private boolean allSpendersFromGroup(List<MemberAbstractRequest> spenders, Long groupId) throws Exception {
        List<String> ids = spenders.stream().map(MemberAbstractRequest::getSpenderId).toList();
        for (String id : ids) {
            if (memberService.readMember(id, groupId).isEmpty()) {
                throw new Exception("User is not from this group");
            }
        }
        return true;
    }

    public GroupReadGroupLogsResponse readGroupLogs(Long groupId) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        return groupMapper.entityToReadGroupLogsResponse(group);
    }

    public GroupUpdateGroupNameResponse updateGroupName(Long groupId, String name) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        if (name.isBlank()) {
            throw new Exception("New name is empty");
        }
        group.setName(name);
        groupRepository.save(group);
        return groupMapper.entityToUpdateGroupNameResponse(group);
    }
}
