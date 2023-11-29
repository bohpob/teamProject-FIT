package cz.cvut.fit.sp.chipin.base.debt;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DebtKey implements Serializable {

    @NotBlank
    @Column(name = "user_group_id")
    private Long groupId;

    @NotBlank
    @Column(name = "lender_id")
    private String lenderId;

    @NotBlank
    @Column(name = "borrower_id")
    private String borrowerId;
}
