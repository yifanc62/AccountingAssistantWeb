package com.cirnoteam.accountingassistant.web.json;

/**
 * Created by Yifan on 2017/7/20.
 */
public class LoginRespEntity {
    private String token;

    public LoginRespEntity(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
