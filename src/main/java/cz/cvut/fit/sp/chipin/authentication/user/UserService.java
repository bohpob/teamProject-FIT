package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserMapper;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserTransactionsResponse;
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
            List<Group> groups = user.getMembers().stream().map(Member::getGroup).toList();
            return groups.stream().map(groupMapper::entityToReadGroupMembersResponse).toList();
        } else {
            throw new Exception("user with id: " + id + " doesn't exists");
        }
    }

    public UserReadUserTransactionsResponse readUserTransactions(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return userMapper.entityToReadUserTransactionsResponse(user);
        } else {
            throw new Exception("user with id: " + id + " doesn't exists");
        }
    }

    public User getUser(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new Exception("user doesn't exists(getUser() method in UserService)");
        }
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
}
