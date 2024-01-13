package cz.cvut.fit.sp.chipin.base.group.swagger;

public class GroupSwaggerExamples {
    public static final String EXAMPLE_CREATE_GROUP = "{\"id\":1,\"name\":\"Adventure Time\",\"currency\":\"USD\"}";

    public static final String EXAMPLE_READ_GROUP = "{\"name\":\"PIVO\",\"currency\":\"EUR\",\"nextPayerId\":\"077966" +
            "c2-9fe7-4073-b1e6-aeb3879db493\",\"payerStrategy\":\"LOWEST_BALANCE\",\"checkNextPayer\":false,\"members" +
            "\":[{\"id\":\"077966c2-9fe7-4073-b1e6-aeb3879db493\",\"name\":\"c\",\"role\":\"USER\"},{\"id\":\"086dda9" +
            "e-c40c-4c53-aac9-14ae5ba3bef2\",\"name\":\"a\",\"role\":\"ADMIN\"},{\"id\":\"189c63b8-010c-4ab1-85e5-93b" +
            "f9da4a258\",\"name\":\"b\",\"role\":\"USER\"},{\"id\":\"e2291b4d-0db0-418c-b9b7-f530f4a74484\",\"name\":" +
            "\"d\",\"role\":\"USER\"}],\"transactions\":[{\"id\":4,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":nul" +
            "l,\"payerId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"TICKETS\",\"da" +
            "teTime\":\"2024-01-01 18:10\"},{\"id\":6,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":null,\"payerId\"" +
            ":\"189c63b8-010c-4ab1-85e5-93bf9da4a258\",\"payerName\":\"b\",\"category\":\"TICKETS\",\"dateTime\":\"20" +
            "24-01-01 18:10\"},{\"id\":5,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":null,\"payerId\":\"086dda9e-c" +
            "40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-01 18:10\"" +
            "},{\"id\":1,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":null,\"payerId\":\"086dda9e-c40c-4c53-aac9-14" +
            "ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"NO_CATEGORY\",\"dateTime\":\"2024-01-01 18:09\"},{\"id\"" +
            ":3,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":null,\"payerId\":\"a1a81cae-2c73-41ff-b96f-8d4e81b0058" +
            "b\",\"payerName\":\"j\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-01 18:10\"},{\"id\":7,\"name\":\"Tr" +
            "\",\"amount\":3322.0,\"currency\":null,\"payerId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName" +
            "\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-05 16:59\"},{\"id\":8,\"name\":\"Tr\",\"amount\":3" +
            "322.0,\"currency\":null,\"payerId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"categ" +
            "ory\":\"FOOD\",\"dateTime\":\"2024-01-05 17:01\"},{\"id\":9,\"name\":\"Tr\",\"amount\":3322.0,\"currency" +
            "\":null,\"payerId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\"," +
            "\"dateTime\":\"2024-01-05 17:04\"},{\"id\":2,\"name\":\"Tr\",\"amount\":3322.0,\"currency\":null,\"payer" +
            "Id\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2" +
            "022-11-23 13:00\"},{\"id\":10,\"name\":\"Tr-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":\"086" +
            "dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-12 " +
            "23:04\"},{\"id\":11,\"name\":\"Tr-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":\"086dda9e-c40c" +
            "-4c53-aac9-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-12 23:06\"},{" +
            "\"id\":12,\"name\":\"Tr-1\",\"amount\":3322.0,\"currency\":\"EUR\",\"payerId\":\"086dda9e-c40c-4c53-aac9" +
            "-14ae5ba3bef2\",\"payerName\":\"a\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-12 23:31\"}],\"debts\"" +
            ":[{\"lenderId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"lenderName\":\"a\",\"borrowerId\":\"189c63b8-" +
            "010c-4ab1-85e5-93bf9da4a258\",\"borrowerName\":\"b\",\"debt\":13288.0},{\"lenderId\":\"086dda9e-c40c-4c5" +
            "3-aac9-14ae5ba3bef2\",\"lenderName\":\"a\",\"borrowerId\":\"077966c2-9fe7-4073-b1e6-aeb3879db493\",\"bor" +
            "rowerName\":\"c\",\"debt\":13288.0},{\"lenderId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"lenderName" +
            "\":\"a\",\"borrowerId\":\"e2291b4d-0db0-418c-b9b7-f530f4a74484\",\"borrowerName\":\"d\",\"debt\":13288.0" +
            "}],\"logs\":[{\"action\":\"Created the group\",\"dateTime\":\"2024-01-12 23:01\",\"userId\":\"086dda9e-c" +
            "40c-4c53-aac9-14ae5ba3bef2\",\"userName\":\"a\"},{\"action\":\"made a payment: 3322.0\",\"dateTime\":\"2" +
            "024-01-12 23:04\",\"userId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"userName\":\"a\"},{\"action\":\"" +
            "made a payment: 3322.0\",\"dateTime\":\"2024-01-12 23:06\",\"userId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3" +
            "bef2\",\"userName\":\"a\"},{\"action\":\"made a payment: 3322.0\",\"dateTime\":\"2024-01-12 23:31\",\"us" +
            "erId\":\"086dda9e-c40c-4c53-aac9-14ae5ba3bef2\",\"userName\":\"a\"}]}";

    public static final String EXAMPLE_READ_HEX_CODE = "8346a4";

    public static final String EXAMPLE_JOIN_GROUP = "User joined.";

    public static final String EXAMPLE_READ_NEXT_PAYER_ID = "077966c2-9fe7-4073-b1e6-aeb3879db493";

    public static final String EXAMPLE_UPDATE_NEXT_PAYER = "Payer strategy = LOWEST_BALANCE, checking = false";

    public static final String EXAMPLE_CREATE_TRANSACTION = "{\"id\":13,\"name\":\"Tr-1\",\"amount\":3322.0,\"currenc" +
            "y\":\"EUR\",\"category\":\"FOOD\",\"dateTime\":\"2024-01-13 16:57\",\"payer\":{\"id\":\"086dda9e-c40c-4c" +
            "53-aac9-14ae5ba3bef2\",\"name\":\"a\"},\"amounts\":[{\"id\":\"189c63b8-010c-4ab1-85e5-93bf9da4a258\",\"n" +
            "ame\":\"b\",\"amount\":1107.33},{\"id\":\"077966c2-9fe7-4073-b1e6-aeb3879db493\",\"name\":\"c\",\"amount" +
            "\":1107.33},{\"id\":\"e2291b4d-0db0-418c-b9b7-f530f4a74484\",\"name\":\"d\",\"amount\":1107.33}]}";

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
