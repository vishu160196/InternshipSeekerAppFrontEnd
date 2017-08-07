package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 26/7/17.
 */

class GetMessagesResponse {
    /*{"message_subject", "message_body", "is_read", "message_id", "time_of_receipt",
            "sender_username"}*/

    private String message_subject;
    private String message_body;
    private boolean is_read;
    private Integer message_id;
    private String time_of_receipt;
    private String sender_username;
    private Integer sender_id;

    public Integer getSender_id() {
        return sender_id;
    }

    public String getSender_username() {
        return sender_username;
    }

    public String getMessage_subject() {
        return message_subject;
    }

    public String getMessage_body() {
        return message_body;
    }

    public boolean is_read() {
        return is_read;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public String getTime_of_receipt() {
        return time_of_receipt;
    }
}
