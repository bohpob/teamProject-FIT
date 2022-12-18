package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationToken;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationTokenService;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import cz.cvut.fit.sp.chipin.base.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String USER_NOT_FOUND = "User with email %s not found";

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String saveUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new IllegalStateException("Email already taken");

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: send e-mail

        return token;
    }

    public User getUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null)
            return user;
        throw new Exception("user doesn't exists(getUser() method in UserService)");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }


    public boolean userHasActiveToken(Long id) {
        for (ConfirmationToken token :
                confirmationTokenService.getAllTokensByUserId(id)) {
            if (token.getExpiresAt().isAfter(LocalDateTime.now()))
                return true;
        }
        return false;
    }

    public List<MemberDTO> getMemberships(Long id) throws Exception {
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

    public void save(User user) throws Exception {
        userRepository.save(user);
    }
}
