package com.fortyk.studentapp.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("data")
    private Data data;
    private boolean error;
    private String errormessage;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
