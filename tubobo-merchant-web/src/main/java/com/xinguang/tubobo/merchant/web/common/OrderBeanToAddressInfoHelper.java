package com.xinguang.tubobo.merchant.web.common;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.common.AddressInfo;

/**
 * Created by Administrator on 2017/6/5.
 */
public class OrderBeanToAddressInfoHelper {
    public static AddressInfo putAddressFromOrderBean(MerchantOrderEntity entity, AddressInfo addressInfo,boolean isSender){
        if (null == addressInfo){
            addressInfo = new AddressInfo();
        }
        String name="";
        String phone="";

        String province="";
        String city="";
        String district="";
        String street="";
        String detail="";
        String roomNo="";

        Double latitude = null;
        Double longitude = null;
        if (isSender){
            name = entity.getSenderName();
            phone = entity.getSenderPhone();
            province = entity.getSenderAddressProvince();
            city = entity.getSenderAddressCity();
            district = entity.getSenderAddressDistrict();
            street = entity.getSenderAddressStreet();
            detail = entity.getSenderAddressDetail();
            roomNo = entity.getSenderAddressRoomNo();
            latitude = entity.getSenderLatitude();
            longitude = entity.getSenderLongitude();
        }else {
            name = entity.getReceiverName();
            phone = entity.getReceiverPhone();
            province = entity.getReceiverAddressProvince();
            city = entity.getReceiverAddressCity();
            district = entity.getReceiverAddressDistrict();
            street = entity.getReceiverAddressStreet();
            detail = entity.getReceiverAddressDetail();
            roomNo = entity.getReceiverAddressRoomNo();
            latitude = entity.getReceiverLatitude();
            longitude = entity.getReceiverLongitude();
        }
        addressInfo.setName(name);
        addressInfo.setAddressStreet(street);
        addressInfo.setAddressCity(city);
        addressInfo.setAddressDetail(detail);
        addressInfo.setAddressDistrict(district);
        addressInfo.setAddressProvince(province);
        addressInfo.setAddressRoomNo(roomNo);
        addressInfo.setTelephone(phone);
        addressInfo.setLatitude(latitude);
        addressInfo.setLongitude(longitude);
        return addressInfo;
    }
}
