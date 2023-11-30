package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccount getUserAccount(String id) throws Exception {
        UserAccount userAccount = userAccountRepository.findById(id).orElse(null);
        if (userAccount != null) {
            return userAccount;
        } else {
            throw new Exception("userAccount doesn't exists(getUserAccount() method in UserAccountService)");
        }
    }

    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.findAll();
    }

    public List<MemberDTO> getMemberships(String id) throws Exception {
        UserAccount userAccount = getUserAccount(id);

        List<MemberDTO> memberships = new ArrayList<>();

        for (Member member : userAccount.getMembers()) {
            memberships.add(new MemberDTO(
                    member.getId().getGroupId(),
                    member.getRole().name(),
                    member.getPaid(),
                    member.getSpent(),
                    member.getBalance()));
        }

        return memberships;
    }

    public void save(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    public List<Long> readMyGroups(String id) throws Exception {
        List<Member> members = getUserAccount(id).getMembers();
        List<Long> result = new ArrayList<>();
        for (Member member : members) {
            result.add(member.getId().getGroupId());
        }
        return result;
    }
}
