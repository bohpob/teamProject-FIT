package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.base.log.LogsGroupResponse;
import cz.cvut.fit.sp.chipin.base.member.MemberRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionsGroupResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-groups")
@AllArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public String createGroup(@Valid @RequestBody UserGroupCreateRequest request) throws Exception {
        try {
            return userGroupService.createGroup(request);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<UserGroupResponse> readGroup(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readGroup(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{groupId}/join")
    public String addMember(@Valid @RequestBody MemberRequest request, @PathVariable Long groupId) throws Exception {
        try {
            return userGroupService.addMember(request.getId(), groupId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateRequest request,
                                                                 @PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.createTransaction(request, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> readTransaction(@PathVariable Long groupId,
                                                               @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readTransaction(transactionId, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionsGroupResponse> readTransactions(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readTransactions(groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> updateTransaction(@Valid @RequestBody TransactionUpdateRequest request,
                                                                 @PathVariable Long groupId,
                                                                 @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.updateTransaction(request, groupId, transactionId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/transactions/{transactionId}")
    public void deleteTransaction(@PathVariable Long groupId, @PathVariable Long transactionId) throws Exception {
        try {
            userGroupService.deleteTransaction(transactionId, groupId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/debt/repayment")
    public void settleDebt(@PathVariable Long groupId, @RequestParam("lenderId") String lenderId,
                           @RequestParam("borrowerId") String borrowerId) throws Exception {
        try {
            userGroupService.settleDebt(groupId, lenderId, borrowerId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{groupId}/logs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogsGroupResponse> readLogs(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readLogs(groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PatchMapping("/{groupId}/name")
    public ResponseEntity<UserGroupResponse> changeGroupName(@PathVariable Long groupId,
                                                             @RequestParam("name") String name) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.changeGroupName(groupId, name));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
