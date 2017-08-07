package com.example.vishal.internshipseekerapp;


/**
 * Created by vishal on 26/7/17.
 */

class MessageWhereClause{
    private Integer message_id;

    public MessageWhereClause(Integer message_id) {
        this.message_id = message_id;
    }
}

class Set{
    private boolean is_read;
    public Set() {
        is_read = true;
    }
}

class MarkAsReadQueryArgs implements MessagesArgResources{
    private Set $set;
    private MessageWhereClause where;
    private String table;

    public MarkAsReadQueryArgs(String role, Integer messageId) {
        if(role.equals(studentRole))
            table = studentMessagesTable;
        else if(role.equals(employerRole))
            table = employerMessagesTable;

        $set = new Set();
        where = new MessageWhereClause(messageId);
    }
}

class MarkAsReadQuery {
    /*{
        "type" : "update",
        "args" : {
                    "table" : "student_messages_received",
                    "$set" : {"is_read" : true},
                    "where" : {"message_id" : 3}
                }
    }*/
    String type = "update";
    MarkAsReadQueryArgs args;

    public MarkAsReadQuery(String role, Integer messageId) {
        args = new MarkAsReadQueryArgs(role, messageId);
    }
}