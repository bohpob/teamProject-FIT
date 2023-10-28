package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user_account")
@NoArgsConstructor
@Getter
@Setter
public class UserAccount implements UserDetails {

    @Id
    private String id;

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

    @NotBlank
    @Column(name = "name")
    private String name;
    @Email
    @Column(name = "email", nullable = false)
    private String email;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountRole userAccountRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    public UserAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserAccount(String name, String email, String password, UserAccountRole userAccountRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userAccountRole = userAccountRole;
    }

    @Override
    public String toString() {
        return getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userAccountRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addMembership(Member member) {
        members.add(member);
    }
}
