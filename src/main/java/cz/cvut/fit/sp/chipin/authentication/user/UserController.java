package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserTransactionsResponse;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupReadGroupMembersResponse;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserReadUserResponse> readUser(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.readUser(userId));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserReadUserResponse> readMyself(Principal principal) {
        try {
            return ResponseEntity.ok(userService.readUser(principal.getName()));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/me/groups")
    public ResponseEntity<List<GroupReadGroupMembersResponse>> readMyGroups(Principal principal) {
        try {
            return ResponseEntity.ok(userService.readUserGroups(principal.getName()));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping("/me/transactions")
    public ResponseEntity<UserReadUserTransactionsResponse> readMyTransactions(Principal principal) {
        try {
            return ResponseEntity.ok(userService.readUserTransactions(principal.getName()));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    //TODO: update DTO
    @GetMapping("/me/memberships")
    ResponseEntity<List<MemberDTO>> readMyMemberships(Principal principal) {
        try {
            return ResponseEntity.ok(userService.getMemberships(principal.getName()));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e
            );
        }
    }
}