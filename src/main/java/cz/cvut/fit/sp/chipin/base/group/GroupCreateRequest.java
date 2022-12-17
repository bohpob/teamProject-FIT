package cz.cvut.fit.sp.chipin.base.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class GroupCreateRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String name;
    @NotBlank
    private String currency;
}