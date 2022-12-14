package cz.cvut.fit.sp.chipin.base.membership;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberDTO {
    private Long group_id;
    private String role;
    private Float paid;
    private Float spent;
    private Float balance;
}
