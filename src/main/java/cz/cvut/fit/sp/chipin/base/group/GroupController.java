package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.membership.MemberRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public String create(@RequestBody GroupCreateDTO request) throws Exception {
        return groupService.create(request);
    }

    @PatchMapping("/{group_id}/join")
    public String addMember(@RequestBody MemberRequest request, @PathVariable Long group_id) throws Exception {
        return groupService.addMember(request.getId(), group_id);
    }


}
