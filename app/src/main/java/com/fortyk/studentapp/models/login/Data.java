package com.fortyk.studentapp.models.login;

public class Data {
    private String accessToken;
    private boolean error;
    private String errormessage;

    public Data(String accessToken, boolean error, String errormessage) {
        this.accessToken = accessToken;
        this.error = error;
        this.errormessage = errormessage;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
