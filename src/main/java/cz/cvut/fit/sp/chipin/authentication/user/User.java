package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.notification.Notification;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_entity")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Amount> amounts = new ArrayList<>();

    @OneToMany(mappedBy = "lender")
    private List<Debt> lendDebts = new ArrayList<>();

    @OneToMany(mappedBy = "borrower")
    private List<Debt> borrowDebts = new ArrayList<>();

    @OneToMany(mappedBy = "payer")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

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

    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    public void addMembership(Member member) {
        members.add(member);
    }
}
