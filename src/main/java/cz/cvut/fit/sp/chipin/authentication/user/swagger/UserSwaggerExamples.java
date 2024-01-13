package cz.cvut.fit.sp.chipin.authentication.user.swagger;

public class UserSwaggerExamples {

    public static final String EXAMPLE_USER_RESPONSE_JSON = "{\"id\": \"ba0a07c8-7f66-4cee-9f81-2d3a985d137d\", " +
            "\"name\": \"Honza\", \"email\": \"honza@cvut.cz\"}";

    public static final String EXAMPLE_GROUP_RESPONSE_JSON = "{\"id\": 1, \"name\": \"Group-1\", " +
            "\"members\": [{\"id\": \"ba0a07c8-7f66-4cee-9f81-2d3a985d137d\", \"name\": \"Honza\", " +
            "\"role\": \"ADMIN\"}, {\"id\": \"94c68919-8adc-46fc-ae47-71ea197a1b48\", " +
            "\"name\": \"Jan\", \"role\": \"USER\"}]}";

    public static final String EXAMPLE_TRANSACTION_RESPONSE_JSON = "{\"transactions\": [" +
            "{\"id\": 1, \"name\": \"Transaction-1\", \"amount\": 333, \"payerId\": " +
            "\"94c68919-8adc-46fc-ae47-71ea197a1b48\", \"payerName\": \"Jan\", " +
            "\"dateTime\": \"2023-11-22 15:30\", \"category\": \"FOOD\"}, " +
            "{\"id\": 2, \"name\": \"Transaction-2\", \"amount\": 222, \"payerId\": " +
            "\"94c68919-8adc-46fc-ae47-71ea197a1b48\", \"payerName\": \"Jan\", " +
            "\"dateTime\": \"2023-11-22 15:30\", \"category\": \"DRINKS\"}, " +
            "{\"id\": 3,\"name\": \"Transaction-3\", \"amount\": 111, \"payerId\": " +
            "\"94c68919-8adc-46fc-ae47-71ea197a1b48\", \"payerName\": \"Jan\", " +
            "\"dateTime\": \"2023-11-15 15:30\", \"category\": \"ACCOMODATION\"}]}\n";

    public static final String EXAMPLE_MEMBERSHIP_RESPONSE_JSON = "[{\"groupId\": 1, \"role\": \"ADMIN\", " +
            "\"paid\": 100, \"spent\": 50, \"balance\": 50}, " +
            "{\"groupId\": 2, \"role\": \"USER\", \"paid\": 200, \"spent\": 100, \"balance\": 100}]";
}