package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.membership.MemberRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public String create(@Valid @RequestBody GroupCreateRequest request) throws Exception {
        return groupService.create(request);
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<GroupResponse> read(@PathVariable Long group_id) throws Exception {
        try {
            return groupService.read(group_id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PatchMapping("/{group_id}/join")
    public String addMember(@Valid @RequestBody MemberRequest request, @PathVariable Long group_id) throws Exception {
        return groupService.addMember(request.getId(), group_id);
    }


}
