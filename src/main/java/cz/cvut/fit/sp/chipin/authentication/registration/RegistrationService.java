package cz.cvut.fit.sp.chipin.authentication.registration;

import cz.cvut.fit.sp.chipin.authentication.email.EmailSender;
import cz.cvut.fit.sp.chipin.authentication.email.EmailValidator;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationToken;
import cz.cvut.fit.sp.chipin.authentication.email.token.ConfirmationTokenService;
import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRole;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Properties;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {

        boolean isValid = emailValidator.test(request.getEmail());
        if (!isValid) {
            throw new IllegalStateException("Email is not valid");
        }

        String token = userService.saveUser(
                new User(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER
                ));

        emailSender.sendConfirmation(
                request.getEmail(),
                request.getName(),
                token
        );

        return token;
    }

    @Transactional()
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
//            throw new IllegalStateException("Email already confirmed");
            return "Email already confirmed";
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("Token expired");
            return "Token expired";
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail()
        );
        return "Confirmed";
    }

}
