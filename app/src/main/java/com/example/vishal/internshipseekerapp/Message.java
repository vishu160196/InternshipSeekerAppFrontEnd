package com.example.vishal.internshipseekerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.LogoutResponseListener;

import static com.example.vishal.internshipseekerapp.MainActivity.client;
import static com.example.vishal.internshipseekerapp.MainActivity.user;

public class ReplyMessage extends AppCompatActivity {

    String[] messageDetails;
    EditText receiverUsername;
    EditText subject;
    EditText body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle messageData = getIntent().getExtras();
        if (messageData == null) {
            // go to parent activity
            finish();
        }

        messageDetails = messageData.getStringArray(MainActivity.MessagesFragment.MESSAGE_DETAILS);

        setContentView(R.layout.activity_reply_message);
        receiverUsername = (EditText) findViewById(R.id.username);
        subject = (EditText) findViewById(R.id.subject);
        body = (EditText) findViewById(R.id.body);

        receiverUsername.setText(messageDetails[1]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View sendButton){
                if(isMessageValid())
                    sendMessage();
            }
        });
    }

    private void sendMessage() {
        String role = messageDetails[4];
        String subject = this.subject.getText().toString();
        String body = this.body.getText().toString();
        Integer receiverId = Integer.parseInt(messageDetails[5]);
        String receiver = messageDetails[1];

        /*String senderRole,String messageSubject, String messageBody, String sender,
                            Integer senderId, String receiver, Integer receiverId*/
        SendMessageQuery query = new SendMessageQuery(role, subject, body, user.getUsername(), user.getId(),
                receiver, receiverId);

        client.asRole(role) //throws an error if the current user does not have this role
                .useDataService()
                .setRequestBody(query)
                .expectResponseType(DeleteUpdateResponse.class)
                .enqueue(new Callback<DeleteUpdateResponse, HasuraException>() {
            @Override
            public void onSuccess(DeleteUpdateResponse response) {
                //Handle response
                if(response.getAffected_rows() == 1)
                    Toast.makeText(getApplicationContext(), getString(R.string.message_sent),
                            Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unresolvable),
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HasuraException e) {
                //Handle error
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isMessageValid() {
        receiverUsername.setError(null);
        subject.setError(null);
        body.setError(null);

        String userName = receiverUsername.getText().toString();
        String messageSubject = subject.getText().toString();
        String messageBody = body.getText().toString();

        View focusView = null;

        if(userName.isEmpty()){
            focusView = receiverUsername;
            receiverUsername.setError(getString(R.string.error_field_required));
        }

        else if(messageSubject.isEmpty()){
            focusView = subject;
            subject.setError(getString(R.string.error_field_required));
        }

        else if(messageBody.isEmpty()){
            focusView = body;
            body.setError(getString(R.string.error_field_required));
        }

        if(focusView != null){
            focusView.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // TODO : Handle search button

        // noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        user.logout(new LogoutResponseListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), getString(R.string.logout_success),
                        Toast.LENGTH_SHORT).show();
                Log.d("serverMessage", message);

                // start LoginActivity
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
            }

            @Override
            public void onFailure(HasuraException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
