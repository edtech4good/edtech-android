package com.fortyk.studentapp.models.signup;

public class Students {
    private String studentfirstname;
    private String contact;
    private String genderid;
    private String city;
    private String country;
    private String state;
    private String dateofjoin;
    private String schoolusername;
    private String schooluserpasswordhash;

    public Students(String studentfirstname, String contact, String genderid,
                    String city, String country, String state,
                    String dateofjoin, String schoolusername, String schooluserpasswordhash) {
        this.studentfirstname = studentfirstname;
        this.contact = contact;
        this.genderid = genderid;
        this.city = city;
        this.country = country;
        this.state = state;
        this.dateofjoin = dateofjoin;
        this.schoolusername = schoolusername;
        this.schooluserpasswordhash = schooluserpasswordhash;
    }

    public String getStudentfirstname() {
        return studentfirstname;
    }

    public void setStudentfirstname(String studentfirstname) {
        this.studentfirstname = studentfirstname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGenderid() {
        return genderid;
    }

    public void setGenderid(String genderid) {
        this.genderid = genderid;
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

    public String getDateofjoin() {
        return dateofjoin;
    }

    public void setDateofjoin(String dateofjoin) {
        this.dateofjoin = dateofjoin;
    }

    public String getSchoolusername() {
        return schoolusername;
    }

    public void setSchoolusername(String schoolusername) {
        this.schoolusername = schoolusername;
    }

    public String getSchooluserpasswordhash() {
        return schooluserpasswordhash;
    }

    public void setSchooluserpasswordhash(String schooluserpasswordhash) {
        this.schooluserpasswordhash = schooluserpasswordhash;
    }
}
