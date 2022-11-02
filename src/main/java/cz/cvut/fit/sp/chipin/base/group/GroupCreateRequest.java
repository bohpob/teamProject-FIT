package cz.cvut.fit.sp.chipin.base.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupCreateRequest {
    private Long userId;
    private String name;
    private String currency;
}
