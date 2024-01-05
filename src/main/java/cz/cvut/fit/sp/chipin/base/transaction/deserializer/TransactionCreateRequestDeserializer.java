package cz.cvut.fit.sp.chipin.base.transaction.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cz.cvut.fit.sp.chipin.base.transaction.Category;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionRequest;

import java.util.List;

public class TransactionCreateRequestDeserializer extends TransactionRequestDeserializer<TransactionCreateTransactionRequest> {
    @Override
    protected TransactionCreateTransactionRequest createTransactionRequest(
            String name, float amount, String payerId, String category, String splitStrategy, List<MemberAbstractRequest> spenders, JsonNode rootNode) {
        return new TransactionCreateTransactionRequest(name, amount, payerId, Category.valueOf(category), TransactionType.valueOf(splitStrategy), spenders);
    }
}