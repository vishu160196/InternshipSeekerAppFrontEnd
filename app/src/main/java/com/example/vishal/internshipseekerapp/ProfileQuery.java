package com.example.vishal.internshipseekerapp;


/**
 * Created by vishal on 23/7/17.
 */

interface ProfileArgResources{
    String studentTable = "student_info";
    String employerTable = "employer_info";

    String[] studentTableCols = {"name", "email", "institution", "year_of_admission",
            "year_of_passing", "percentage", "dob", "gender", "path_to_cv"};
    String[] employerTableCols = {"name", "email", "company", "dob", "gender", "designation"};
}


class StudentWhereClause{
    Integer student_id;

    StudentWhereClause(Integer id){
        student_id = id;
    }
}

class EmployerWhereClause{
    Integer emp_id;

    EmployerWhereClause(Integer id){
        emp_id = id;
    }
}

class StudentProfileArgs implements ProfileArgResources{
    private String[] columns;
    private StudentWhereClause where;
    private String table = studentTable;


    public StudentProfileArgs(Integer id) {
        columns = studentTableCols;
        where = new StudentWhereClause(id);
    }
}

class EmployerProfileArgs implements ProfileArgResources{
    private String[] columns;
    private EmployerWhereClause where;
    private String table = employerTable;


    public EmployerProfileArgs(Integer id) {
        columns = employerTableCols;
        where = new EmployerWhereClause(id);
    }
}

class StudentProfileQuery implements ProfileArgResources {

    /*
    * {
        "type" : "select",
        "args" : {
                    "table" : "table_name",
                    "columns" : ["col0", "col1"],
                    "where" : {"fieldName" : "name"}
                 }
      }
    */

    private final String type = "select";
    private StudentProfileArgs args;

    public StudentProfileQuery(Integer id) {
        args = new StudentProfileArgs(id);

    }
}

class EmployerProfileQuery implements ProfileArgResources {

    /*
    * {
        "type" : "select",
        "args" : {
                    "table" : "table_name",
                    "columns" : ["col0", "col1"],
                    "where" : {"fieldName" : "name"}
                 }
      }
    */

    private final String type = "select";
    private EmployerProfileArgs args;

    public EmployerProfileQuery(Integer id) {
        args = new EmployerProfileArgs(id);
    }
}
