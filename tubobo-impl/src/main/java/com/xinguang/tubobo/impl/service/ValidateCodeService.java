package com.xinguang.tubobo.impl.service;

import java.util.Calendar;
import java.util.Date;

import com.xinguang.tubobo.api.enums.EnumRespCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.ValidUtils;
import com.xinguang.tubobo.impl.entity.ValidateCodeEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.impl.dao.ValidateCodeDAO;

@Service
public class ValidateCodeService {
	
    @Autowired
    private ValidateCodeDAO validateCodeDAO;
    
    public void save(ValidateCodeEntity entity) {
    	entity.setId(IdGen.uuid());
    	entity.setCreateDate(new Date());
    	entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
    	entity.setResult(ValidateCodeEntity.RESULT_INIT);
        validateCodeDAO.save(entity);
    }

    public ValidateCodeEntity get(String id) {
    	return validateCodeDAO.get(id);
    }
    
    public int disableOldCodes(String phone,String type) {
        return validateCodeDAO.disableOldCodes(phone, type);
    }
    
    public ValidateCodeEntity findUnValidateCodeByPhoneAndType(String phone,String type) {
    	return validateCodeDAO.findUnValidateCodeByPhoneAndType(phone, type);
    }
    
    public int updateResultStatus(String id,String result) {
    	return validateCodeDAO.updateResultStatus(id, result);
    }
    
    public Page<ValidateCodeEntity> find(Page<ValidateCodeEntity> page,ValidateCodeEntity code) {
        return validateCodeDAO.find(page, code);
    }
    
    /**
     * 保存验证码发送记录
     * @return ValidateCode
     */
    public String saveValidateCode(String phone,String type){
    	//无效之前的所有验证码记录
    	disableOldCodes(phone, type);
    	//保存新验证码记录
    	ValidateCodeEntity entity = new ValidateCodeEntity();
		entity.setPhone(phone);
		entity.setType(type);
		entity.setCode(ValidUtils.getValidateCode(6));
		//有效时间一小时
		Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, 1);
		entity.setExpireTime(c.getTime());
    	save(entity);
    	return entity.getCode();
    }
    
    /**
     * 验证短信验证码
     */
    public EnumRespCode checkValidateCode(String phone,String type,String code){
    	if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(code)) {
    		ValidateCodeEntity entity = findUnValidateCodeByPhoneAndType(phone, type);
            if (entity != null) {
            	if(entity.getExpireTime().getTime() >= new Date().getTime()){
            		if(entity.getCode().equals(code)){
                    	//验证成功
                    	updateResultStatus(entity.getId(),ValidateCodeEntity.RESULT_SUC);
                    	return EnumRespCode.SUCCESS;
                    }else {
                    	updateResultStatus(entity.getId(),ValidateCodeEntity.RESULT_FAIL);
                    	return EnumRespCode.VALIDATE_CODE_ERROR;
            		}
                }else {
                	updateResultStatus(entity.getId(),ValidateCodeEntity.RESULT_EXPIRED);
                	return EnumRespCode.VALIDATE_CODE_EXPIRED;
        		}
            }else {
				return EnumRespCode.VALIDATE_CODE_NOT_FOUND;
			}
		}else {
			return EnumRespCode.PARAMS_ERROR;
		}
    }
    
}
