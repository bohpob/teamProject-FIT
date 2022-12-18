package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.*;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.*;
import cz.cvut.fit.sp.chipin.base.log.*;
import cz.cvut.fit.sp.chipin.base.member.GroupRole;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberService;
import cz.cvut.fit.sp.chipin.base.transaction.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final MemberService memberService;
    private final TransactionService transactionService;

    public String createGroup(GroupCreateRequest request) throws Exception {
        User user = userService.getUser(request.getUserId());

        if (request.getName() == null || request.getCurrency() == null)
            throw new Exception("Name and Currency fields cannot be empty");

        Group group = new Group();
        group.setName(request.getName());
        group.setCurrency(Currency.valueOf(request.getCurrency()));
        groupRepository.save(group);

        Member member = new Member(user, group, GroupRole.ADMIN, 0f, 0f, 0f);
        memberService.save(member);

        group.addMembership(member);
        user.addMembership(member);

        groupRepository.save(group);
        userService.save(user);

        logService.create("created the group.", group, user);
        return "Created";
    }

    public GroupResponse readGroup(Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found");

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.get().getName());
        groupResponse.setCurrency(group.get().getCurrency());

        groupResponse.setUsers(UserConverter.toUsersGroupResponse(memberService.readMembers(group_id)));

        groupResponse.setTransactions(TransactionConverter.toTransactionsGroupResponse(transactionService.getTransactionsByGroupId(group_id)));

        groupResponse.setDebts(debtService.readDebts(group_id).stream().map(DebtConverter::toDebtGroupResponse).collect(Collectors.toList()));

        ArrayList<Log> logs = logService.getAllByGroupId(group.get().getId());
        groupResponse.setLogs(LogConverter.toLogsGroupResponse(logs));

        return groupResponse;
    }

    public String addMember(Long userId, Long groupId) throws Exception {

        User user = userService.getUser(userId);
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));

        for (Member member : group.getMembers()) {
            if (member.getUser().equals(user))
                return "User already member of this group";
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
                    transaction.getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

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
                    transaction.getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getGroup());
    }

    public void settleDebt(Long groupId, Long lenderId, Long borrowerId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new Exception("Group not found");
        Optional<Member> lender = memberService.readMember(lenderId, groupId);
        if (lender.isEmpty())
            throw new Exception("User not found");
        Optional<Member> borrower = memberService.readMember(borrowerId, groupId);
        if (borrower.isEmpty())
            throw new Exception("User not found");
        Optional<Debt> debt = debtService.readDebt(groupId, lenderId, borrowerId);
        if (debt.isEmpty())
            throw new Exception("Debt not found");

        List<Long> amounts = new ArrayList<>();
        amounts.add(lenderId);
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest(
                borrower.get().getUser().getName() + " repaid " + lender.get().getUser().getName() + "'s " + "debt"
                , debt.get().getAmount(), borrower.get().getUser().getId(), amounts);


        try {
            if (allSpendersFromGroup(transactionCreateRequest.getSpenderIds(), groupId)) {
                Transaction transaction = transactionService.create(transactionCreateRequest, borrower.get().getUser(), group.get());
                acceptTxCreate(transaction);
                //
                logService.create(transaction.getName(), transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public TransactionResponse createTransaction(TransactionCreateRequest transactionCreateRequest, Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<Member> payer = memberService.readMember(transactionCreateRequest.getPayerId(), groupId);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        Transaction transaction = new Transaction();
        try {
            if (allSpendersFromGroup(transactionCreateRequest.getSpenderIds(), groupId)) {
                transaction = transactionService.create(transactionCreateRequest, payer.get().getUser(), group.get());
                acceptTxCreate(transaction);
                // change to the user who will actually delete the transaction
                logService.create("made a payment: " + transaction.getAmount(), transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return TransactionConverter.toDto(transaction);
    }

    public TransactionResponse readTransaction(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");

        return TransactionConverter.toDto(transaction.get());
    }

    public TransactionResponse updateTransaction(TransactionUpdateRequest transactionUpdateRequest, Long groupId, Long transactionId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        if (!Objects.equals(transaction.get().getGroup().getId(), groupId))
            throw new Exception("Transaction does not belong to this group.");
        Optional<Member> prevPayer = memberService.readMember(transaction.get().getPayer().getId(), groupId);
        if (prevPayer.isEmpty())
            throw new Exception("Payer not found.");
        Optional<Member> nextPayer = memberService.readMember(transactionUpdateRequest.getPayerId(), groupId);
        if (nextPayer.isEmpty())
            throw new Exception("Payer not found.");

        try {
            if (allSpendersFromGroup(transactionUpdateRequest.getSpenderIds(), groupId)) {
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
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        Optional<Member> payer = memberService.readMember(transaction.get().getPayer().getId(), groupId);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        acceptTxDelete(transaction.get());
        transactionService.delete(transaction.get());
        // change to the user who will actually delete the transaction
        logService.create("deleted transaction", payer.get().getGroup(), payer.get().getUser());
    }

    private boolean allSpendersFromGroup(List<Long> ids, Long groupId) throws Exception {
        for (Long id : ids) {
            if (memberService.readMember(id, groupId).isEmpty()) {
                throw new Exception("User is not from this group");
            }
        }
        return true;
    }
}
