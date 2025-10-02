package com.fortyk.studentapp.models;

public class School {
    private String schooltype;
    private String schoolname;
    private String city;
    private String country;
    private String state;

    public School(String schooltype, String schoolname, String city, String country, String state) {
        this.schooltype = schooltype;
        this.schoolname = schoolname;
        this.city = city;
        this.country = country;
        this.state = state;
    }

    public String getSchooltype() {
        return schooltype;
    }

    public void setSchooltype(String schooltype) {
        this.schooltype = schooltype;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
