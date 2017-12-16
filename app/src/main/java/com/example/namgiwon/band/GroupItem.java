package com.example.namgiwon.band;

import java.io.Serializable;

/**
 * Created by namgiwon on 2017. 11. 23..
 */

public class GroupItem implements Serializable{
    private  int group_id;
    private String group_member;
    private String group_name;
    private String group_dept;
    private String group_school;
    private String chief;

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_dept() {
        return group_dept;
    }

    public void setGroup_dept(String group_dept) {
        this.group_dept = group_dept;
    }

    public String getGroup_school() {
        return group_school;
    }

    public void setGroup_school(String group_school) {
        this.group_school = group_school;
    }

    public String getGroup_member() {
        return group_member;
    }

    public void setGroup_member(String group_member) {
        this.group_member = group_member;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
