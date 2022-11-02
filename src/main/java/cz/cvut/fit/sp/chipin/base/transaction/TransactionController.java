package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/{group_id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionDTO transactionDTO, @PathVariable Long group_id) throws Exception {
        try {
            return transactionService.create(transactionDTO, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/{group_id}/transactions/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionDTO> read(@PathVariable Long group_id, @PathVariable Long transaction_id) throws Exception {
        try {
            return transactionService.read(transaction_id, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/{group_id}/transactions/{transaction_id}")
    public void delete(@PathVariable Long group_id, @PathVariable Long transaction_id) throws Exception {
        try {
            transactionService.delete(transaction_id, group_id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
