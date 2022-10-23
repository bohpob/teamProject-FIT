package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.log.Log;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "group")
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;

    @OneToMany(mappedBy = "group_id")
    private List<Debt> debts = new ArrayList<>();

    @OneToMany(mappedBy = "group_id")
    private List<Log> logs = new ArrayList<>();

    @NotBlank
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
