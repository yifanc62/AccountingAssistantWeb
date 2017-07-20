package com.cirnoteam.accountingassistant.web.json;

/**
 * Created by Yifan on 2017/7/21.
 */
public class RegisterRespEntity {
    private String activateToken;

    public RegisterRespEntity(String activateToken) {
        this.activateToken = activateToken;
    }

    public String getActivateToken() {
        return activateToken;
    }

    public void setActivateToken(String activateToken) {
        this.activateToken = activateToken;
    }
}
