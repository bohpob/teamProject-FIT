package cz.cvut.fit.sp.chipin.base.notification.swagger;

public class NotificationSwaggerExamples {
    public static final String EXAMPLE_ALL_NOTIFICATION_JSON = "{\"count\":4,\"notifications\":[{\"id\":4,\"content\":{\"id\":4," +
            "\"title\":\"Mark added the transaction 'Tr-2' in 'Group-2'.\",\"text\":\"You get back $1\"},\"dateTime\":" +
            "\"2022-12-23 13:00\",\"read\":false},{\"id\":3,\"content\":{\"id\":3,\"title\":\"Jon added the transaction" +
            " 'Tr' in 'Group-1'.\",\"text\":\"You owe $1107.33\"},\"dateTime\":\"2022-11-23 13:00\",\"read\":false}," +
            "{\"id\":2,\"content\":{\"id\":2,\"title\":\"You joined the group \\\"Group-2\\\".\",\"text\":\"\"},\"dateTime" +
            "\":\"2022-02-23 13:00\",\"read\":true},{\"id\":1,\"content\":{\"id\":1,\"title\":\"You joined the group " +
            "\\\"Group-2\\\".\",\"text\":\"\"},\"dateTime\":\"2022-01-23 13:00\",\"read\":true}]}";

    public static final String EXAMPLE_UNREAD_NOTIFICATION_JSON = "{\"count\":2,\"notifications\":[{\"id\":3,\"content" +
            "\":{\"id\":3,\"title\":\"Jon added the transaction 'Tr' in 'Group-1'.\",\"text\":\"You owe $1107.33\"}," +
            "\"dateTime\":\"2022-11-23 13:00\",\"read\":false},{\"id\":4,\"content\":{\"id\":4,\"title\":\"Mark added " +
            "the transaction 'Tr-2' in 'Group-2'.\",\"text\":\"You get back $1\"},\"dateTime\":\"2022-12-23 13:00\",\"read\":false}]}";

}
