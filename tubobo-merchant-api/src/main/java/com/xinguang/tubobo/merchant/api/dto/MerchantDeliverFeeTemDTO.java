package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/10/13.
 */
public class MerchantDeliverFeeTemDTO implements Serializable{
    private String name; //名称
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
