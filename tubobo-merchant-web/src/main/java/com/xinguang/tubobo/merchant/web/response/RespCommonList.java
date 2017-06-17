package com.xinguang.tubobo.merchant.web.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public class RespCommonList<T> {
    private List<T> list;
    public RespCommonList(List<T> list){
        this.list = list;
        if (list == null){
            this.list = new ArrayList<>(0);
        }
    }
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
