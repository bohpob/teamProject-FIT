package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountConverter;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.debt.DebtConverter;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.log.Log;
import cz.cvut.fit.sp.chipin.base.log.LogConverter;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.log.LogsGroupResponse;
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
    private final UserAccountService userAccountService;
    private final DebtService debtService;
    private final LogService logService;
    private final MemberService memberService;
    private final TransactionService transactionService;

    public String createGroup(GroupCreateRequest request) throws Exception {
        UserAccount userAccount = userAccountService.getUserAccount(request.getUserAccountId());

        if (request.getName() == null || request.getCurrency() == null) {
            throw new Exception("Name and Currency fields cannot be empty");
        }

        Group group = new Group(request.getName(), Currency.valueOf(request.getCurrency()), generateRandomHexCode());
        groupRepository.save(group);

        Member member = new Member(userAccount, group, GroupRole.ADMIN, 0f, 0f, 0f);
        memberService.save(member);

        group.addMembership(member);
        userAccount.addMembership(member);

        groupRepository.save(group);
        userAccountService.save(userAccount);

        logService.create("created the group.", group, userAccount);
        return "Created";
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

    public GroupResponse readGroup(Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.get().getName());
        groupResponse.setCurrency(group.get().getCurrency());

        groupResponse.setUserAccounts(UserAccountConverter.toUserAccountsGroupResponse(memberService.readMembers(groupId)));

        groupResponse.setTransactions(TransactionConverter
                .toGroupResponse(transactionService.readTransactions(groupId)));

        groupResponse.setDebts(debtService.readDebts(groupId).stream().map(DebtConverter::toDebtGroupResponse)
                .collect(Collectors.toList()));

        List<Log> logs = logService.readLogs(group.get().getId());
        groupResponse.setLogs(LogConverter.toGroupResponse(logs));

        return groupResponse;
    }

    public String addMember(Long userAccountId, Long groupId) throws Exception {

        UserAccount userAccount = userAccountService.getUserAccount(userAccountId);
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));

        for (Member member : group.getMembers()) {
            if (member.getUserAccount().equals(userAccount)) {
                return "UserAccount already member of this group";
            }
        }

        Member member = new Member(userAccount, group, GroupRole.USER, 0f, 0f, 0f);
        group.addMembership(member);
        userAccount.addMembership(member);

        memberService.save(member);
        userAccountService.save(userAccount);
        groupRepository.save(group);

        logService.create("joined the group.", group, userAccount);
        return "UserAccount joined";
    }

    public void acceptTxCreate(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.setPaid(payerMember.getPaid() + transaction.getAmount());
        memberService.save(payerMember);

        Map<UserAccount, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUserAccount().getId(),
                            transaction.getGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.setSpent(member.getSpent() + amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUserAccount(), amount.getAmount());
        }

        debtService.recalculate(spent, transaction.getPayer(), transaction.getGroup());
    }

    public void acceptTxDelete(Transaction transaction) throws Exception {
        Member payerMember = memberService.readMember(transaction.getPayer().getId(),
                transaction.getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.decreasePaid(transaction.getAmount());
        memberService.save(payerMember);

        Map<UserAccount, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberService.readMember(amount.getUserAccount().getId(),
                            transaction.getGroup().getId())
                    .orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberService.save(member);

            spent.put(amount.getUserAccount(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUserAccount(), payerMember.getGroup());
    }

    public void settleDebt(Long groupId, Long lenderId, Long borrowerId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }
        Optional<Member> lender = memberService.readMember(lenderId, groupId);
        if (lender.isEmpty()) {
            throw new Exception("UserAccount not found");
        }
        Optional<Member> borrower = memberService.readMember(borrowerId, groupId);
        if (borrower.isEmpty()) {
            throw new Exception("UserAccount not found");
        }
        Optional<Debt> debt = debtService.readDebt(groupId, lenderId, borrowerId);
        if (debt.isEmpty()) {
            throw new Exception("Debt not found");
        }

        List<Long> amounts = new ArrayList<>();
        amounts.add(lenderId);
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest(
                borrower.get().getUserAccount().getName() + " repaid "
                        + lender.get().getUserAccount().getName() + "'s " + "debt",
                debt.get().getAmount(), borrower.get().getUserAccount().getId(), amounts);

        try {
            if (allSpendersFromGroup(transactionCreateRequest.getSpenderIds(), groupId)) {
                Transaction transaction = transactionService.create(transactionCreateRequest,
                        borrower.get().getUserAccount(), group.get());
                acceptTxCreate(transaction);
                //
                logService.create(transaction.getName(), transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public TransactionResponse createTransaction(TransactionCreateRequest transactionCreateRequest,
                                                 Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found.");
        }
        Optional<Member> payer = memberService.readMember(transactionCreateRequest.getPayerId(), groupId);
        if (payer.isEmpty()) {
            throw new Exception("Payer not found.");
        }

        Transaction transaction = new Transaction();
        try {
            if (allSpendersFromGroup(transactionCreateRequest.getSpenderIds(), groupId)) {
                transaction = transactionService.create(transactionCreateRequest, payer.get().getUserAccount(), group.get());
                acceptTxCreate(transaction);
                // change to the userAccount who will actually delete the transaction
                logService.create("made a payment: " + transaction.getAmount(),
                        transaction.getGroup(), transaction.getPayer());
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

    public TransactionsGroupResponse readTransactions(Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }

        List<Transaction> transactions = transactionService.readTransactions(groupId);
        Collections.reverse(transactions);
        List<TransactionGroupResponse> transactionsGroupResponse = transactions.stream()
                .map(TransactionConverter::toTransactionGroupResponse).toList();

        return new TransactionsGroupResponse(transactionsGroupResponse);
    }

    public TransactionResponse updateTransaction(TransactionUpdateRequest transactionUpdateRequest,
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
            if (allSpendersFromGroup(transactionUpdateRequest.getSpenderIds(), groupId)) {
                acceptTxDelete(transaction.get());
                transactionService.update(transaction.get(), transactionUpdateRequest, nextPayer.get().getUserAccount());
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
        // change to the userAccount who will actually delete the transaction
        logService.create("deleted transaction", payer.get().getGroup(), payer.get().getUserAccount());
    }

    private boolean allSpendersFromGroup(List<Long> ids, Long groupId) throws Exception {
        for (Long id : ids) {
            if (memberService.readMember(id, groupId).isEmpty()) {
                throw new Exception("UserAccount is not from this group");
            }
        }
        return true;
    }

    public LogsGroupResponse readLogs(Long groupId) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }

        List<Log> logs = logService.readLogs(groupId);
        Collections.reverse(logs);
        return new LogsGroupResponse(logs.stream().map(LogConverter::toDto).collect(Collectors.toList()));
    }

    public GroupResponse changeGroupName(Long groupId, String name) throws Exception {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new Exception("Group not found");
        }
        if (name.isBlank()) {
            throw new Exception("New name is empty");
        }

        group.get().setName(name);
        groupRepository.save(group.get());
        return readGroup(groupId);
    }
}
