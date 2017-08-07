package com.example.vishal.internshipseekerapp;

/**
 * Created by vishal on 14/7/17.
 */

class SkillState {
    private String skillName;
    private boolean isChecked;
    private int id;

    public SkillState(String skillName, int id) {
        this.skillName = skillName;
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getId() {
        return id;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
