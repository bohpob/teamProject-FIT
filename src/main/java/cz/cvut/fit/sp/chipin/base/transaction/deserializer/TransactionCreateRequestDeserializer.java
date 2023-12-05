package cz.cvut.fit.sp.chipin.base.transaction.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionCreateRequest;

import java.util.List;

public class TransactionCreateRequestDeserializer extends TransactionRequestDeserializer<TransactionCreateRequest> {
    @Override
    protected TransactionCreateRequest createTransactionRequest(
            String name, float amount, String payerId, String splitStrategy, List<MemberAbstractRequest> spenders, JsonNode rootNode) {
        return new TransactionCreateRequest(name, amount, payerId, TransactionType.valueOf(splitStrategy), spenders);
    }
}