package com.example.vishal.internshipseekerapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.Set;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.LogoutResponseListener;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static HasuraUser user;
    public static HasuraClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = Hasura.getClient();
        user = client.getUser();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // link ViewPager with TabLayout
        tabLayout.setupWithViewPager(mViewPager);

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

        //noinspection SimplifiableIfStatement
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
                Toast.makeText(getApplicationContext(), getString(R.string.logout_success),Toast.LENGTH_SHORT).show();
                Log.d("serverMessage", message);
                // start LoginActivity
                finish();
            }

            @Override
            public void onFailure(HasuraException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Three fragments
     */

    static String[] role;
    public static class ProfileFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

            View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
            return fragmentView;
        }

        private final int EMPLOYER_TABLE_ROWS = 4;
        private final int STUDENT_TABLE_ROWS = 7;
        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            // make network calls to fetch data from server

            role = user.getRoles();

            if(role[1].equalsIgnoreCase(getString(R.string.student_role))){
                StudentProfileQuery query = new StudentProfileQuery(user.getId());
                client.asRole(getString(R.string.student_role).toLowerCase()).useDataService()
                        .setRequestBody(query)
                        .expectResponseTypeArrayOf(ProfileResponse.class)
                        .enqueue(new Callback<List<ProfileResponse>, HasuraException>() {
                            @Override
                            public void onSuccess(List<ProfileResponse> response) {
                                //Handle response

                                //get headingContainer
                                LinearLayout headingContainer = (LinearLayout) getView().findViewById(R.id.name_and_institute);
                                // get inflater object
                                LayoutInflater inflater = LayoutInflater.from(getContext());
//
                                // place name
                                EditText headingName = (EditText) inflater.inflate(R.layout.profile_page_title, headingContainer, false);
                                headingName.setText(response.get(0).getName(), TextView.BufferType.NORMAL);
                                headingContainer.addView(headingName, 0);
                                headingName.setTextIsSelectable(true);

                                // place instituteName
                                EditText headingInstitution = (EditText) inflater.inflate(R.layout.profile_page_title, headingContainer, false);
                                headingInstitution.setText("Student" + " at " + response.get(0).getInstitution(),
                                        TextView.BufferType.SPANNABLE);
                                headingContainer.addView(headingInstitution, 1);
                                headingInstitution.setTextIsSelectable(true);

                                String[] field = {"Email", "Date Of Birth", "Gender", "Year of Admission", "Year of Passing", "Percentage",
                                        "Resume URL"};
                                String[] detail = {response.get(0).getEmail(), response.get(0).getDob(), response.get(0).getGender(),
                                        response.get(0).getYear_of_admission().toString(), response.get(0).getYear_of_passing().toString(),
                                        response.get(0).getPercentage().toString(), response.get(0).getPath_to_cv()};
                                Log.d("nonInflater", detail.toString());

                                // insert TableRows in TableLayout
                                // get TableLayout container
                                TableLayout tableContainer = (TableLayout) getView().findViewById(R.id.profile_table);

                                for(int i = 0; i < STUDENT_TABLE_ROWS; i++){
                                    // inflate TableRow
                                    TableRow row = (TableRow) inflater.inflate(R.layout.profile_page_table, tableContainer, false);
                                    tableContainer.addView(row);

                                    // inflate element of col0 and set field, width
                                    EditText col0Element = (EditText)inflater.inflate(R.layout.profile_table_row_element_0, row, false);
                                    col0Element.setText(field[i], TextView.BufferType.NORMAL);
                                    col0Element.setTextIsSelectable(true);
                                    row.addView(col0Element);

                                    // inflate element of col1 and set detail, width
                                    EditText col1Element = (EditText) inflater.inflate(R.layout.profile_table_row_element_1, row, false);
                                    col1Element.setText(detail[i], TextView.BufferType.NORMAL);
                                    col1Element.setTextIsSelectable(true);
                                    row.addView(col1Element);

                                    View line = inflater.inflate(R.layout.table_line, tableContainer, false);
                                    tableContainer.addView(line);
                                }
                            }

                            @Override
                            public void onFailure(HasuraException e) {
                                //Handle error
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            else if(role[1].equalsIgnoreCase(getString(R.string.employer_role))){
                EmployerProfileQuery query = new EmployerProfileQuery(user.getId());
                client.asRole(getString(R.string.employer_role).toLowerCase()).useDataService()
                        .setRequestBody(query)
                        .expectResponseTypeArrayOf(ProfileResponse.class)
                        .enqueue(new Callback<List<ProfileResponse>, HasuraException>() {
                            @Override
                            public void onSuccess(List<ProfileResponse> response) {
                                // Handle response

                                // get headingContainer
                                LinearLayout headingContainer = (LinearLayout) getView().findViewById(R.id.name_and_institute);
                                // get inflater object
                                LayoutInflater inflater = LayoutInflater.from(getContext());

                                // place name
                                EditText headingName = (EditText) inflater.inflate(R.layout.profile_page_title, headingContainer, false);
                                headingName.setText(response.get(0).getName(), TextView.BufferType.NORMAL);
                                headingName.setTextIsSelectable(true);
                                headingContainer.addView(headingName, 0);

                                // place company name and designation
                                EditText headingCompany = (EditText) inflater.inflate(R.layout.profile_page_title, headingContainer, false);
                                headingCompany.setText(response.get(0).getDesignation() + " at " + response.get(0).getCompany(), TextView.BufferType.NORMAL);
                                headingCompany.setTextIsSelectable(true);
                                headingContainer.addView(headingCompany, 1);

                                String[] field = {"Name", "Email", "Date Of Birth", "Gender"};
                                String[] detail = {response.get(0).getName(), response.get(0).getEmail(), response.get(0).getDob(), response.get(0).getGender()};

                                // insert TableRows in TableLayout
                                // get TableLayout container
                                TableLayout tableContainer = (TableLayout) getView().findViewById(R.id.profile_table);

                                for(int i = 0; i < EMPLOYER_TABLE_ROWS; i++){
                                    // inflate TableRow
                                    TableRow row = (TableRow) inflater.inflate(R.layout.profile_page_table, tableContainer, false);
                                    tableContainer.addView(row);

                                    // inflate element of col0 and set field, width
                                    EditText col0Element = (EditText)inflater.inflate(R.layout.profile_table_row_element_0, row, false);
                                    col0Element.setText(field[i], TextView.BufferType.NORMAL);
                                    col0Element.setTextIsSelectable(true);
                                    row.addView(col0Element);

                                    // inflate element of col1 and set detail, width
                                    TextView col1Element = (TextView)inflater.inflate(R.layout.profile_table_row_element_1, row, false);
                                    col1Element.setText(detail[i], TextView.BufferType.NORMAL);
                                    col1Element.setTextIsSelectable(true);
                                    row.addView(col1Element);

                                    View line = inflater.inflate(R.layout.table_line, tableContainer, false);
                                    tableContainer.addView(line);
                                }
                            }

                            @Override
                            public void onFailure(HasuraException e) {
                                //Handle error
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public static class MessagesFragment extends Fragment implements View.OnClickListener {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

            View fragmentView = inflater.inflate(R.layout.fragment_messages, container, false);
            return fragmentView;
        }

        public static String MESSAGE_DETAILS = "messageDetails";
        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            // register onClick listener for compose button
            FloatingActionButton compose = (FloatingActionButton) getView().findViewById(R.id.compose);
            compose.setOnClickListener(this);

            // make call to fetch all messages of user
            final String[] role = user.getRoles();

            GetMessagesQuery query = new GetMessagesQuery(role[1], user.getId());
            client.asRole(role[1]) //throws an error if the current user does not have this role
                    .useDataService()
                    .setRequestBody(query)
                    .expectResponseTypeArrayOf(GetMessagesResponse.class)
                    .enqueue(new Callback<List<GetMessagesResponse>, HasuraException>() {
                        @Override
                        public void onSuccess(List<GetMessagesResponse> getMessagesResponses) {
                            final HashMap<Integer, GetMessagesResponse> responses = new HashMap<>();

                            int size = getMessagesResponses.size();

                            for(int i = 0; i < size; i++)
                                responses.put(getMessagesResponses.get(i).getMessage_id(),
                                        getMessagesResponses.get(i));

                            LinearLayout messageContainer = (LinearLayout) getView().findViewById(R.id.message_display_layout);
                            for(int i = 0; i < size; i++){
                                LayoutInflater inflater = LayoutInflater.from(getContext());

                                // inflate a message box
                                TextView messageBox = (TextView) inflater.inflate(R.layout.message_box, messageContainer, false);

                                // set first line to sender_username and set second line to message_subject
                                messageBox.setText(messageBoxTemplate(getMessagesResponses.get(i).getSender_username(),
                                        getMessagesResponses.get(i).getMessage_subject()));

                                // set tag of message box to message_id
                                messageBox.setTag(getMessagesResponses.get(i).getMessage_id());
                                // if message is read

                                if(getMessagesResponses.get(i).is_read())
                                    // set background to @drawable/rounded_corners_read
                                    messageBox.setBackground(getResources().getDrawable(R.drawable.rounded_corners_read));

                                // add message box to message_display_layout
                                messageContainer.addView(messageBox);
                            }

                            // set onClick listeners for all message boxes
                            for(int i = 0; i < size; i++){

                                getView().findViewWithTag(getMessagesResponses.get(i).getMessage_id()).setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View messageBox){
                                        // start new activity for displaying message
                                        Integer id = Integer.parseInt(messageBox.getTag().toString());

                                        String[] messageDetails = { responses.get(id).getMessage_id().toString(),
                                                responses.get(id).getSender_username(), responses.get(id).getMessage_subject(),
                                                responses.get(id).getMessage_body(), role[1], responses.get(id).getSender_id().toString() };

                                        Intent readMessage = new Intent(getContext(), DisplayMessage.class);
                                        readMessage.putExtra(MESSAGE_DETAILS, messageDetails);
                                        startActivity(readMessage);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        public String messageBoxTemplate(String senderName, String subject){
            String template = "FROM : " + senderName + "\nSUBJECT : " + subject;
            return template;
        }

        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.compose : launchMessageComposer();
            }
        }

        private void launchMessageComposer() {
            // Create the fragment and show it as a dialog.
            SendMessageDialog newMessage = SendMessageDialog.newMessage(R.string.prompt_receiver_username);
            newMessage.show(getFragmentManager(), "launchComposeMessageDialog");
        }
    }

    public void doPositiveClick(final String username){
        final String role = user.getRoles()[1];

        GetStudentIdQuery getStudentIdQuery = null;
        GetEmployerIdQuery getEmployerIdQuery = null;

        // get id of username
        if(role.equals(getString(R.string.student_role).toLowerCase()))
            getEmployerIdQuery = new GetEmployerIdQuery(username);
        else if(role.equals(getString(R.string.employer_role).toLowerCase()))
            getStudentIdQuery = new GetStudentIdQuery(username);

        Object query = null;
        if(getEmployerIdQuery != null)
            query = getEmployerIdQuery;
        else if(getStudentIdQuery != null)
            query = getStudentIdQuery;

        client.asRole(role)
                .useDataService().setRequestBody(query)
                .expectResponseTypeArrayOf(ProfileResponse.class)
                .enqueue(new Callback<List<ProfileResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(List<ProfileResponse> profileResponses) {
                        if(profileResponses.size() == 0){
                            Toast.makeText(getApplicationContext(), getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                            return;
                        }

                        String[] messageDetails = new String[6];
                        if(role.equals(getString(R.string.student_role).toLowerCase())){
                            //messageDetails = {null, username, null, null, role, profileResponses.get(0).getEmp_id().toString()}
                            messageDetails[0] = null;
                            messageDetails[1] = username;
                            messageDetails[2] = null;
                            messageDetails[3] = null;
                            messageDetails[4] = role;
                            messageDetails[5] = profileResponses.get(0).getEmp_id().toString();
                        }
                        else if(role.equals(getString(R.string.employer_role).toLowerCase())){
                            messageDetails[0] = null;
                            messageDetails[1] = username;
                            messageDetails[2] = null;
                            messageDetails[3] = null;
                            messageDetails[4] = role;
                            messageDetails[5] = profileResponses.get(0).getStudent_id().toString();
                        }

                        // start Message activity
                        Intent sendMessage = new Intent(getApplicationContext(), Message.class);
                        sendMessage.putExtra(MessagesFragment.MESSAGE_DETAILS, messageDetails);
                        startActivity(sendMessage);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    static List<String> userSkills;
    public static class HomeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

            return fragmentView;
        }

        private static Set<Integer> demandList;
        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            demandList = new HashSet<>();
            super.onActivityCreated(savedInstanceState);

            TextView heading = (TextView) getView().findViewById(R.id.home_heading);

            if(role[1].equalsIgnoreCase(getString(R.string.employer_role)))
                heading.setText(getString(R.string.employer_home_heading));
            else if(role[1].equalsIgnoreCase(getString(R.string.student_role)))
                heading.setText(getString(R.string.student_home_heading));

            // fetch all skills of user from respective skill table
            userSkills = new ArrayList<>();
            SkillQuery skillQuery = new SkillQuery(role[1], user.getId(), false);
            client.asRole(role[1]).useDataService()
                    .setRequestBody(skillQuery)
                    .expectResponseTypeArrayOf(SkillResponse.class)
                    .enqueue(new Callback<List<SkillResponse>, HasuraException>() {
                        @Override
                        public void onSuccess(List<SkillResponse> response) {
                            // Handle response
                            for(SkillResponse skill : response)
                                userSkills.add(skill.getSkill());

                            String queryString = null;

                            if (role[1].equalsIgnoreCase(getString(R.string.student_role))) {
                                queryString = "select id from employer_skill where ";
                            }
                            else if(role[1].equalsIgnoreCase(getString(R.string.employer_role))){
                                queryString = "select id from student_skills where ";
                            }

                            for(String skill : userSkills)
                                queryString += "skill = \'" + skill + "\'" + " or ";

                            queryString = queryString.substring(0, queryString.length() - 4);

                            DemandQuery demand = new DemandQuery(queryString);
                            client.asRole(role[1]).useDataService()
                                    .setRequestBody(demand)
                                    .expectResponseType(DemandResponse.class)
                                    .enqueue(new Callback<DemandResponse, HasuraException>() {
                                        @Override
                                        public void onSuccess(DemandResponse response) {
                                            // Handle response
                                            List<List<String>> demandIds = response.getResult();

                                            Iterator<List<String>> ite = response.getResult().iterator();
                                            ite.next();
                                            while(ite.hasNext())
                                                demandList.add(Integer.parseInt(ite.next().get(0)));

                                            for(Integer id : demandList){
                                                GetInfoQuery infoQuery = new GetInfoQuery(role[1], id);
                                                client.asRole(role[1]).useDataService()
                                                        .setRequestBody(infoQuery)
                                                        .expectResponseTypeArrayOf(GetInfoResponse.class)
                                                        .enqueue(new Callback<List<GetInfoResponse>, HasuraException>() {
                                                            @Override
                                                            public void onSuccess(List<GetInfoResponse> response) {
                                                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                                                LinearLayout container = (LinearLayout) getView().findViewById(R.id.suggestion_display_layout);

                                                                TextView suggestion = (TextView) inflater.inflate(R.layout.message_box, container, false);

                                                                Integer suggestionId = null;
                                                                GetInfoResponse details = response.get(0);
                                                                String text = null;
                                                                if(role[1].equalsIgnoreCase(getString(R.string.student_role))){
                                                                    text = getEmployerTemplate(details.getName(), details.getDesignation(), details.getCompany(),
                                                                            details.getEmail(), details.getUsername());
                                                                    suggestionId = details.getEmp_id();
                                                                }

                                                                else if(role[1].equalsIgnoreCase(getString(R.string.employer_role))){
                                                                    text = getStudentTemplate(details.getName(), details.getInstitution(), details.getYear_of_admission(),
                                                                            details.getYear_of_passing(), details.getPercentage(), details.getPath_to_cv(), details.getEmail(),
                                                                            details.getUsername());
                                                                    suggestionId = details.getStudent_id();
                                                                }


                                                                suggestion.setText(text);
                                                                suggestion.setTag(suggestionId);

                                                                suggestion.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Integer id;
                                                                        String name = ((TextView)view).getText().toString().split("\n")[0];
                                                                        id = (Integer) view.getTag();
                                                                        displaySkills(name, id, role[1]);
                                                                    }
                                                                });

                                                                container.addView(suggestion);
                                                            }

                                                            @Override
                                                            public void onFailure(HasuraException e) {
                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onFailure(HasuraException e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            //Handle error
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        public static final String SKILL_DETAILS = "skillDetails";
        private void displaySkills(String name, Integer id, String role) {
            Intent showSkills = new Intent(getContext(), DisplaySkills.class);
            String[] detail = {name, id.toString(), role};
            showSkills.putExtra(SKILL_DETAILS, detail);
            startActivity(showSkills);
        }

        private String getStudentTemplate(String name, String institution, String year_of_admission, String year_of_passing,
                                          Float percentage, String path_to_cv, String email, String username) {
            String template;

            template = name + "\nStudent at " + institution + "\nBatch:\t" + year_of_admission + " to " + year_of_passing + "\nPercentage:\t"
                    + percentage + "\nCV:\t" + path_to_cv + "\nEmail:\t" + email + "\nUsername:\t" + username;

            return template;
        }

        private String getEmployerTemplate(String name, String designation, String company, String email, String username) {
            String template;

            template = name + "\n" + designation + " at " + company + "\nEmail:\t" + email + "\nUsername:\t" + username;

            return template;
        }
    }

    public static class SendMessageDialog extends DialogFragment {

        public static SendMessageDialog newMessage(int title) {
            SendMessageDialog frag = new SendMessageDialog();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setView(R.layout.dialog)
                    .setPositiveButton(R.string.send_message,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String username = ((EditText)getDialog().findViewById(R.id.receiver_username))
                                            .getText().toString();
                                    ((MainActivity)getActivity()).doPositiveClick(username);
                                }
                            }
                    )
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dismiss();
                                }
                            }
                    )
                    .create();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a UI (defined as a static inner class below).
            switch(position){
                case 0:
                    return new ProfileFragment();

                case 1:
                    return new MessagesFragment();

                case 2:
                    return new HomeFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.profile_tab);
                case 1:
                    return getString(R.string.messages_tab);
                case 2:
                    return getString(R.string.home_tab);
            }
            return null;
        }
    }
}
