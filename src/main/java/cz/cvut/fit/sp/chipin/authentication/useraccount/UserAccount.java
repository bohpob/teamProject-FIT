package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
@NoArgsConstructor
@Getter
@Setter
public class UserAccount {

    @Id
    @Column(name = "id")
    private String id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "userAccount")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "userAccount")
    private List<Amount> amounts = new ArrayList<>();

    @OneToMany(mappedBy = "lender")
    private List<Debt> lendDebts = new ArrayList<>();

    @OneToMany(mappedBy = "borrower")
    private List<Debt> borrowDebts = new ArrayList<>();

    @OneToMany(mappedBy = "payer")
    private List<Transaction> transactions = new ArrayList<>();

    public UserAccount(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getName() {
        return userEntity.getFirstName() + " " + userEntity.getLastName();
    }

    public void addMembership(Member member) {
        members.add(member);
    }
}
