package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userAccounts")
@AllArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("{id}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userAccountService.getUserAccount(id));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping
    public List<UserAccount> getAllUserAccounts() {
        return userAccountService.getAllUserAccounts();
    }

    @GetMapping("{id}/memberships")
    List<MemberDTO> getMemberships(@PathVariable Long id) throws Exception {
        return userAccountService.getMemberships(id);
    }

}