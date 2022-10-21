package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "debt")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Debt {
    @Id
    @SequenceGenerator(
            name = "debt_sequence",
            sequenceName = "debt_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "debt_sequence"
    )
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne()
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @NotBlank
    @Column(name = "amount", nullable = false)
    private float amount;
}
