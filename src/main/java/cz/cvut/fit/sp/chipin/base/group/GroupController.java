package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.group.mapper.*;
import cz.cvut.fit.sp.chipin.base.group.swagger.GroupSwaggerExamples;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionUpdateTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupCreateGroupResponse> createGroup(@Valid @RequestBody GroupCreateGroupRequest request,
                                                                Principal principal) throws Exception {
        try {
            return ResponseEntity.ok(groupService.createGroup(request, principal.getName()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupReadGroupResponse> readGroup(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroup(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{groupId}/hexCode")
    public ResponseEntity<String> readHexCode(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readHexCode(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestParam String hexCode, Principal principal) throws Exception {
        try {
            return ResponseEntity.ok(groupService.addMember(principal.getName(), hexCode));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/next")
    public ResponseEntity<String> readNextPayerId(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readNextPayerId(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{groupId}/next")
    public ResponseEntity<String> updateNextPayer(
            @PathVariable Long groupId,
            @RequestParam Optional<String> strategy,
            @RequestParam Optional<Boolean> check
    ) throws Exception {
        try {
            return ResponseEntity.ok(groupService.setCheckNextPayer(groupId, strategy, check));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PostMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionCreateTransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateTransactionRequest request,
                                                                                  @PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.createTransaction(request, groupId));
        } catch (Exception e) {
            if (e.getMessage().equals("Next payer check failed"))
                return ResponseEntity.badRequest().build();
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionReadGroupTransactionResponse> readTransaction(@PathVariable Long groupId,
                                                                                   @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransaction(transactionId, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TransactionReadGroupTransactionsResponse>> readGroupTransactions(
            @PathVariable Long groupId, Pageable pageable) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransactions(groupId, pageable));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/transactions/search")
    public ResponseEntity<GroupReadGroupTransactionsResponse> readGroupTransactionsFiltered(
            @PathVariable Long groupId,
            @RequestParam Optional<String> categories,
            @RequestParam Optional<String> dateTimeFrom,
            @RequestParam Optional<String> dateTimeTo,
            @RequestParam Optional<String> memberIds
    ) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransactions(groupId, categories, dateTimeFrom, dateTimeTo, memberIds));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionUpdateTransactionResponse> updateTransaction(@Valid @RequestBody TransactionUpdateRequest request,
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
    public void settleDebt(@PathVariable Long groupId, @RequestParam String lenderId,
                           @RequestParam String borrowerId) throws Exception {
        try {
            groupService.settleDebt(groupId, lenderId, borrowerId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{groupId}/logs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<LogReadLogResponse>> readLogs(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(hidden = true) Pageable pageable) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupLogs(groupId, pageable));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Group name update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = GroupUpdateGroupNameResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_GROUP_UPDATE_NAME))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupUpdateGroupNameResponse> updateGroupName(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "New group name") @RequestParam @NotBlank String name) throws Exception {
        try {
            return ResponseEntity.ok(groupService.updateGroupName(groupId, name));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
