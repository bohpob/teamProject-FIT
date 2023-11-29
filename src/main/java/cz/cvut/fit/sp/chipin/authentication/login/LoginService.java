package cz.cvut.fit.sp.chipin.authentication.login;

import cz.cvut.fit.sp.chipin.authentication.email.EmailSender;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationToken;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationTokenService;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserAccountService userAccountService;

    @Autowired
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    public LoginResponse login(LoginRequest loginRequest) {

//        UserAccount userAccount = userAccountService.loadUserAccountByUserAccountname(loginRequest.getEmail());
        UserAccount userAccount = new UserAccount();

        if (!userAccount.isEnabled()) {
            if (!userAccountService.userAccountHasActiveToken(userAccount.getId())) {
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        userAccount
                );

                confirmationTokenService.saveConfirmationToken(confirmationToken);

                emailSender.sendConfirmation(
                        userAccount.getEmail(),
                        userAccount.getName(),
                        token
                );
            }

        }

//        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), userAccount.getPassword())) {
//            return new LoginResponse(userAccount.getId(), userAccount.getName(), userAccount.getEmail(), userAccount.getEnabled());
//        } else {
//            throw new IllegalStateException("Invalid credentials");
//        }

        return new LoginResponse(userAccount.getId(), userAccount.getName(), userAccount.getEmail(), userAccount.getEnabled());

    }

}
