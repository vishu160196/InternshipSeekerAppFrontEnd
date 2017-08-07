package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 29/7/17.
 */

class SkillResponse {
    private String skill;

    public String getSkill() {
        return skill;
    }
}

class SkillQueryArgs{
    private String table;
    private final String[] columns = {"skill"};
    private MessagesWhereClause where;

    public SkillQueryArgs(String role, Integer id, boolean isDisplaySkillsQuery) {
        if (!isDisplaySkillsQuery) {
            if(role.equalsIgnoreCase("student"))
                table = "student_skills";

            else if(role.equalsIgnoreCase("employer"))
                table = "employer_skill";
        }
        else{
            if(role.equalsIgnoreCase("student"))
                table = "employer_skill";

            else if(role.equalsIgnoreCase("employer"))
                table = "student_skills";
        }

        where = new MessagesWhereClause(id);
    }
}

class SkillQuery{
    private final String type = "select";

    private SkillQueryArgs args;

    public SkillQuery(String role, Integer id, boolean isDisplaySkillsQuery) {
        args = new SkillQueryArgs(role, id, isDisplaySkillsQuery);
    }
}
