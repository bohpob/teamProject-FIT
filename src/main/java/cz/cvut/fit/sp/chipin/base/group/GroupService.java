package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.authentication.registration.RegistrationRequest;
import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRepository;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.membership.GroupRole;
import cz.cvut.fit.sp.chipin.base.membership.Membership;
import cz.cvut.fit.sp.chipin.base.membership.MembershipRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    public String create(GroupCreateRequest request) throws Exception {
        try {
            User user = userService.getUser(request.getUserId());

            if (request.getName() == null || request.getCurrency() == null)
                throw new Exception("Name and Currency fields cannot be empty");

            Group group = new Group();
            group.setName(request.getName());
            group.setCurrency(Currency.valueOf(request.getCurrency()));

            Membership membership = new Membership(user, group, GroupRole.ADMIN, 0f, 0f, 0f);

            group.addMembership(membership);

            user.addMembership(membership);

            membershipRepository.save(membership);
            userRepository.save(user);
            groupRepository.save(group);

            return "Created";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
