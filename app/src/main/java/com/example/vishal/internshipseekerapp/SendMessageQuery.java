package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 27/7/17.
 */

class InsertObject{
    /*{
        			"message_subject":"test",
        			"message_body":"test message",
        			"id" : 2,
        			"username" : "employer1",
        			"sender_username":"student1",
        			"sender_id" :3
        		}*/
    private String message_subject;
    private String message_body;
    private Integer id;
    private String username;
    private String sender_username;
    private Integer sender_id;

    public InsertObject(String message_subject, String message_body, Integer id, String username,
                        String sender_username, Integer sender_id) {
        this.message_subject = message_subject;
        this.message_body = message_body;
        this.id = id;
        this.username = username;
        this.sender_username = sender_username;
        this.sender_id = sender_id;
    }
}

class InsertRequestArgs implements MessagesArgResources{
    private String table;
    private InsertObject[] objects = new InsertObject[1];

    public InsertRequestArgs(String senderRole, String messageSubject, String messageBody, String sender,
                             Integer senderId, String receiver, Integer receiverId){
        if(senderRole.equals(studentRole))
            table = employerMessagesTable;
        else if(senderRole.equals(employerRole))
            table = studentMessagesTable;

        objects[0] = new InsertObject(messageSubject, messageBody, receiverId, receiver, sender, senderId);
    }
}

class SendMessageQuery {
    private final String type = "insert";
    private InsertRequestArgs args;

    public SendMessageQuery(String senderRole,String messageSubject, String messageBody, String sender,
                            Integer senderId, String receiver, Integer receiverId){
        args = new InsertRequestArgs(senderRole, messageSubject, messageBody, sender, senderId, receiver,
                receiverId);
    }
}
