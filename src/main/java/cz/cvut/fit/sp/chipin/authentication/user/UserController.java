package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserResponse;
import cz.cvut.fit.sp.chipin.authentication.user.mapper.UserReadUserTransactionsResponse;
import cz.cvut.fit.sp.chipin.authentication.user.swagger.UserSwaggerExamples;
import cz.cvut.fit.sp.chipin.base.group.mapper.GroupReadGroupMembersResponse;
import cz.cvut.fit.sp.chipin.base.member.MemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by id", description = "Returns user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserReadUserResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = UserSwaggerExamples.EXAMPLE_USER_RESPONSE_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/{userId}")
    public ResponseEntity<UserReadUserResponse> readUser(
            @Parameter(description = "Unique identifier of the user") @PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.readUser(userId));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @Operation(summary = "Get yourself", description = "Returns itself")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserReadUserResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = UserSwaggerExamples.EXAMPLE_USER_RESPONSE_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
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

    @Operation(summary = "Get the groups you're in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = GroupReadGroupMembersResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = UserSwaggerExamples.EXAMPLE_GROUP_RESPONSE_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/me/groups")
    public ResponseEntity<Page<GroupReadGroupMembersResponse>> readMyGroups
            (Principal principal, @Parameter(hidden = true) @PageableDefault(sort = {"id"},
                    direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.readUserGroups(principal.getName(), pageable));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @Operation(summary = "Get the transactions you participated in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserReadUserTransactionsResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = UserSwaggerExamples.EXAMPLE_TRANSACTION_RESPONSE_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/me/transactions")
    public ResponseEntity<Page<UserReadUserTransactionsResponse>> readMyTransactions(
            Principal principal, @Parameter(hidden = true) Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.readUserTransactions(principal.getName(), pageable));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @Operation(summary = "Get information about your membership in each group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = MemberDTO.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = UserSwaggerExamples.EXAMPLE_MEMBERSHIP_RESPONSE_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("/me/memberships")
    ResponseEntity<Page<MemberDTO>> readMyMemberships(Principal principal, @Parameter(hidden = true) Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.getMemberships(principal.getName(), pageable));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e
            );
        }
    }
}