package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.*;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountRepository;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.debt.DebtConverter;
import cz.cvut.fit.sp.chipin.base.debt.DebtRepository;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.log.*;
import cz.cvut.fit.sp.chipin.base.membership.GroupRole;
import cz.cvut.fit.sp.chipin.base.membership.Member;
import cz.cvut.fit.sp.chipin.base.membership.MemberRepository;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionConverter;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionGroupResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {

    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final LogRepository logRepository;
    private final TransactionRepository transactionRepository;
    private final DebtRepository debtRepository;

    public String create(GroupCreateRequest request) throws Exception {
        User user = userService.getUser(request.getUserId());

        if (request.getName() == null || request.getCurrency() == null)
            throw new Exception("Name and Currency fields cannot be empty");

        Group group = new Group();
        group.setName(request.getName());
        group.setCurrency(Currency.valueOf(request.getCurrency()));
        groupRepository.save(group);

        Member member = new Member(user, group, GroupRole.ADMIN, 0f, 0f, 0f);
        memberRepository.save(member);

        group.addMembership(member);
        user.addMembership(member);

        groupRepository.save(group);
        userRepository.save(user);

        logService.create("created the group.", group, user);
        return "Created";
    }

    public ResponseEntity<GroupResponse> read(Long group_id) throws Exception {
        Optional<Group> group = groupRepository.findById(group_id);
        if (group.isEmpty())
            throw new Exception("Group not found");

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.get().getName());
        groupResponse.setCurrency(group.get().getCurrency());

        ArrayList<Member> members = memberRepository.findMembersByGroupId(group_id);
        groupResponse.setUsers(UserConverter.toUsersGroupResponse(members));

        ArrayList<Long> ids = transactionRepository.getTransactionIdsByGroupId(group_id);
        groupResponse.setTransactions(TransactionConverter.toTransactionsGroupResponse(transactionRepository.findAllById(ids)));

        ArrayList<Debt> debts = debtRepository.findDebtsByGroupId(group_id);
        groupResponse.setDebts(debts.stream().map(DebtConverter::toDebtGroupResponse).collect(Collectors.toList()));

        ArrayList<Log> logs = logRepository.findAllByGroupId(group.get().getId());
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

        memberRepository.save(member);
        userRepository.save(user);
        groupRepository.save(group);

        logService.create("joined the group.", group, user);
        return "User joined";
    }

    public void acceptTxCreate(Transaction transaction) throws Exception {
        Member payerMember = memberRepository.findByUserIdAndGroupId(transaction.getPayer().getUser().getId(),
                transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.setPaid(payerMember.getPaid() + transaction.getAmount());
        memberRepository.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberRepository.findByUserIdAndGroupId(amount.getUser().getId(),
                    transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.setSpent(member.getSpent() + amount.getAmount());
            memberRepository.save(member);

            spent.put(amount.getUser(), amount.getAmount());
        }

        debtService.recalculate(spent, transaction.getPayer().getUser(), transaction.getPayer().getGroup());
    }

    public void acceptTxDelete(Transaction transaction) throws Exception {
        Member payerMember = memberRepository.findByUserIdAndGroupId(transaction.getPayer().getUser().getId(),
                transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Payer is not found"));

        payerMember.decreasePaid(transaction.getAmount());
        memberRepository.save(payerMember);

        Map<User, Float> spent = new HashMap<>();

        for (Amount amount : transaction.getAmounts()) {
            Member member = memberRepository.findByUserIdAndGroupId(amount.getUser().getId(),
                    transaction.getPayer().getGroup().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));

            member.decreaseSpent(amount.getAmount());
            memberRepository.save(member);

            spent.put(amount.getUser(), amount.reverse());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getGroup());
    }
}
