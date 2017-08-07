package com.example.vishal.internshipseekerapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // student button
        Button student = (Button) findViewById(R.id.student_role);

        // employer button
        Button employer = (Button) findViewById(R.id.employer_role);

        // register onclick listeners
        student.setOnClickListener(this);
        employer.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.student_role:
                registerStudent();
                break;

            case R.id.employer_role:
                registerEmployer();
                break;
        }
    }

    private void registerEmployer() {

        Intent registerEmployer = new Intent(this, EmployerRegistration.class);

        startActivity(registerEmployer);
    }

    private void registerStudent() {

        Intent registerStudent = new Intent(this, StudentRegistration.class);

        startActivity(registerStudent);
    }
}
