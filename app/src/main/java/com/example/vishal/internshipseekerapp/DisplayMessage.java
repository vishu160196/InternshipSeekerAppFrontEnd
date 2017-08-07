package com.example.vishal.internshipseekerapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.LogoutResponseListener;

import static com.example.vishal.internshipseekerapp.MainActivity.client;
import static com.example.vishal.internshipseekerapp.MainActivity.user;


public class DisplayMessage extends AppCompatActivity implements View.OnClickListener {

    private String role;
    private Integer messageId;
    String[] messageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle messageData = getIntent().getExtras();
        if (messageData == null) {
            // go to parent activity
            finish();
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_display_message);

        messageDetails = messageData.getStringArray(MainActivity.MessagesFragment.MESSAGE_DETAILS);

        role = messageDetails[4];
        messageId = Integer.parseInt(messageDetails[0]);

        TextView from = (TextView) findViewById(R.id.from);
        TextView subject = (TextView) findViewById(R.id.subject);
        TextView body = (TextView) findViewById(R.id.message_body);

        from.setText(messageDetails[1]);
        subject.setText(messageDetails[2]);
        body.setText(messageDetails[3]);

        Button markAsRead = (Button) findViewById(R.id.mark_as_read);
        Button delete = (Button) findViewById(R.id.delete_message);
        Button reply = (Button) findViewById(R.id.reply);

        markAsRead.setOnClickListener(this);
        delete.setOnClickListener(this);
        reply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.mark_as_read:
                MarkAsReadQuery markAsReadQuery = new MarkAsReadQuery(role, messageId);
                client.asRole(role)
                        .useDataService().setRequestBody(markAsReadQuery)
                        .expectResponseType(DeleteUpdateResponse.class)
                        .enqueue(new Callback<DeleteUpdateResponse, HasuraException>() {
                            @Override
                            public void onSuccess(DeleteUpdateResponse response) {
                                if(response.getAffected_rows() == 1)
                                    Toast.makeText(getApplicationContext(), getString(R.string.marked_read),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), getString(R.string.snap),
                                            Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(HasuraException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                break;

            case R.id.delete_message:
                DeleteMessageQuery deleteMessageQuery = new DeleteMessageQuery(role, messageId);
                client.asRole(role)
                        .useDataService().setRequestBody(deleteMessageQuery)
                        .expectResponseType(DeleteUpdateResponse.class)
                        .enqueue(new Callback<DeleteUpdateResponse, HasuraException>() {
                            @Override
                            public void onSuccess(DeleteUpdateResponse response) {
                                if(response.getAffected_rows() == 1)
                                    Toast.makeText(getApplicationContext(), getString(R.string.message_deleted),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), getString(R.string.snap),
                                            Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(HasuraException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                break;

            case R.id.reply:
                Intent reply = new Intent(getApplicationContext(), Message.class);
                reply.putExtra(MainActivity.MessagesFragment.MESSAGE_DETAILS, messageDetails);
                startActivity(reply);
                break;
        }
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
