package com.xinguang.tubobo.impl.runnable;

import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.utils.SpringContextHolder;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.enums.EnumDeliveryType;
import com.xinguang.tubobo.api.enums.EnumExpressTrack;
import com.xinguang.tubobo.api.enums.EnumWaybillStatus;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;
import com.xinguang.tubobo.impl.alisms.enums.EnumAliSmsTemplate;
import com.xinguang.tubobo.impl.alisms.utils.AliSmsCenterService;
import com.xinguang.tubobo.impl.entity.TuboboUserEntity;
import com.xinguang.tubobo.impl.entity.UploadWayBillEntity;
import com.xinguang.tubobo.impl.entity.WayBillSEntity;
import com.xinguang.tubobo.impl.redisCache.RedisCache;
import com.xinguang.tubobo.impl.service.ExpressTrackService;
import com.xinguang.tubobo.impl.service.TuboboUserService;
import com.xinguang.tubobo.impl.service.UploadWayBillService;
import com.xinguang.tubobo.impl.service.WayBillSService;
import com.xinguang.tubobo.impl.wechat.Tubobo;
import com.xinguang.tubobo.impl.wechat.TuboboUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;

/**
 * 根据已保存的上传信息处理运单 线程
 * 
 * @author oU_Young
 * @2016年9月16日
 */
public class RunnableSaveWayBill implements Runnable {

	protected static Logger logger = LoggerFactory.getLogger(RunnableSaveWayBill.class);
	
	private static UploadWayBillService uploadWayBillService = SpringContextHolder.getBean(UploadWayBillService.class);
	private static WayBillSService wayBillSService = SpringContextHolder.getBean(WayBillSService.class);
	private static ExpressTrackService expressTrackService = SpringContextHolder.getBean(ExpressTrackService.class);
	private static AliSmsCenterService aliSmsCenterService = SpringContextHolder.getBean(AliSmsCenterService.class);
	private static TuboboUserService tuboboUserService = SpringContextHolder.getBean(TuboboUserService.class);

	private String recordId;

	public RunnableSaveWayBill(String recordId) {
		this.recordId = recordId;
	}

	@Override
	public void run() {
		try {
			UploadWayBillEntity info = uploadWayBillService.get(recordId);
			if (null == info){
				return;
			} 
			// 手机号、区域号不能为空
			if (StringUtils.isNotBlank(info.getReceiverPhone()) && StringUtils.isNotBlank(info.getAreaNum())) {

				// 查询运单号
				WayBillSEntity exist = wayBillSService.findByWayBillNo(info.getWaybillNo());
				//已签收的配送件不允许更改操作 added on 2017-03-19
				if (exist != null && EnumDeliveryType.PEISONG.getValue().equals(exist.getPickupType())
						&& exist.getWaybillStatus() == EnumWaybillStatus.SIGNED.getValue()){
					uploadWayBillService.deleteById(recordId);
					return;
				}
				// 查询用户信息
				String userId = "";
				String openId = "";
				TuboboUserEntity user = tuboboUserService.findByPhone(info.getReceiverPhone());
				if (user != null) {
					userId = user.getUserId();
					openId = user.getOpenId();
				}

				WayBillSEntity newWaybill ;
				// 如果运单未录入，则新增运单；否则更新运单信息
				if (exist == null) {
					// 新增运单
					newWaybill = wayBillSService.saveByUploadInfo(userId, info);
					// 入库 插入运单追踪信息
					expressTrackService.save(newWaybill.getId(), EnumExpressTrack.IN.getValue(), info.getCreateDate(),info.getStore(),info.getEmployee());
				}else {
					newWaybill = exist;
					// 更新运单 userId、手机号、位置号、运单状态等
					wayBillSService.updateByUploadInfo(userId, info);
				}
				
				// 出库 插入运单追踪信息
				if (info.isSendFlag()) {
	            	expressTrackService.save(newWaybill.getId(), EnumExpressTrack.OUT.getValue(),new Date(),info.getStore(),info.getEmployee());
				}
				// 出库 插入运单追踪信息
				if (info.isDelayFlag()) {
					expressTrackService.save(newWaybill.getId(), EnumExpressTrack.DELAY.getValue(),new Date(),info.getStore(),info.getEmployee());
				}
				
				String expressCompanyName = RedisCache.queryExpressCompanyName(info.getExpressCompanyId());
				// 发送取件短信
				if (info.isSmsFlag()) {
					// 如果运单已录入 且 手机号、位置号相同 则不发送短信
					if (exist != null
							&& info.getReceiverPhone().equals(exist.getReceiverPhone())
							&& info.getAreaNum().equals(exist.getAreaNum())) {
						
					} else {
						// 判断短信模版 
						// 如果是未录入的滞留件，则用默认的取件模版
						if (exist == null && info.isDelayFlag()) {
							AliSmsTemplateEntity aliSmsTemplateEntity = RedisCache.queryAliSmsTemplate(EnumAliSmsTemplate.QUJIAN_DEFAULT.getValue());
							info.setSmsModel(aliSmsTemplateEntity.getAliSmsId());
							info.setSmsSignName(aliSmsTemplateEntity.getAliSignName());
						}
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("code", info.getAreaNum());
						map.put("address", info.getStore().getStoreAddressDetail());
						map.put("expressName", expressCompanyName);
						map.put("waybllNo", StringUtils.formartWayBillNo(info.getWaybillNo()));
						// 发送短信
						aliSmsCenterService.sendAliSmsQujian(info.getSmsModel(), info.getSmsSignName(), info.getReceiverPhone(), JSONObject.toJSONString(map),
								info.getStore().getStoreId(), info.getEmployee().getId(), info.isDelayFlag() ? false:true);
					}
				}
				// 如果为正常件 且用户已绑定公众号 推送微信消息
				if (StringUtils.isNotBlank(openId) && !info.isDelayFlag()) {
					if (info.getUploadType() == UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZITI) {
						TuboboUtils.pushZitiNotice(openId, info.getWaybillNo(), expressCompanyName, info.getAreaNum(), info.getStore().getStoreName(), info.getStore().getStoreAddressDetail(),Tubobo.tubobo_expressDetail_url + newWaybill.getId());
					}
//					else if (info.getUploadType() == Constants.WAYBILL_UPLOADTYPE_PEISONG) {
//						TuboboUtils.pushDaodianPeisongNotice(openId, info.getWaybillNo(), expressCompanyName, info.getStore().getStoreName(), info.getStore().getStoreAddressDetail(),Tubobo.tubobo_expressList_url);
//
//					}
				}
			} else {
				logger.error("upload waybillInfo error:{}",info.toString());
			}
			//删除记录
			uploadWayBillService.deleteById(recordId);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}
	
}
