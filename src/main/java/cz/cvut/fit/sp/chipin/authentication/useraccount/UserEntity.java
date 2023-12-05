package cz.cvut.fit.sp.chipin.authentication.useraccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_entity")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "user_entity_id")
    private String id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserAccount userAccount;

    private String email;
    private String emailConstraint;
    private Boolean emailVerified;
    private Boolean enabled;
    private String federationLink;
    private String firstName;
    private String lastName;
    private String realmId;
    private String username;
    private Long createdTimestamp;
    private String serviceAccountClientLink;
    private Integer notBefore;
}
