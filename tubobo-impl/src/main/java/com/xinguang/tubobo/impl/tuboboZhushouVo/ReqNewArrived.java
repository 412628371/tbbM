package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.List;

public class ReqNewArrived {

    private String tokenId;
    private List<Data> list;
    
    public static class Data {
        private String expressCompanyId;//快递公司id
        private String waybillNo;//运单号
        
        public String getExpressCompanyId() {
            return expressCompanyId;
        }
        public String getWaybillNo() {
            return waybillNo;
        }
        public void setExpressCompanyId(String expressCompanyId) {
            this.expressCompanyId = expressCompanyId;
        }
        public void setWaybillNo(String waybillNo) {
            this.waybillNo = waybillNo;
        }
    }

    public String getTokenId() {
        return tokenId;
    }
    public List<Data> getList() {
        return list;
    }
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    public void setList(List<Data> list) {
        this.list = list;
    }
}
