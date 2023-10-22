package cz.cvut.fit.sp.chipin.base.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MemberKey implements Serializable {

    @NotNull
    @Column(name = "user_account_id")
    private Long userAccountId;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;
}
