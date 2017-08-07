package com.example.vishal.internshipseekerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class StudentRegistration extends AppCompatActivity implements View.OnClickListener{

    boolean[] checkField = new boolean[12];
    String[] field = {"computer vision", "content writing", "data mining", "game development", "electrical/electronics",
            "image processing", "marketing", "mechanical engineering", "mobile app dev", "programming", "software dev", "web dev"};
    private final int NUM_FIELDS = field.length;
    private CheckBox[] fieldCheckbox= new CheckBox[12];

    private EditText signUpEmail;
    private EditText signUpName;
    private EditText signUpUsername;
    private EditText signUpPassword;
    private EditText signUpConfirmPassword;
    private EditText signUpInstitution;
    private Button signUpSelectDob;
    private EditText signUpDob;
    private EditText signUpPathToCV;
    private Button signUpButton;
    private LinearLayout skillListDisplay;
    private EditText percentage;
    private EditText yearOfAdmission;
    private EditText yearOfPassing;

    private Button done;
    private Button dob;
    private  RadioButton signUpGenderSelectMale;

    // an ArrayList to contain all the checkBox ids
    private List<SkillState> skillStates = new ArrayList<>();
    // number of skills will be used to traverse the ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        fieldCheckbox[0] = (CheckBox) findViewById(R.id.field0);
        fieldCheckbox[1] = (CheckBox) findViewById(R.id.field1);
        fieldCheckbox[2] = (CheckBox) findViewById(R.id.field2);
        fieldCheckbox[3] = (CheckBox) findViewById(R.id.field3);
        fieldCheckbox[4] = (CheckBox) findViewById(R.id.field4);
        fieldCheckbox[5] = (CheckBox) findViewById(R.id.field5);
        fieldCheckbox[6] = (CheckBox) findViewById(R.id.field6);
        fieldCheckbox[7] = (CheckBox) findViewById(R.id.field7);
        fieldCheckbox[8] = (CheckBox) findViewById(R.id.field8);
        fieldCheckbox[9] = (CheckBox) findViewById(R.id.field9);
        fieldCheckbox[10] = (CheckBox) findViewById(R.id.field10);
        fieldCheckbox[11] = (CheckBox) findViewById(R.id.field11);

        signUpEmail = (EditText) findViewById(R.id.sign_up_email);
        signUpName = (EditText) findViewById(R.id.sign_up_name);
        signUpUsername = (EditText) findViewById(R.id.sign_up_usernamename);
        signUpPassword = (EditText) findViewById(R.id.sign_up_password);
        signUpConfirmPassword = (EditText) findViewById(R.id.sign_up_confirm_password);
        signUpInstitution = (EditText) findViewById(R.id.sign_up_institution);
        signUpSelectDob =(Button) findViewById(R.id.signup_dob_select);
        signUpButton = (Button) findViewById(R.id.sign_up);
        skillListDisplay = (LinearLayout) findViewById(R.id.skill_list);
        signUpDob = (EditText) findViewById(R.id.signup_dob);
        signUpPathToCV = (EditText) findViewById(R.id.signup_path_to_cv);
        percentage = (EditText) findViewById(R.id.sign_up_percentage);
        yearOfAdmission = (EditText) findViewById(R.id.year_of_admission);
        yearOfPassing = (EditText) findViewById(R.id.year_of_passing);

        signUpGenderSelectMale = (RadioButton) findViewById(R.id.select_male);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);




        // register onclick listener for DONE button
        done = (Button) findViewById(R.id.field_select_done);
        done.setOnClickListener(this);

        // register onclick listener for select dob
        dob = (Button) findViewById(R.id.signup_dob_select);
        dob.setOnClickListener(this);

        signUpButton.setOnClickListener(this);
    }



    public void onClick(View v){
        switch(v.getId()){


            case R.id.field_select_done:
                displayRelevantSkills();
                break;

            case R.id.signup_dob_select:
                selectDate();
                break;

            case R.id.sign_up:
                signUpUser();
                break;

        }
    }

    private void selectDate() {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1995);
        int mYear = c.get(Calendar.YEAR); // year 1995
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(StudentRegistration.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        // format YYYY-MM-DD
                        String setMonth, setDay;

                        if(monthOfYear < 9)
                            setMonth = "0" + (monthOfYear + 1);
                        else
                            setMonth = "" + (monthOfYear + 1);

                        if(dayOfMonth < 10)
                            setDay = "0" + dayOfMonth;
                        else
                            setDay = "" + dayOfMonth;

                        TextView dateDisplay = (TextView) findViewById(R.id.signup_dob);
                        dateDisplay.setText(year + "-" + setMonth + "-" + setDay);
                        dateDisplay.setVisibility(View.VISIBLE);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void signUpUser() {
        setSkillCheckboxStates();
        SignupForm form = createSignUpObject();

        if (form != null) {
            Log.d("SignupObject", "size is " + form.getSkillList().size());
            // send object to signup endpoint

            final String SKILL_FIELD_URL = "https://register.outfight74.hasura-app.io/";

            // set request options for all requests
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(SKILL_FIELD_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            // create retrofit adapter
            Retrofit retrofit =
                    builder
                            .build();

            SignUp signUpClient = retrofit.create(SignUp.class);

            Call<SignupForm> call = signUpClient.createNewUser(form);
            call.enqueue(new Callback<SignupForm>() {
                @Override
                public void onResponse(Call<SignupForm> call, Response<SignupForm> response) {
                    if(response.body().getMessage().equalsIgnoreCase(getString(R.string.success_message))){
                        // start LoginActivity
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(getApplicationContext(), getString(R.string.login_to_continue), Toast.LENGTH_LONG).show();
                        startActivity(login);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SignupForm> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_sending_request), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private SignupForm createSignUpObject() {
        if(isFormValid()) {
            String gender;
            if(signUpGenderSelectMale.isChecked())
                gender = "male";
            else
                gender = "female";

            // add selected skills to a List
            List<String> skillList = new ArrayList<String>();
            int i = 0;
            int size = skillStates.size();
            while(i < size)
            {
                SkillState skill = skillStates.get(i);
                if(skill.isChecked())
                    skillList.add(skill.getSkillName());
                i++;
            }

            // remove duplicates from skillList
            size = skillList.size();
            i = 0;
            while(i < size){

                String check = skillList.get(i);

                int j = size - 1;

                while(j > i){
                    if(skillList.get(j).equals(check))
                        skillList.remove(j);
                    j--;
                }
                size = skillList.size();
                i++;
            }

            return new SignupForm(signUpEmail.getText().toString(), signUpName.getText().toString(), signUpUsername.getText().toString(),
                    signUpPassword.getText().toString(), signUpInstitution.getText().toString(), signUpDob.getText().toString(),
                    signUpPathToCV.getText().toString(), gender, skillList, "student", Float.parseFloat(percentage.getText().toString()),
                    Integer.parseInt(yearOfPassing.getText().toString()), Integer.parseInt(yearOfAdmission.getText().toString()));
        }

        else
            return null;
    }

    private boolean isFormValid() {

        // Reset errors.
        signUpEmail.setError(null);
        signUpName.setError(null);
        signUpUsername.setError(null);
        signUpPassword.setError(null);
        signUpConfirmPassword.setError(null);
        signUpInstitution.setError(null);
        signUpSelectDob.setError(null);
        signUpPathToCV.setError(null);
        percentage.setError(null);
        yearOfAdmission.setError(null);
        yearOfPassing.setError(null);

        // Store values at the time of the signup attempt.
        String email = signUpEmail.getText().toString();
        String name = signUpName.getText().toString();
        String userName = signUpUsername.getText().toString();
        String password = signUpPassword.getText().toString();
        String confirmPassword = signUpConfirmPassword.getText().toString();
        String institution = signUpInstitution.getText().toString();
        String dob = signUpDob.getText().toString();
        String formPercentage = percentage.getText().toString();
        String formYearPassing = yearOfPassing.getText().toString();
        String formYearAdm = yearOfAdmission.getText().toString();

        boolean cancel = false;
        View focusView = null;
        int passwordLength = password.length();

        // check URL
        try {
            URL pathToCV = new URL(signUpPathToCV.getText().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            signUpPathToCV.setError(getString(R.string.error_malformed_URL));
            cancel = true;
            focusView = signUpPathToCV;
        }

        if(noSkillSelected()){
            cancel = true;
            Toast.makeText(getApplicationContext(),getString(R.string.error_one_skill_required), Toast.LENGTH_SHORT).show();
            focusView = skillListDisplay;
        }

        // check dob
        else if(dob.isEmpty()){
            cancel = true;
            signUpSelectDob.setError(getString(R.string.error_field_required));
            focusView = signUpSelectDob;
        }

        // check percentage
        else if(formPercentage.isEmpty()){
            cancel = true;
            percentage.setError(getString(R.string.error_field_required));
            focusView = percentage;
        }

        // check year of admission
        else if(formYearAdm.isEmpty()){
            cancel = true;
            yearOfAdmission.setError(getString(R.string.error_field_required));
            focusView = yearOfAdmission;
        }

        // check year of passing
        else if(formYearPassing.isEmpty()){
            cancel = true;
            yearOfPassing.setError(getString(R.string.error_field_required));
            focusView = yearOfPassing;
        }

        // check for nonempty institution name
        else if(institution.isEmpty()){
            cancel = true;
            signUpInstitution.setError(getString(R.string.error_field_required));
            focusView = signUpInstitution;
        }

        // check for matching confirmPassword and confirmPassword
        else if(!password.equals(confirmPassword)){
            signUpConfirmPassword.setError(getString(R.string.error_passwords_do_not_match));
            focusView = signUpConfirmPassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        else if (passwordLength < 8) {
            if (passwordLength != 0)
                signUpPassword.setError(getString(R.string.error_invalid_password));
            else
                signUpPassword.setError(getString(R.string.error_field_required));

            focusView = signUpPassword;
            cancel = true;
        }

        // check for nonempty userName
        else if(userName.isEmpty()){
            cancel = true;
            signUpUsername.setError(getString(R.string.error_field_required));
            focusView = signUpUsername;
        }

        // check for nonempty signUpName
        else if(name.isEmpty()){
            cancel = true;
            signUpName.setError(getString(R.string.error_field_required));
            focusView = signUpName;
        }

        // Check for a valid email address.
        else if (email.isEmpty()) {
            signUpEmail.setError(getString(R.string.error_field_required));
            focusView = signUpEmail;
            cancel = true;
        } else if (!email.contains("@")) {
            signUpEmail.setError(getString(R.string.error_invalid_email));
            focusView = signUpEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
            // form is valid
            return true;

        return false;
    }

    private void displayNewCheckBox(String skillName) {

        ViewGroup list = (ViewGroup) findViewById(R.id.skill_list);

        CheckBox cb = new CheckBox(this);

        // generate SkillState for checkbox

        int id = cb.generateViewId();
        cb.setId(id);
        SkillState state = new SkillState(skillName, id);

        // set CheckBox Attributes
        cb.setText(skillName);
        cb.setTextSize(getResources()
                .getDimension(R.dimen.field_text_medium));

        // set onclick listener
        cb.setOnClickListener(this);

        // add CheckBox to skill_list
        list.addView(cb);

        skillStates.add(state);
    }

    private boolean noSkillSelected() {
        Iterator<SkillState> ite = skillStates.iterator();

        while(ite.hasNext()) if(ite.next().isChecked()) return false;

        return true;
    }

    private void displayFieldHeading(String s) {
        LinearLayout list = (LinearLayout) findViewById(R.id.skill_list);

        TextView heading = new TextView(this);
        heading.setText("Skills relevant to " + s);
        heading.setTextSize(getResources()
                .getDimension(R.dimen.field_heading_size));
        ListView.LayoutParams headingParams = new ListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        heading.setLayoutParams(headingParams);
        list.addView(heading);
    }

    private void displayRelevantSkills() {

        final String SKILL_FIELD_URL = "https://data.outfight74.hasura-app.io";

        // set checkField array as per state of checkboxes





        // set request options for all requests
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(SKILL_FIELD_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        // create retrofit adapter
        Retrofit retrofit =
                builder
                    .build();

        // create retrofit REST client
        getRelevantSkills skillClient =  retrofit.create(getRelevantSkills.class);


        // select skill, field from skill_field_relation where field = 'computer vision' or field = 'web dev'
        String query = "select skill, field from skill_field_relation where ";
        // if checkbox is ticked
        for (int i = 0; i < NUM_FIELDS; i++ ) {
            if (fieldCheckbox[i].isChecked()) {
                query += "field = \'" + field[i] + "\' or ";
            }
        }

        query = query.substring(0, query.length() - 4);

        // fetch relevant skills from server
        SQLQuery skillQuery = new SQLQuery(query);

        Call<Skill> call =
                skillClient.relevantSkills(skillQuery);

        showProgress(true);
        findViewById(R.id.login_progress).requestFocus();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<Skill>() {
            @Override
            public void onResponse(Call<Skill> call, Response<Skill> response) {
                // The network call was a success and we got a response

                // disappear the DONE button and make the signup button visible
                done.setEnabled(false);
                signUpButton.setVisibility(View.VISIBLE);


                // stop showing progress bar
                showProgress(false);

                // display skills under relevant field
                // TODO : Remove network unpredictability bug
                HashMap<String, List<String>> skillFieldMap = new HashMap<String, List<String>>();

                for(int i = 0; i < NUM_FIELDS; i++){
                    if(fieldCheckbox[i].isChecked())
                        skillFieldMap.put(field[i], new ArrayList<String>());
                }

                Iterator<List<String>> ite = response.body().getResult().iterator();
                ite.next();

                while (ite.hasNext()){
                    List<String> fieldSkillPair = ite.next();
                    skillFieldMap.get(fieldSkillPair.get(1)).add(fieldSkillPair.get(0));
                }

                for(int j = 0; j < NUM_FIELDS; j++) {
                    if (fieldCheckbox[j].isChecked()) {
                        displayFieldHeading(field[j]);

                        for (String s : skillFieldMap.get(field[j])) displayNewCheckBox(s);
                    }
                }

                Toast.makeText(getApplicationContext(), getString(R.string.response_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Skill> call, Throwable t) {
                // the network call was a failure

                // stop showing progress bar
                showProgress(false);

                // TODO: handle error
                Toast.makeText(getApplicationContext(), getString(R.string.error_sending_request), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setSkillCheckboxStates() {
        // set state of all skill checkboxes
        int size = skillStates.size();
        CheckBox cb;
        for(int i = 0;i < size; i++)
        {
            cb = (CheckBox) findViewById(skillStates.get(i).getId());

            if(cb.isChecked())
                skillStates.get(i).setChecked(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        final View doneButton = findViewById(R.id.field_select_done);
        final View mProgressView = findViewById(R.id.login_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            doneButton.setVisibility(show ? View.GONE : View.VISIBLE);
            doneButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    doneButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            doneButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
