package cz.cvut.fit.sp.chipin.authentication.login;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
