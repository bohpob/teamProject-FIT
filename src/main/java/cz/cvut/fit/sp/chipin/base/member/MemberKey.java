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
    @Column(name = "user_entity_id")
    private String userId;

    @NotNull
    @Column(name = "user_group_id")
    private Long groupId;
}
