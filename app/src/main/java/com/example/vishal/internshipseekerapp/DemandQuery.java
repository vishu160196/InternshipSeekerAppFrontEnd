package com.example.vishal.internshipseekerapp;

import java.util.List;

/**
 * Created by vishal on 29/7/17.
 */

class DemandQueryArgs{
    private String sql;

    public DemandQueryArgs(String queryString) {
        this.sql = queryString;
    }
}

/*{
	"auth_token": "qeq4pftfb4jq2luaxkpi649g9pojass9",
    "type" : "run_sql",
    "args" : {
        "sql" : "select student_id from student_info"
    }
}*/

class DemandQuery {
    private final String type = "run_sql";
    private DemandQueryArgs args;

    public DemandQuery(String queryString) {
        args = new DemandQueryArgs(queryString);
    }
}

class DemandResponse{
    private String result_type;

    private List<List<String>> result;

    public String getResult_type() {
        return result_type;
    }

    public List<List<String>> getResult() {
        return result;
    }
}

/*{
    "result_type": "TuplesOk",
    "result": [
        [
            "id"
        ],
        [
            "2"
        ],
        [
            "8"
        ],
        [
            "10"
        ],
        [
            "4"
        ]
    ]
}*/
