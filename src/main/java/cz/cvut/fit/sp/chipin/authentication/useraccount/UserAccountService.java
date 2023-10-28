package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationToken;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationTokenService;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroupResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAccountService {

    private final static String USER_NOT_FOUND = "UserAccount with email %s not found";

    private final UserAccountRepository userAccountRepository;
    private final ConfirmationTokenService confirmationTokenService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Override
//    public UserAccount loadUserAccountByUserAccountname(String email) throws UserAccountnameNotFoundException {
//        return userAccountRepository.findUserAccountByEmail(email)
//                .orElseThrow(() -> new UserAccountnameNotFoundException(String.format(USER_NOT_FOUND, email)));
//    }

    private UserAccount findUserAccountByIdOrCreate(String id) {
        return userAccountRepository.findById(id).orElseGet(() -> {
            UserAccount newUser = new UserAccount();
            newUser.setId(id);
            return userAccountRepository.save(newUser);
        });
    }

    public String saveUserAccount(UserAccount userAccount) {
        if (userAccountRepository.findUserAccountByEmail(userAccount.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }

        userAccountRepository.save(userAccount);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userAccount
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

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

    public int enableUserAccount(String email) {
        return userAccountRepository.enableUserAccount(email);
    }

    public boolean userAccountHasActiveToken(String id) {
        for (ConfirmationToken token : confirmationTokenService.getAllTokensByUserAccountId(id)) {
            if (token.getExpiresAt().isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
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

    public void save(UserAccount userAccount) throws Exception {
        userAccountRepository.save(userAccount);
    }

    public List<Long> readMyGroups(String name) {
        List<Member> members = findUserAccountByIdOrCreate(name).getMembers();
        List<Long> result = new ArrayList<>();
        for (Member member : members) {
            result.add(member.getId().getGroupId());
        }
        return result;
    }
}
