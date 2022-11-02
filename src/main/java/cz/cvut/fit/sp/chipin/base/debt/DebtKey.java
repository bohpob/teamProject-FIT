package cz.cvut.fit.sp.chipin.base.debt;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DebtKey implements Serializable {

    @NotBlank
    @Column(name = "lender_id")
    private Long lenderId;

    @NotBlank
    @Column(name = "borrower_id")
    private Long borrowerId;

    @NotBlank
    @Column(name = "group_id")
    private Long groupId;
}
