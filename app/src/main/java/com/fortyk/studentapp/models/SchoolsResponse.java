package com.fortyk.studentapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolsResponse {
    @SerializedName("data")
    private List<School> schoolList;
    private boolean error;
    private String errormessage;

    public SchoolsResponse(List<School> schoolList, boolean error, String errormessage) {
        this.schoolList = schoolList;
        this.error = error;
        this.errormessage = errormessage;
    }

    public List<School> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<School> schoolList) {
        this.schoolList = schoolList;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
