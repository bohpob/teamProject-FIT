package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.log.LogsGroupResponse;
import cz.cvut.fit.sp.chipin.base.member.MemberRequest;
import cz.cvut.fit.sp.chipin.base.transaction.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public String createGroup(@Valid @RequestBody GroupCreateRequest request) throws Exception {
        return groupService.createGroup(request);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> readGroup(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroup(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{groupId}/join")
    public String addMember(@Valid @RequestBody MemberRequest request, @PathVariable Long groupId) throws Exception {
        return groupService.addMember(request.getId(), groupId);
    }

    @PostMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateRequest request,
                                                                 @PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.createTransaction(request, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> readTransaction(@PathVariable Long groupId,
                                                               @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readTransaction(transactionId, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionsGroupResponse> readTransactions(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readTransactions(groupId));
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
            return ResponseEntity.ok(groupService.updateTransaction(request, groupId, transactionId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/transactions/{transactionId}")
    public void deleteTransaction(@PathVariable Long groupId, @PathVariable Long transactionId) throws Exception {
        try {
            groupService.deleteTransaction(transactionId, groupId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/debt/repayment")
    public void settleDebt(@PathVariable Long groupId, @RequestParam("lenderId") Long lenderId,
                           @RequestParam("borrowerId") Long borrowerId) throws Exception {
        try {
            groupService.settleDebt(groupId, lenderId, borrowerId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{groupId}/logs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogsGroupResponse> readLogs(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readLogs(groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
