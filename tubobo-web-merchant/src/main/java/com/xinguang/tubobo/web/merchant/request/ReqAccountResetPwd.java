package com.xinguang.tubobo.web.merchant.request;

import javax.validation.constraints.Size;

public class ReqAccountResetPwd {

    @Size(min = 1, message = "oldPwd 不能为空")
    private String oldPwd;
    @Size(min = 1, message = "newPwd 不能为空")
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
