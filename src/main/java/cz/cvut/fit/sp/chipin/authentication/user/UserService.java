package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserMapper;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupMapper;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupReadGroupMembersResponse;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;

    public UserReadUserResponse readUser(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return userMapper.entityToReadUserResponse(user);
        } else {
            throw new Exception("user doesn't exists(getUserAccount() method in UserAccountService)");
        }
    }

    public List<GroupReadGroupMembersResponse> readUserGroups(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Member> members = user.getMembers();
            List<Group> groups = members.stream().map(Member::getGroup).toList();
            return groups.stream().map(groupMapper::entityToReadGroupMembersResponse).toList();
        } else {
            throw new Exception("user doesn't exists(getUserAccount() method in UserAccountService)");
        }
    }


    public List<UserReadUserResponse> readAllUserAccounts() {
        List<User> userAccounts = userRepository.findAll();
        return userAccounts.stream().map(userMapper::entityToReadUserResponse).toList();
    }


    public User getUser(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new Exception("user doesn't exists(getUser() method in UserService)");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<MemberDTO> getMemberships(String id) throws Exception {
        User user = getUser(id);

        List<MemberDTO> memberships = new ArrayList<>();

        for (Member member : user.getMembers()) {
            memberships.add(new MemberDTO(
                    member.getId().getGroupId(),
                    member.getRole().name(),
                    member.getPaid(),
                    member.getSpent(),
                    member.getBalance()));
        }

        return memberships;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<Long> readMyGroups(String id) throws Exception {
        List<Member> members = getUser(id).getMembers();
        List<Long> result = new ArrayList<>();
        for (Member member : members) {
            result.add(member.getId().getGroupId());
        }
        return result;
    }
}
