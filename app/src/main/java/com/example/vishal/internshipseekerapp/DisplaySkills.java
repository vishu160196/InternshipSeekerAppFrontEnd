package com.example.vishal.internshipseekerapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.LogoutResponseListener;

import static com.example.vishal.internshipseekerapp.MainActivity.client;
import static com.example.vishal.internshipseekerapp.MainActivity.user;

public class DisplaySkills extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] details;
        String name = null;
        Integer id = null;
        String role = null;

        try{
            details = getIntent().getExtras().getStringArray(MainActivity.HomeFragment.SKILL_DETAILS);
            name = details[0];
            role = details[2];
            id = Integer.parseInt(details[1]);
        }catch(NullPointerException e){
            Toast.makeText(getApplicationContext(), getString(R.string.error_unresolvable), Toast.LENGTH_LONG);
            finish();
        }

        setContentView(R.layout.activity_display_skills);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.heading_skills)).setText(getTemplate(role, name));

        display(role, id);
    }

    private String getTemplate(String role, String name){
        if(role.equalsIgnoreCase(getString(R.string.student_role)))
            return name + " seeks the following skills";
        else if(role.equalsIgnoreCase(getString(R.string.employer_role)))
            return name + " has the following skills";

        return null;
    }

    private void display(String role, Integer id){

        SkillQuery query = new SkillQuery(role, id, true);

        final List<String> skillList = new ArrayList<>();
        client.asRole(role)
                .useDataService()
                .setRequestBody(query)
                .expectResponseTypeArrayOf(SkillResponse.class)
                .enqueue(new Callback<List<SkillResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(List<SkillResponse> skillResponses) {
                        for(SkillResponse s : skillResponses)
                            skillList.add(s.getSkill());

                        display(skillList);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void display(List<String> skillList){
        ((ListView)findViewById(R.id.skills)).setAdapter(new ArrayAdapter(this, R.layout.skill_holder, skillList));
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
