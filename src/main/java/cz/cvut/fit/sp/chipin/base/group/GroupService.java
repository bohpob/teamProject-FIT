package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountRepository;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.membership.GroupRole;
import cz.cvut.fit.sp.chipin.base.membership.Member;
import cz.cvut.fit.sp.chipin.base.membership.MemberRepository;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupService {

    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final AmountRepository amountRepository;

    public String create(GroupCreateDTO request) throws Exception {
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

    public String acceptTxUpdate(Transaction transaction, TransactionUpdateRequest transactionUpdateRequest, Member prevPayer, Member nextPayer) {
        String log = "";
        if (!Objects.equals(transaction.getDate(), transactionUpdateRequest.getDate())) {
            log += "transaction date changed from " + transaction.getDate() + " to " + transactionUpdateRequest.getDate() + ". ";
            transaction.setDate(transactionUpdateRequest.getDate());
        }
        if (!Objects.equals(transaction.getName(), transactionUpdateRequest.getName())) {
            log += "transaction name changed from " + transaction.getName() + " to " + transactionUpdateRequest.getName() + ". ";
            transaction.setName(transactionUpdateRequest.getName());
        }
        if (!Objects.equals(prevPayer.getUser().getId(), nextPayer.getUser().getId())) {
            log += "payer changed from " + prevPayer.getUser().getName() + " to " + nextPayer.getUser().getName() + ". ";
            transaction.setPayer(nextPayer);
        }
        if (!Objects.equals(transaction.getAmount(), transactionUpdateRequest.getAmount())) {
            log += "transaction amount changed from " + transaction.getAmount() + " to " + transactionUpdateRequest.getAmount() + ". ";
            transaction.setAmount(transactionUpdateRequest.getAmount());
        }
        if (!compareSpendersIds(transaction.getAmounts(), transactionUpdateRequest.getSpenderIds())) {
            log += "participants in the transaction have changed.";
        }
        return log;
    }

    private boolean compareSpendersIds(List<Amount> amounts, List<Long> spendersIds) {
        if (spendersIds.size() == amounts.size()) {
            amounts.sort(new Comparator<Amount>() {
                @Override
                public int compare(Amount o1, Amount o2) {
                    return o1.getUser().getId().compareTo(o2.getUser().getId());
                }
            });
            spendersIds.sort(new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int i = 0; i < amounts.size(); i++) {
                if (!Objects.equals(amounts.get(i).getUser().getId(), spendersIds.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
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

            amountRepository.deleteById(amount.getId());
        }

        debtService.recalculate(spent, payerMember.getUser(), payerMember.getGroup());
    }
}
