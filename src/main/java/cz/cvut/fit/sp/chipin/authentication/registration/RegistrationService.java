package cz.cvut.fit.sp.chipin.authentication.registration;

import cz.cvut.fit.sp.chipin.authentication.email.EmailValidator;
import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserRole;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {

        boolean isValid = emailValidator.test(request.getEmail());
        if (!isValid)
            throw new IllegalStateException("Email is not valid");

        userService.saveUser(
                new User(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER));
        return "User created";
    }

}
