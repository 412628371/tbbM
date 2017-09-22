package com.xinguang.tubobo.merchant.api.condition;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class RiderInfo implements Serializable{
    private String name;
    private String telephone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
