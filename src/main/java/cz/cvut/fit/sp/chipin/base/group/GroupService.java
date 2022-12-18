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
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<GroupResponse> readGroup(Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found");

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.get().getName());
        groupResponse.setCurrency(group.get().getCurrency());

        groupResponse.setUsers(UserConverter.toUsersGroupResponse(memberService.getMembersByGroupId(group_id)));

        groupResponse.setTransactions(TransactionConverter.toTransactionsGroupResponse(transactionService.getTransactionsByGroupId(group_id)));

        groupResponse.setDebts(debtService.getDebtsByGroupId(group_id).stream().map(DebtConverter::toDebtGroupResponse).collect(Collectors.toList()));

        ArrayList<Log> logs = logService.getAllByGroupId(group.get().getId());
        groupResponse.setLogs(LogConverter.toLogsGroupResponse(logs));

        return ResponseEntity.ok(groupResponse);
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
        Member payerMember = memberService.getMember(transaction.getPayer().getUser().getId(),
                transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.setPaid(payerMember.getPaid() + transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.getMember(amount.getUser().getId(),
                    transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.setSpent(member.getSpent() + amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.getAmount());
        }

        debtService.recalculate(spent, transaction.getPayer().getUser(), transaction.getPayer().getGroup());
    }

    public void acceptTxDelete(Transaction transaction) throws Exception {
        Member payerMember = memberService.getMember(transaction.getPayer().getUser().getId(),
                transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.decreasePaid(transaction.getAmount());
        memberService.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.getMember(amount.getUser().getId(),
                    transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUser(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getGroup());
    }

    public void repaymentDebt(Long groupId, DebtKeyDTO dto) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new Exception("Group not found");
        Optional<Member> lender = memberService.getMember(dto.getLenderId(), groupId);
        if (lender.isEmpty())
            throw new Exception("User not found");
        Optional<Member> borrower = memberService.getMember(dto.getBorrowerId(), groupId);
        if (borrower.isEmpty())
            throw new Exception("User not found");


        Transaction transaction = debtService.delete(groupId, dto, lender.get(), borrower.get());
        acceptTxCreate(transaction);
        //
        logService.create(transaction.getName(), transaction.getPayer().getGroup(), transaction.getPayer().getUser());
    }

    public TransactionResponse createTransaction(TransactionCreateRequest transactionCreateRequest, Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new Exception("Group not found.");
        Optional<Member> payer = memberService.getMember(transactionCreateRequest.getPayerId(), groupId);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        Transaction transaction = transactionService.create(transactionCreateRequest, payer.get(), groupId);
        acceptTxCreate(transaction);
        // change to the user who will actually delete the transaction
        logService.create("made a payment: " + transaction.getAmount(), transaction.getPayer().getGroup(), transaction.getPayer().getUser());
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
        if (!Objects.equals(transaction.get().getPayer().getGroup().getId(), groupId))
            throw new Exception("Transaction does not belong to this group.");
        Optional<Member> prevPayer = memberService.getMember(transaction.get().getPayer().getUser().getId(), groupId);
        if (prevPayer.isEmpty())
            throw new Exception("Payer not found.");
        Optional<Member> nextPayer = memberService.getMember(transactionUpdateRequest.getPayerId(), groupId);
        if (nextPayer.isEmpty())
            throw new Exception("Payer not found.");

        acceptTxDelete(transaction.get());
        transactionService.update(transaction.get(), transactionUpdateRequest, nextPayer.get(), groupId);
        acceptTxCreate(transaction.get());
        return TransactionConverter.toDto(transaction.get());
    }

    public void deleteTransaction(Long transactionId, Long groupId) throws Exception {
        Optional<Transaction> transaction = transactionService.read(transactionId, groupId);
        if (transaction.isEmpty())
            throw new Exception("Transaction not found.");
        Optional<Member> payer = memberService.getMember(transaction.get().getPayer().getUser().getId(), groupId);
        if (payer.isEmpty())
            throw new Exception("Payer not found.");

        acceptTxDelete(transaction.get());
        transactionService.delete(transaction.get());
        // change to the user who will actually delete the transaction
        logService.create("deleted transaction", payer.get().getGroup(), payer.get().getUser());
    }
}
