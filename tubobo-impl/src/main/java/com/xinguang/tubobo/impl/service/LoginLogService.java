package com.xinguang.tubobo.impl.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinguang.tubobo.impl.entity.LoginLogEntity;
import com.xinguang.tubobo.impl.dao.LoginLogDAO;

@Service
public class LoginLogService {
    @Autowired
    private LoginLogDAO loginLogDAO;

    public void saveLoginLog(String tokenId,String storeId,String storeName,String employeeId,String employeeType,String phone) {
    	LoginLogEntity logEntity = new LoginLogEntity();
        logEntity.setTokenId(tokenId);
        logEntity.setSignStore(storeId);
        logEntity.setStoreName(storeName);
        logEntity.setEmployeeId(employeeId);
        logEntity.setEmployeeType(employeeType);
        logEntity.setPhone(phone);
        logEntity.setLoginTime(new Date());
    	loginLogDAO.save(logEntity);
    }

}
