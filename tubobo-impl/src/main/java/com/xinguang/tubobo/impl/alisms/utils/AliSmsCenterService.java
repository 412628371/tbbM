package com.xinguang.tubobo.impl.alisms.utils;

import com.hzmux.hzcms.common.utils.ValidUtils;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;
import com.xinguang.tubobo.impl.alisms.enums.EnumAliSmsTemplate;
import com.xinguang.tubobo.impl.alisms.service.AliSmsRecordService;
import com.xinguang.tubobo.impl.alisms.service.AliSmsTemplateService;
import com.xinguang.tubobo.impl.runnable.RunnableALiSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliSmsCenterService {

//    @Autowired
//    private AppService appService;
    @Autowired
    private AliSmsTemplateService aliSmsTemplateService;
    @Autowired
    private AliSmsRecordService aliSmsRecordService;
    
	/**
	 * aliSms 发送短信
	 */
	public EnumRespCode sendAliSms(String smsType, String phone, String jsonParams, String storeId) {
		EnumRespCode respCode;
		if (ValidUtils.isPhone(phone)) {
			AliSmsTemplateEntity template= aliSmsTemplateService.findOneBySmsType(smsType);
			if (template != null) {
				AliSmsRecordEntity entity = new AliSmsRecordEntity();
				entity.setAppId("wenwenZhushou");
				entity.setAliSmsId(template.getAliSmsId());
				entity.setAliSignName(template.getAliSignName());
				entity.setSmsType(template.getSmsType());
				entity.setImmediate(true);
				entity.setPhone(phone);
				entity.setJsonParams(jsonParams);
				entity.setStoreId(storeId);
				aliSmsRecordService.save(entity);
				
				new Thread(new RunnableALiSMS(entity)).start();

				respCode  = EnumRespCode.SUCCESS;
			} else {
				respCode = EnumRespCode.SMSTYPE_ERROR;
			}
		} else {
			respCode = EnumRespCode.PHONE_FORMAT_ERROR;
		}
		return respCode;
	}

	/**
	 * aliSms 发送取件短信
	 */
	public EnumRespCode sendAliSmsQujian(String smsId, String smsSignName, String phone,
			String jsonParams, String storeId, String operator,boolean immediate) {
		AliSmsRecordEntity entity = new AliSmsRecordEntity();
		entity.setAppId("wenwenZhushou");
		entity.setSmsType(EnumAliSmsTemplate.QUJIAN.getValue());
		entity.setAliSmsId(smsId);
		entity.setAliSignName(smsSignName);
		entity.setPhone(phone);
		entity.setJsonParams(jsonParams);
		entity.setImmediate(immediate);
		entity.setStoreId(storeId);
		entity.setOperator(operator);
		aliSmsRecordService.save(entity);
		
		if (immediate) {
			new RunnableALiSMS(entity).run();
		}
		return EnumRespCode.SUCCESS;
	}
    
}
