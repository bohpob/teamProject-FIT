package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.usergroup.mapper.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-groups")
@AllArgsConstructor
public class UserGroupController {
    private final UserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<GroupCreateGroupResponse> createGroup(@Valid @RequestBody GroupCreateGroupRequest request,
                                                                Principal principal) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.createGroup(request, principal.getName()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupReadGroupResponse> readGroup(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readGroup(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{groupId}/join")
    public String addMember(@Valid @RequestParam String userId, @PathVariable Long groupId) throws Exception {
        try {
            return userGroupService.addMember(userId, groupId);
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
    public ResponseEntity<TransactionReadGroupTransactionResponse> readTransaction(@PathVariable Long groupId,
                                                                                   @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readGroupTransaction(transactionId, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GroupReadGroupTransactionsResponse> readGroupTransactions(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readGroupTransactions(groupId));
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
    public void settleDebt(@PathVariable Long groupId, @RequestParam String lenderId,
                           @RequestParam String borrowerId) throws Exception {
        try {
            userGroupService.settleDebt(groupId, lenderId, borrowerId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{groupId}/logs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GroupReadGroupLogsResponse> readLogs(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.readGroupLogs(groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupUpdateGroupNameResponse> updateGroupName(@PathVariable Long groupId,
                                                                  @RequestParam @NotBlank String name) throws Exception {
        try {
            return ResponseEntity.ok(userGroupService.updateGroupName(groupId, name));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
