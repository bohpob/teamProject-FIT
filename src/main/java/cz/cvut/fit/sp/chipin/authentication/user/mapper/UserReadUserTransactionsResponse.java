package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserReadUserTransactionsResponse {
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    @NotBlank
    private final List<TransactionReadGroupTransactionsResponse> transactions;
}
