package com.cirnoteam.accountingassistant.web.json;

/**
 * Created by Yifan on 2017/7/22.
 */
public class ResetRespEntity {
    private String resetToken;

    public ResetRespEntity(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
