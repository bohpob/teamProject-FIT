package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.debt.DebtKeyDTO;
import cz.cvut.fit.sp.chipin.base.member.MemberRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
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

    @GetMapping("/{group_id}")
    public ResponseEntity<GroupResponse> readGroup(@PathVariable Long group_id) throws Exception {
        try {
            return groupService.readGroup(group_id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{group_id}/join")
    public String addMember(@Valid @RequestBody MemberRequest request, @PathVariable Long group_id) throws Exception {
        return groupService.addMember(request.getId(), group_id);
    }

    @PostMapping("/{group_id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateRequest transactionCreateRequest, @PathVariable Long group_id) throws Exception {
        try {
            return ResponseEntity.ok(groupService.createTransaction(transactionCreateRequest, group_id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{group_id}/transactions/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> readTransaction(@PathVariable Long group_id, @PathVariable Long transaction_id) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readTransaction(transaction_id, group_id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/{group_id}/transactions/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> updateTransaction(@Valid @RequestBody TransactionUpdateRequest transactionUpdateRequest, @PathVariable Long group_id, @PathVariable Long transaction_id) throws Exception {
        try {
            return ResponseEntity.ok(groupService.updateTransaction(transactionUpdateRequest, group_id, transaction_id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{group_id}/transactions/{transaction_id}")
    public void deleteTransaction(@PathVariable Long group_id, @PathVariable Long transaction_id) throws Exception {
        try {
            groupService.deleteTransaction(transaction_id, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{group_id}/debt/repayment")
    public void repaymentDebt(@PathVariable Long group_id, @Valid @RequestBody DebtKeyDTO dto) throws Exception {
        try {
            groupService.repaymentDebt(group_id, dto);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
