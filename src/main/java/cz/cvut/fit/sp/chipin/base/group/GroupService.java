package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.group.mapper.*;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogMapper;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import cz.cvut.fit.sp.chipin.base.member.GroupRole;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberService;
import cz.cvut.fit.sp.chipin.base.notification.NotificationService;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.transaction.*;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.*;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.UnequalTransactionMember;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final NotificationService notificationService;
    private final LogMapper logMapper;

    public GroupCreateGroupResponse createGroup(GroupCreateGroupRequest request, String userId) throws Exception {
        User user = userService.getUser(userId);

        Group group = groupMapper.createGroupRequestToEntity(request);
        group.setHexCode(generateRandomHexCode());
        group.setPayerStrategy(PayerStrategy.LOWEST_BALANCE);
        group.setCheckNextPayer(false);
        groupRepository.save(group);

        Member member = new Member(user, group, GroupRole.ADMIN, 0f, 0f, 0f);
        memberService.save(member);

        group.setNextPayer(member);
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
        Float sumSpent = 0F;
        for (Member member : group.getMembers()) {
            sumSpent += member.getSpent();
        }
        return groupMapper.entityToReadGroupResponse(group, sumSpent);
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

        notificationService.createNotification(user, group, new NotificationContent(
                "You joined the group " + group.getName()));

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
                debt.get().getAmount(),
                borrower.get().getUser().getId(),
                Category.DEBT_REPAYMENT,
                TransactionType.UNEQUALLY,
                amounts
        );

        try {
            if (allSpendersFromGroup(request.getSpenders(), groupId)) {
                Transaction transaction = transactionService.create(request,
                        borrower.get().getUser(), group.get());
                acceptTxCreate(transaction);

                // Notifying the borrower of debt repayment
                debtService.notifyDebtRepayment(borrower.get().getUser(), lender.get().getUser(),
                        group.get(), debt.get().getAmount());
                userService.save(borrower.get().getUser());
                userService.save(lender.get().getUser());
                logService.create(transaction.getName(), transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public String setCheckNextPayer(
            Long groupId,
            Optional<String> payerStrategyString,
            Optional<Boolean> checkNextPayer
    ) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        if (payerStrategyString.isPresent()) {
            PayerStrategy payerStrategy = PayerStrategy.valueOf(payerStrategyString.get().toUpperCase());
            group.setPayerStrategy(payerStrategy);
            groupRepository.save(group);
        }
        if (checkNextPayer.isPresent()) {
            group.setCheckNextPayer(checkNextPayer.get());
            groupRepository.save(group);
        }
        return "Payer strategy = " + group.getPayerStrategy() + ", checking = " + group.getCheckNextPayer();
    }

    public String readNextPayerId(Long groupId) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        return group.getNextPayer().getId().getUserId();
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
        if (group.get().getCheckNextPayer() != null && group.get().getCheckNextPayer() &&
                group.get().getNextPayer() != payer.get()) {
            throw new Exception("Next payer check failed");
        }

        Transaction transaction = new Transaction();
        try {
            if (allSpendersFromGroup(request.getSpenders(), groupId)) {
                transaction = transactionService.create(request, payer.get().getUser(), group.get());
                acceptTxCreate(transaction);
                group.get().setNextPayer(findNextPayer(group.get()));
                groupRepository.save(group.get());

                logService.create("made a payment: " + transaction.getAmount(),
                        transaction.getGroup(), transaction.getPayer());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return transactionMapper.entityToCreateTransactionResponse(transaction);
    }

    public TransactionReadGroupTransactionResponse readGroupTransaction(Long transactionId, Long groupId) throws Exception {
        return transactionService.readGroupTransaction(transactionId, groupId);
    }

    public Page<TransactionReadGroupTransactionsResponse> readGroupTransactions(Long groupId, Pageable pageable) throws Exception {
        try {
            return transactionService.readTransactions(groupId, pageable).map(transactionMapper::entityToReadGroupTransactionsResponse);
        } catch (Exception e) {
            throw new Exception("Group not found.");
        }
    }

    public GroupReadGroupTransactionsResponse readGroupTransactions(
            Long groupId,
            Optional<String> categoriesString,
            Optional<String> dateTimeFrom,
            Optional<String> dateTimeTo,
            Optional<String> memberIdsString
    ) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        List<Transaction> transactions = TransactionService.filterTransactions(
                group.getTransactions(),
                categoriesString,
                dateTimeFrom,
                dateTimeTo,
                memberIdsString
        );
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

    public Page<LogReadLogResponse> readGroupLogs(Long groupId, Pageable pageable) throws Exception {
        try {
            return logService.readLogs(groupId, pageable).map(logMapper::entityToReadLogResponse);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<User> getUsersByGroupId(Long groupId) {
        return groupRepository.findUsersByGroupId(groupId);
    }

    public GroupUpdateGroupNameResponse updateGroupName(Long groupId, String name) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found"));
        if (name.isBlank()) {
            throw new Exception("New name is empty");
        }

        String pastName = group.getName();
        group.setName(name);

        NotificationContent notificationContent = new NotificationContent(
                "Group name updated", "The group name has been changed from " + pastName + " to " + name);

        List<User> users = getUsersByGroupId(groupId);
        notificationService.createNotifications(users, group, notificationContent);
        userService.saveAll(users);
        groupRepository.save(group);
        return groupMapper.entityToUpdateGroupNameResponse(group);
    }

    public static Member findNextPayer(Group group) {
        return switch (group.getPayerStrategy()) {
            case LOWEST_BALANCE -> group.getMembers().stream()
                    .min(Comparator.comparing(Member::getBalance))
                    .orElseThrow(() -> new IllegalArgumentException("Group with no members"));
            case WEIGHTED_RANDOM -> {
                float highestBalance = group.getMembers().stream()
                        .max(Comparator.comparing(Member::getBalance))
                        .orElseThrow(() -> new IllegalArgumentException("Group with no members"))
                        .getBalance();

                float totalBalance = 0f;
                NavigableMap<Float, Member> weightedMembers = new TreeMap<>();
                for (Member member : group.getMembers()) {
                    float adjustedBalance = highestBalance - member.getBalance();
                    if (adjustedBalance != 0) {
                        totalBalance += adjustedBalance;
                        weightedMembers.put(totalBalance, member);
                    }
                }

                Random random = new Random();
                float randomValue = random.nextFloat() * totalBalance;
                yield weightedMembers.higherEntry(randomValue).getValue();
            }
            case PURE_RANDOM -> {
                Random random = new Random();
                int randomIndex = random.nextInt(group.getMembers().size());
                yield group.getMembers().get(randomIndex);
            }
        };
    }
}
