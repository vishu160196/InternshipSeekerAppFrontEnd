package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 27/7/17.
 */

class GetStudentIdQueryArgsWhereClause{
    String username;
    public GetStudentIdQueryArgsWhereClause(String username) {
        this.username = username;
    }
}

class GetStudentIdQueryArgs{
    private final String table = "student_info";
    private final String[] columns = {"student_id"};

    private GetStudentIdQueryArgsWhereClause where;

    public GetStudentIdQueryArgs(String username) {
        where = new GetStudentIdQueryArgsWhereClause(username);
    }
}

class GetStudentIdQuery {
    private final String type = "select";
    private GetStudentIdQueryArgs args;

    public GetStudentIdQuery(String username) {
        args = new GetStudentIdQueryArgs(username);
    }
}

class GetEmployerIdQueryArgsWhereClause{
    String username;
    public GetEmployerIdQueryArgsWhereClause(String username) {
        this.username = username;
    }
}

class GetEmployerIdQueryArgs{
    private final String table = "employer_info";
    private final String[] columns = {"emp_id"};

    private GetEmployerIdQueryArgsWhereClause where;

    public GetEmployerIdQueryArgs(String username) {
        where = new GetEmployerIdQueryArgsWhereClause(username);
    }
}

class GetEmployerIdQuery {
    private final String type = "select";
    private GetEmployerIdQueryArgs args;

    public GetEmployerIdQuery(String username) {
        args = new GetEmployerIdQueryArgs(username);
    }
}
