package cz.cvut.fit.sp.chipin.base.balance;

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
public class BalanceKey implements Serializable {

    @NotBlank
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(name = "group_id")
    private Long groupId;
}
