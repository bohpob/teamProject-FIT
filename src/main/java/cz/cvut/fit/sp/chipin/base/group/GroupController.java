package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.group.mapper.*;
import cz.cvut.fit.sp.chipin.base.group.swagger.GroupSwaggerExamples;
import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Group Controller")
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "Create a new group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = GroupCreateGroupResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_CREATE_GROUP))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content)})
    @PostMapping
    public ResponseEntity<GroupCreateGroupResponse> createGroup(@Valid @RequestBody GroupCreateGroupRequest request,
                                                                Principal principal) throws Exception {
        try {
            return ResponseEntity.ok(groupService.createGroup(request, principal.getName()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Read a group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = GroupReadGroupResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_READ_GROUP))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupReadGroupResponse> readGroup(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroup(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Operation(summary = "Read group's hex code by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = String.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_READ_HEX_CODE))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}/hexCode")
    public ResponseEntity<String> readHexCode(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readHexCode(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Operation(summary = "Join group by hex code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = String.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_JOIN_GROUP))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @PatchMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestParam String hexCode, Principal principal) throws Exception {
        try {
            return ResponseEntity.ok(groupService.addMember(principal.getName(), hexCode));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Read next payer's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = String.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_READ_NEXT_PAYER_ID))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}/next")
    public ResponseEntity<String> readNextPayerId(@PathVariable Long groupId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readNextPayerId(groupId));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Operation(summary = "Update next payer strategy and checking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = String.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_UPDATE_NEXT_PAYER))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
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

    @Operation(summary = "Create new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TransactionCreateTransactionResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_CREATE_TRANSACTION))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
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

    @Operation(summary = "Read transaction by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TransactionReadGroupTransactionResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_TRANSACTION_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionReadGroupTransactionResponse> readTransaction(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "Transaction ID") @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransaction(transactionId, groupId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Read all group transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TransactionReadGroupTransactionsResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_ALL_TRANSACTIONS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TransactionReadGroupTransactionsResponse>> readGroupTransactions(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(hidden = true) Pageable pageable) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransactions(groupId, pageable));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Read group transaction filtered")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = GroupReadGroupTransactionsResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_TRANSACTION_WITH_FILTERS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{groupId}/transactions/search")
    public ResponseEntity<GroupReadGroupTransactionsResponse> readGroupTransactionsFiltered(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "Categories") @RequestParam Optional<String> categories,
            @Parameter(name = "Date time from") @RequestParam Optional<String> dateTimeFrom,
            @Parameter(name = "Date time to") @RequestParam Optional<String> dateTimeTo,
            @RequestParam Optional<String> memberIds
    ) throws Exception {
        try {
            return ResponseEntity.ok(groupService.readGroupTransactions(groupId, categories, dateTimeFrom, dateTimeTo, memberIds));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Update transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TransactionUpdateTransactionResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_UPDATE_TRANSACTION))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @PutMapping("/{groupId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionUpdateTransactionResponse> updateTransaction(
            @Parameter(name = "Updated transaction") @Valid @RequestBody TransactionUpdateRequest request,
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "Transaction ID") @PathVariable Long transactionId) throws Exception {
        try {
            return ResponseEntity.ok(groupService.updateTransaction(request, groupId, transactionId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Delete transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @DeleteMapping("/{groupId}/transactions/{transactionId}")
    public void deleteTransaction(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "Transaction ID") @PathVariable Long transactionId) throws Exception {
        try {
            groupService.deleteTransaction(transactionId, groupId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Operation(summary = "Settle debt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @DeleteMapping("/{groupId}/debt/repayment")
    public void settleDebt(
            @Parameter(name = "Group ID") @PathVariable Long groupId,
            @Parameter(name = "Lender ID") @RequestParam String lenderId,
            @Parameter(name = "Borrower ID") @RequestParam String borrowerId) throws Exception {
        try {
            groupService.settleDebt(groupId, lenderId, borrowerId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Operation(summary = "Read group logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = LogReadLogResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = GroupSwaggerExamples.EXAMPLE_GROUP_LOGS))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
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
