package cz.cvut.fit.sp.chipin.base.debt;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class DebtController {

    private DebtService debtService;

    @DeleteMapping("/{group_id}/debts/{debt_id}")
    public void delete(@PathVariable Long group_id, @PathVariable Long debt_id) throws Exception {
        try {
            //debtService.delete(transaction_id, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
