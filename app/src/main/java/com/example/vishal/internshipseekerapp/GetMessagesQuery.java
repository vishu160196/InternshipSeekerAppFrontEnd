package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 26/7/17.
 */

interface MessagesArgResources{
    String studentMessagesTable = "student_messages_received";
    String employerMessagesTable = "employer_messages_received";
    String studentRole = "student";
    String employerRole = "employer";

    String[] messagesTableCols = {"message_subject", "message_body", "is_read", "message_id", "time_of_receipt",
            "sender_username", "sender_id"};
}


class MessagesWhereClause{
    Integer id;

    public MessagesWhereClause(Integer id){
        this.id = id;
    }
}



class MessagesArgs implements MessagesArgResources{
    private String[] columns;
    private MessagesWhereClause where;
    private String table;


    public MessagesArgs(String role, Integer id) {
        if(role.equals(studentRole))
            table = studentMessagesTable;
        else if(role.equals(employerRole))
            table = employerMessagesTable;

        columns = messagesTableCols;
        where = new MessagesWhereClause(id);
    }
}



class GetMessagesQuery{

    /*{

	"type" :"select",
	"args":{
		"table":"student_messages_received",
		"columns":["message_subject", "message_body", "is_read", "message_id", "time_of_receipt", "sender_username"],
		"where":{
			"student_id":3
		}
	}
}*/

    private final String type = "select";
    private MessagesArgs args;

    public GetMessagesQuery(String role, Integer id) {
        args = new MessagesArgs(role, id);
    }
}



