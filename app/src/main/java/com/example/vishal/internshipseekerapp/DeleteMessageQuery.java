package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 26/7/17.
 */

class DeleteMessageArgs implements MessagesArgResources{
    private String table;
    private MessageWhereClause where;

    public DeleteMessageArgs(String role, Integer messageId) {
        if(role.equals(studentRole))
            table = studentMessagesTable;
        else if(role.equals(employerRole))
            table = employerMessagesTable;

        where = new MessageWhereClause(messageId);
    }
}

class DeleteMessageQuery {

    private final String type = "delete";
    private DeleteMessageArgs args;

    public DeleteMessageQuery(String role, Integer messageId) {
        args = new DeleteMessageArgs(role, messageId);
    }
}
