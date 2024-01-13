package cz.cvut.fit.sp.chipin.base.transaction.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionCreateTransactionRequest;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionUpdateRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {
    @Bean
    public static ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TransactionCreateTransactionRequest.class, new TransactionCreateRequestDeserializer());
        module.addDeserializer(TransactionUpdateRequest.class, new TransactionUpdateRequestDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
