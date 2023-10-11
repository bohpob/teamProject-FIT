package cz.cvut.fit.sp.chipin.authentication.login;

import cz.cvut.fit.sp.chipin.authentication.email.EmailSender;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationToken;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationTokenService;
import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserService userService;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userService.loadUserByUsername(loginRequest.getEmail());

        if (!user.isEnabled()) {
            if (!userService.userHasActiveToken(user.getId())) {
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );

                confirmationTokenService.saveConfirmationToken(confirmationToken);

                emailSender.sendConfirmation(
                        user.getEmail(),
                        user.getName(),
                        token
                );
            }

        }

        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getEnabled());
        } else {
            throw new IllegalStateException("Invalid credentials");
        }

    }

}
