package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserMapper;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserTransactionsResponse;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupMapper;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupReadGroupMembersResponse;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import cz.cvut.fit.sp.chipin.base.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
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

    public Page<GroupReadGroupMembersResponse> readUserGroups(String id, Pageable pageable) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            //get all user's groups and map them to GroupReadGroupMembersResponse and return page of them
            List<Group> groups = user.getMembers().stream().map(Member::getGroup).toList();
            List<GroupReadGroupMembersResponse> groupResponses = groups.stream().map(groupMapper::entityToReadGroupMembersResponse).toList();
            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;

            List<GroupReadGroupMembersResponse> pageContent;

            if (groupResponses.size() < startItem) {
                pageContent = new ArrayList<>();
            } else {
                int toIndex = Math.min(startItem + pageSize, groupResponses.size());
                pageContent = groupResponses.subList(startItem, toIndex);
            }

            return new PageImpl<>(pageContent, pageable, groupResponses.size());
        } else {
            throw new Exception("user with id: " + id + " doesn't exists");
        }
    }

    public Page<UserReadUserTransactionsResponse> readUserTransactions(String id, Pageable pageable) throws Exception {
        try {
            Page<User> user = userRepository.findById(id, pageable);
            return user.map(userMapper::entityToReadUserTransactionsResponse);
//            return userMapper.entityToReadUserTransactionsResponse(user);
        } catch (Exception e){
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

    public Page<MemberDTO> getMemberships(String id, Pageable pageable) throws Exception {
       /* User user = getUser(id);

        List<MemberDTO> memberships = new ArrayList<>();

        for (Member member : user.getMembers()) {
            memberships.add(new MemberDTO(
                    member.getId().getGroupId(),
                    member.getRole().name(),
                    member.getPaid(),
                    member.getSpent(),
                    member.getBalance()));
        }

        return memberships;*/
        Page<Member> members = memberRepository.findByUserId(id, pageable);
        return members.map(memberMapper -> new MemberDTO(
                memberMapper.getId().getGroupId(),
                memberMapper.getRole().name(),
                memberMapper.getPaid(),
                memberMapper.getSpent(),
                memberMapper.getBalance()));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
