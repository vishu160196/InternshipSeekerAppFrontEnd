package com.example.vishal.internshipseekerapp;

import java.util.List;

/**
 * Created by vishal on 15/7/17.
 */


class SignupForm {
    private String signUpEmail;
    private String signUpName;
    private String signUpUsername;
    private String signUpPassword;
    private String signUpDob;
    private String gender;
    private String role;
    private List<String> skillList;

    private String signUpInstitution;
    private String signUpPathToCV;
    private Float percentage;
    private Integer yearOfPassing;
    private Integer yearOfAdmission;

    private String company;
    private String designation;

    private String message;

    // constructor for student form
    public SignupForm(String signUpEmail, String signUpName, String signUpUsername, String signUpPassword,
                      String signUpInstitution, String signUpDob, String signUpPathToCV, String gender,
                      List<String> skillList, String role, Float percentage, Integer yearOfPassing,
                      Integer yearOfAdmission) {
        this.signUpEmail = signUpEmail;
        this.signUpName = signUpName;
        this.signUpUsername = signUpUsername;
        this.signUpPassword = signUpPassword;
        this.signUpInstitution = signUpInstitution;
        this.signUpDob = signUpDob;
        this.signUpPathToCV = signUpPathToCV;
        this.gender = gender;
        this.skillList = skillList;
        this.role = role;
        this.percentage = percentage;
        this.yearOfAdmission = yearOfAdmission;
        this.yearOfPassing = yearOfPassing;
    }

    // constructor for employer form
    public SignupForm(String signUpEmail, String signUpName, String signUpUsername, String signUpPassword, String company, String signUpDob,
                      String designation, String gender, List<String> skillList, String role) {
        this.signUpEmail = signUpEmail;
        this.signUpName = signUpName;
        this.signUpUsername = signUpUsername;
        this.signUpPassword = signUpPassword;
        this.company = company;
        this.signUpDob = signUpDob;
        this.designation = designation;
        this.gender = gender;
        this.skillList = skillList;
        this.role = role;
    }

    public Float getPercentage() {
        return percentage;
    }

    public Integer getYearOfPassing() {
        return yearOfPassing;
    }

    public Integer getYearOfAdmission() {
        return yearOfAdmission;
    }

    public String getCompany() {
        return company;
    }

    public String getDesignation() {
        return designation;
    }

    public String getRole() {
        return role;
    }

    public String getSignUpEmail() {
        return signUpEmail;
    }

    public String getMessage() {
        return message;
    }

    public String getSignUpName() {
        return signUpName;
    }

    public String getSignUpUsername() {
        return signUpUsername;
    }

    public String getSignUpPassword() {
        return signUpPassword;
    }

    public String getSignUpInstitution() {
        return signUpInstitution;
    }

    public String getSignUpDob() {
        return signUpDob;
    }

    public String getSignUpPathToCV() {
        return signUpPathToCV;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getSkillList() {
        return skillList;
    }
}
