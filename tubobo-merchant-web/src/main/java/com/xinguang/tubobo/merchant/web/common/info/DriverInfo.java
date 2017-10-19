package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * 司机/骑手信息.
 */
public class DriverInfo implements Serializable{

    private String name;
    private String telephone;
    private String headImage;  //1.42加入骑手头像

    public DriverInfo(String name, String telephone, String headImage) {
        this.name = name;
        this.telephone = telephone;
        this.headImage = headImage;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }


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
