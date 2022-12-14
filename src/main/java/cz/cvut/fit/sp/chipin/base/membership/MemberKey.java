package cz.cvut.fit.sp.chipin.base.membership;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MemberKey implements Serializable {

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;
}
