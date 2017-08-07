package com.example.vishal.internshipseekerapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
//import retrofit2.http.GET;
import retrofit2.http.POST;

//import static com.example.vishal.internshipseekerapp.SQLQuery.args.*;

class Args{
    private String sql;

    public Args(String sql) {
        this.sql = sql;
    }
}

class SQLQuery{
    final String type = "run_sql";
    private Args args;
    public SQLQuery(java.lang.String sql) {
        args = new Args(sql);
    }
}

class Skill{
    private String result_type;
    private List<List<String>> result;

    public String getResult_type() {
        return result_type;
    }

    public List<List<String>> getResult() {
        return result;
    }
}

public interface getRelevantSkills {
    @POST("/v1/query")
    Call<Skill> relevantSkills(
            @Body SQLQuery fetchSkills
    );
}
