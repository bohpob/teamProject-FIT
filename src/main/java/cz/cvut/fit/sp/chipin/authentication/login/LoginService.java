package cz.cvut.fit.sp.chipin.authentication.login;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userService.loadUserByUsername(loginRequest.getEmail());

//        String encodedPassword = bCryptPasswordEncoder.encode(loginRequest.getPassword());

        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(user.getId(), user.getName(), user.getEmail());
        } else {
            throw new IllegalStateException("Invalid credentials");
        }

    }

}
