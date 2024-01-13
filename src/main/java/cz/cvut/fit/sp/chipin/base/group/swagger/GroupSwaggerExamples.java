package cz.cvut.fit.sp.chipin.base.group.swagger;

public class GroupSwaggerExamples {
    public static final String EXAMPLE_GROUP_UPDATE_NAME = "{\"name\":\"New name\",\"currency\":\"USD\"}";

    public static final String EXAMPLE_GROUP_LOGS = "{\"logs\":[" +
            "{\"action\":\"Transaction-1-1\",\"dateTime\":\"2022-11-23 13:00\",\"userId\":\"ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"userName\":\"Honza\"}," +
            "{\"action\":\"joined the group\",\"dateTime\":\"2022-11-11 13:00\",\"userId\":\"94c68919-8adc-46fc-ae47-71ea197a1b48\",\"userName\":\"Jan\"}," +
            "{\"action\":\"joined the group\",\"dateTime\":\"2022-11-10 13:00\",\"userId\":\"ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"userName\":\"Honza\"}" +
            "]}";

    public static final String EXAMPLE_UPDATE_TRANSACTION = "{\"id\":1,\"name\":\"Tr-999\",\"amount\":\"3322\",\"" +
            "currency\":\"EUR\",\"dateTime\":\"2022-11-23 13:00\",\"payer\":{\"id\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2" +
            "\",\"name\":\"a\"},\"amounts\":[{\"id\":\"189c63b8-010c-4ab1-85e5-93bf9da4a258\",\"name\":\"b\",\"amount\":1107.33}" +
            ",{\"id\":\"077966c2-9fe7-4073-b1e6-aeb3879db493\",\"name\":\"c\",\"amount\":1107.33},{\"id\":\"" +
            "e2291b4d-0db0-418c-b9b7-f530f4a74484\",\"name\":\"d\",\"amount\":1107.33}]}";

    public static final String EXAMPLE_TRANSACTION_WITH_FILTERS = "{\"transactions\":[" +
            "{\"id\":1,\"name\":\"Transaction-1-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":\"" +
            "ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"payerName\":\"Honza\",\"dateTime\":\"2024-01-01 18:10\",\"category\":\"TICKETS\"}," +
            "{\"id\":2,\"name\":\"Transaction-2-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":" +
            "\"189c63b8-010c-4ab1-85e5-93bf9da4a258\",\"payerName\":\"b\",\"dateTime\":\"2024-01-01 18:10\",\"category\":\"TICKETS\"}," +
            "{\"id\":5,\"name\":\"Transaction-3-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":" +
            "086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"dateTime\":\"2024-01-01 18:10\",\"category\":\"FOOD\"}" +
            "]}";

    public static final String EXAMPLE_ALL_TRANSACTIONS = "{\"transactions\":[" +
            "{\"id\":1,\"name\":\"Transaction-1-1\",\"amount\":1000,\"currency\":\"EUR\",\"payerId\":\"" +
            "ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"payerName\":\"Honza\",\"dateTime\":\"2022-11-23 13:00\",\"category\":\"FOOD\"}," +
            "{\"id\":2,\"name\":\"Transaction-1-1\",\"amount\":1000,\"currency\":\"EUR\",\"payerId\":\"" +
            "ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"payerName\":\"Honza\",\"dateTime\":\"2022-11-23 13:00\",\"category\":\"FOOD\"}," +
            "{\"id\":3,\"name\":\"Transaction-1-1\",\"amount\":1000,\"currency\":\"EUR\",\"payerId\":\"" +
            "ba0a07c8-7f66-4cee-9f81-2d3a985d137d\",\"payerName\":\"Honza\",\"dateTime\":\"2022-11-23 13:00\",\"category\":\"FOOD\"}" +
            "]}";

    public static final String EXAMPLE_TRANSACTION_JSON = "{\"name\":\"Tr-1\",\"amount\":3322,\"currency\":\"EUR\"," +
            "\"category\":\"FOOD\",\"dateTime\":\"2024-01-12 23:04\",\"payer\":{\"id\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\"" +
            ",\"name\":\"a\"},\"amounts\":[{\"id\":\"077966c2-9fe7-4073-b1e6-aeb3879db493\",\"name\":\"c\",\"" +
            "amount\":1107.33},{\"id\":\"189c63b8-010c-4ab1-85e5-93bf9da4a258\",\"name\":\"b\",\"amount\":1107.33}," +
            "{\"id\":\"e2291b4d-0db0-418c-b9b7-f530f4a74484\",\"name\":\"d\",\"amount\":1107.33}]}";

}
