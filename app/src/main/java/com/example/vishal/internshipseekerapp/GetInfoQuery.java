package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 29/7/17.
 */

interface GetInfoQueryArgsResources{
    String[] studentColumn = {"student_id", "name", "username", "email", "institution", "year_of_admission", "year_of_passing", "percentage",
    "path_to_cv"};
    String[] employerColumn = {"emp_id", "name", "username", "email", "company"};
}

class GetInfoWhereClause{
    private Integer student_id = null;
    private Integer emp_id = null;

    public GetInfoWhereClause(String role, Integer id) {
        if(role.equalsIgnoreCase("student"))
            emp_id = id;
        else if(role.equalsIgnoreCase("employer"))
            student_id = id;
    }
}

class GetInfoQueryArgs implements GetInfoQueryArgsResources, ProfileArgResources{
    private String table;
    private String[] columns;
    private GetInfoWhereClause where;

    public GetInfoQueryArgs(String role, Integer id) {
        if(role.equalsIgnoreCase("student")){
            columns = employerColumn;
            table = employerTable;
        }
        else if(role.equalsIgnoreCase("employer")){
            columns = studentColumn;
            table = studentTable;
        }

        where = new GetInfoWhereClause(role, id);
    }
}

class GetInfoQuery {
    private final String type = "select";
    private GetInfoQueryArgs args;

    public GetInfoQuery(String role, Integer id) {
        args = new GetInfoQueryArgs(role, id);
    }
}

class GetInfoResponse{
    private String name;
    private String username;
    private String email;

    private String company;
    private String designation;
    private Integer emp_id;

    private String institution;
    private String year_of_admission;
    private String year_of_passing;
    private String path_to_cv;
    private Float percentage;
    private Integer student_id;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getDesignation() {
        return designation;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public String getInstitution() {
        return institution;
    }

    public String getYear_of_admission() {
        return year_of_admission;
    }

    public String getYear_of_passing() {
        return year_of_passing;
    }

    public String getPath_to_cv() {
        return path_to_cv;
    }

    public Float getPercentage() {
        return percentage;
    }

    public Integer getStudent_id() {
        return student_id;
    }
}
