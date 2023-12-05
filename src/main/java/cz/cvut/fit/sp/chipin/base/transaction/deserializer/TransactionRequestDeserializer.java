package cz.cvut.fit.sp.chipin.base.transaction.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.sp.chipin.base.transaction.spender.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionRequestDeserializer<T> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode rootNode = mapper.readTree(jp);

        String name = rootNode.get("name").asText();
        float amount = (float) rootNode.get("amount").asDouble();
        String payerId = rootNode.get("payerId").asText();
        String splitStrategy = rootNode.get("splitStrategy").asText();

        List<MemberAbstractRequest> spenders = new ArrayList<>();
        JsonNode spendersNode = rootNode.get("spenders");
        if (spendersNode.isArray()) {
            for (JsonNode spenderNode : spendersNode) {
                MemberAbstractRequest spender = mapSpender(splitStrategy, spenderNode, mapper);
                spenders.add(spender);
            }
        }

        return createTransactionRequest(name, amount, payerId, splitStrategy, spenders, rootNode);
    }

    protected abstract T createTransactionRequest(
            String name, float amount, String payerId, String splitStrategy, List<MemberAbstractRequest> spenders, JsonNode rootNode);

    private MemberAbstractRequest mapSpender(String splitStrategy, JsonNode spenderNode, ObjectMapper mapper)
            throws JsonProcessingException {
        return switch (splitStrategy) {
            case "EQUALLY" -> mapper.treeToValue(spenderNode, EqualTransactionMember.class);
            case "UNEQUALLY" -> mapper.treeToValue(spenderNode, UnequalTransactionMember.class);
            case "BY_PERCENTAGES" -> mapper.treeToValue(spenderNode, PercentTransactionMember.class);
            case "BY_SHARES" -> mapper.treeToValue(spenderNode, ShareTransactionMember.class);
            case "BY_ADJUSTMENT" -> mapper.treeToValue(spenderNode, AdjustmentTransactionMember.class);
            default -> throw new IllegalArgumentException("Unknown split strategy: " + splitStrategy);
        };
    }
}