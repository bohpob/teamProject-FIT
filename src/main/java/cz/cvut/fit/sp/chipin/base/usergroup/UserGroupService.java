package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.spender.UnequalTransactionMember;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.member.GroupRole;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberService;
import cz.cvut.fit.sp.chipin.base.transaction.*;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.mapper.*;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final MemberService memberService;
    private final TransactionService transactionService;
    private final UserGroupMapper userGroupMapper;

    public GroupCreateGroupResponse createGroup(GroupCreateGroupRequest request, String userId) throws Exception {
        User user = userService.getUser(userId);

        UserGroup userGroup = userGroupMapper.createGroupRequestToEntity(request);
        userGroup.setHexCode(generateRandomHexCode());
        userGroupRepository.save(userGroup);

        Member member = new Member(user, userGroup, GroupRole.ADMIN, 0f, 0f, 0f);
        memberService.save(member);

        userGroup.addMembership(member);
        user.addMembership(member);

        userGroupRepository.save(userGroup);
        userService.save(user);

        logService.create("Created the group", userGroup, user);
        userGroup.setLogs(logService.readLogs(userGroup.getId()));
        userGroupRepository.save(userGroup);

        return userGroupMapper.entityToCreateGroupResponse(userGroup);
    }

    private String generateRandomHexCode() {
        Random random = new Random();
        String hexCode = Long.toHexString(random.nextLong(0xffffff + 1));
        if (userGroupRepository.findUserGroupByHexCode(hexCode).isEmpty()) {
            return hexCode;
        } else {
            return generateRandomHexCode();
        }
    }

    public GroupReadGroupResponse readGroup(Long id) throws Exception {
        UserGroup userGroup = userGroupRepository.findById(id)
                .orElseThrow(() -> new Exception("Group not found"));
        return userGroupMapper.entityToReadGroupResponse(userGroup);
    }

    public String addMember(String userId, Long groupId) throws Exception {

        User user = userService.getUser(userId);
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));

        for (Member member : userGroup.getMembers()) {
            if (member.getUser().equals(user)) {
                return "User already member of this group";
            }
        }

        Member member = new Member(user, userGroup, GroupRole.USER, 0f, 0f, 0f);
        userGroup.addMembership(member);
        user.addMembership(member);

        memberService.save(member);
        userService.save(user);
        userGroupRepository.save(userGroup);

        logService.create("joined the group.", userGroup, user);
        return "User joined";
    }

    public void acceptTxCreate(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getUserGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.setPaid(payerMember.getPaid() + transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUser().getId(),
                            transaction.getUserGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.setSpent(member.getSpent() + amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.getAmount());
        }

        debtService.recalculate(spent, transaction.getPayer(), transaction.getUserGroup());
    }

    public void acceptTxDelete(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getUserGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.decreasePaid(transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUser().getId(),
                            transaction.getUserGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getUserGroup());
    }

    public void settleDebt(Long groupId, String lenderId, String borrowerId) throws Exception {
        Optional<UserGroup> group = userGroupRepository.findById(groupId);
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

        TransactionCreateRequest request = new TransactionCreateRequest(
                borrower.get().getUser().getName() + " repaid "
                        + lender.get().getUser().getName() + "'s " + "debt",
                debt.get().getAmount(), borrower.get().getUser().getId(), TransactionType.UNEQUALLY, amounts);


        try {
            if (allSpendersFromGroup(request.getSpenders(), groupId)) {
                Transaction transaction = transactionService.create(request,
                        borrower.get().getUser(), group.get());
                acceptTxCreate(transaction);
                //
                logService.create(transaction.getName(), transaction.getUserGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public TransactionResponse createTransaction(TransactionCreateRequest request, Long groupId) throws Exception {
        Optional<UserGroup> group = userGroupRepository.findById(groupId);
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
                        transaction.getUserGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return TransactionConverter.toDto(transaction);
    }

    public TransactionResponse readTransaction(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }

        return TransactionConverter.toDto(transaction.get());
    }

    public TransactionReadGroupTransactionResponse readGroupTransaction(Long transactionId, Long groupId) throws Exception {
        return transactionService.readGroupTransaction(transactionId, groupId);
    }

    public GroupReadGroupTransactionsResponse readGroupTransactions(Long groupId) throws Exception {
        UserGroup userGroup = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        return userGroupMapper.entityToReadGroupTransactionsResponse(userGroup);
    }

    public TransactionResponse updateTransaction(TransactionUpdateRequest transactionUpdateRequest,
                                                 Long groupId, Long transactionId) throws Exception {
        Optional<UserGroup> group = userGroupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found.");
        }
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty()) {
            throw new Exception("Transaction not found.");
        }
        if (!Objects.equals(transaction.get().getUserGroup().getId(), groupId)) {
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

        return TransactionConverter.toDto(transaction.get());
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
        logService.create("deleted transaction", payer.get().getUserGroup(), payer.get().getUser());
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
        UserGroup userGroup = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        return userGroupMapper.entityToReadGroupLogsResponse(userGroup);
    }

    public GroupUpdateGroupNameResponse updateGroupName(Long groupId, String name) throws Exception {
        UserGroup userGroup = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        if (name.isBlank()) {
            throw new Exception("New name is empty");
        }
        userGroup.setName(name);
        userGroupRepository.save(userGroup);
        return userGroupMapper.entityToUpdateGroupNameResponse(userGroup);
    }
}
