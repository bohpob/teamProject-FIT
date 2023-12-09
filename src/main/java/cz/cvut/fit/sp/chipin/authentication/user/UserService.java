package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserMapper;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
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

    public UserReadUserResponse readUser(String id) throws Exception {
        User userAccount = userRepository.findById(id).orElse(null);
        if (userAccount != null) {
            return userMapper.entityToReadUserResponse(userAccount);
        } else {
            throw new Exception("userAccount doesn't exists(getUserAccount() method in UserAccountService)");
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
