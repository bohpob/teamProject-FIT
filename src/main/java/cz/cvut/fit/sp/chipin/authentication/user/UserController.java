package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public String readMyself(Principal principal) {
        try {
            return "username: " + principal.getName();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/me/user-groups")
    public List<Long> readMyGroups(Principal principal) {
        try {
            return userService.readMyGroups(principal.getName());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping
    public List<User> getAllUserAccounts() {
        return userService.getAllUsers();
    }

    @GetMapping("/memberships")
    List<MemberDTO> getMemberships(Principal principal) throws Exception {
        return userService.getMemberships(principal.getName());
    }

}