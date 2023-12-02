package cz.cvut.fit.sp.chipin.base.usergroup;

import cz.cvut.fit.sp.chipin.base.log.LogDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findUserGroupByHexCode(String hexCode);

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    class GroupCreateGroupResponse {
        @NotBlank
        private String name;
        @NotNull
        private Currency currency;
        @NotNull
        private List<LogDTO> logs;
    }
}
