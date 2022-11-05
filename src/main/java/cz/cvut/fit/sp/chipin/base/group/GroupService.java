package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.registration.RegistrationRequest;
import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.DebtService;
import cz.cvut.fit.sp.chipin.base.log.LogService;
import cz.cvut.fit.sp.chipin.base.membership.GroupRole;
import cz.cvut.fit.sp.chipin.base.membership.Membership;
import cz.cvut.fit.sp.chipin.base.membership.MembershipRepository;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GroupService {

    private final UserService userService;
    private final DebtService debtService;
    private final LogService logService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    public String create(GroupCreateRequest request) throws Exception {
        User user = userService.getUser(request.getUserId());

        if (request.getName() == null || request.getCurrency() == null)
            throw new Exception("Name and Currency fields cannot be empty");

        Group group = new Group();
        group.setName(request.getName());
        group.setCurrency(Currency.valueOf(request.getCurrency()));
        groupRepository.save(group);

        Membership membership = new Membership(user, group, GroupRole.ADMIN, 0f, 0f, 0f);
        membershipRepository.save(membership);

        group.addMembership(membership);
        user.addMembership(membership);

        groupRepository.save(group);
        userRepository.save(user);

        return "Created";
    }

    public String addMember(Long userId, Long groupId) throws Exception {

        User user = userService.getUser(userId);
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));

        for (Membership membership : group.getMemberships()) {
            if (membership.getUser().equals(user))
                return "User already member of this group";
        }

        Membership membership = new Membership(user, group, GroupRole.USER, 0f, 0f, 0f);
        group.addMembership(membership);
        user.addMembership(membership);

        membershipRepository.save(membership);
        userRepository.save(user);
        groupRepository.save(group);

        return "User joined";
    }

    public void acceptTxCreate(Transaction transaction) throws Exception {
        Membership payerMembership = membershipRepository.findByUserId(transaction.getPayer().getId()).orElseThrow(() -> new Exception("Payer is not found"));
        payerMembership.setPaid(payerMembership.getPaid() + transaction.getAmount());
        membershipRepository.save(payerMembership);

        for (Amount amount: transaction.getAmounts()) {
            Membership membership  = membershipRepository.findByUserId(amount.getUser().getId()).orElseThrow(() -> new Exception("Transaction participant is not found"));
            membership.setSpent(membership.getSpent() + amount.getAmount());
            membershipRepository.save(membership);
        }

//        debtService.recalculate();
        logService.create("created transaction: ", transaction.getGroup(), transaction.getPayer());
    }
}
