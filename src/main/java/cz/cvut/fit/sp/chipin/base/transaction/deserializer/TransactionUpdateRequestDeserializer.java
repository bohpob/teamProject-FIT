package cz.cvut.fit.sp.chipin.base.transaction.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;

import java.util.List;

public class TransactionUpdateRequestDeserializer extends TransactionRequestDeserializer<TransactionUpdateRequest> {
    @Override
    protected TransactionUpdateRequest createTransactionRequest(
            String name, float amount, String currency, String payerId, String splitStrategy, List<MemberAbstractRequest> spenders, JsonNode rootNode) {
        String date = rootNode.get("date").asText();
        return new TransactionUpdateRequest(name, amount, currency, date, payerId, TransactionType.valueOf(splitStrategy), spenders);
    }
}
