package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 23/7/17.
 */

class ProfileResponse {
    private String name;
    private String email;
    private String dob;
    private String gender;

    private String designation;
    private String company;

    private String institution;
    private Integer year_of_admission;
    private Integer year_of_passing;
    private String path_to_cv;
    private Float percentage;

    private Integer emp_id;
    private Integer student_id;

    public Integer getEmp_id() {
        return emp_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getDesignation() {
        return designation;
    }

    public String getInstitution() {
        return institution;
    }

    public Integer getYear_of_admission() {
        return year_of_admission;
    }

    public Integer getYear_of_passing() {
        return year_of_passing;
    }

    public String getPath_to_cv() {
        return path_to_cv;
    }

    public Float getPercentage() {
        return percentage;
    }
}
