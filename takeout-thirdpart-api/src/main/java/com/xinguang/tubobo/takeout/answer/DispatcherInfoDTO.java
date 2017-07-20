package com.xinguang.tubobo.takeout.answer;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/17.
 */
public class DispatcherInfoDTO implements Serializable{

    private String riderName;
    private String riderPhone;

    public DispatcherInfoDTO(){}

    public DispatcherInfoDTO(String riderName,String riderPhone){
        this.riderName = riderName;
        this.riderPhone = riderPhone;
    }
    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }
}
