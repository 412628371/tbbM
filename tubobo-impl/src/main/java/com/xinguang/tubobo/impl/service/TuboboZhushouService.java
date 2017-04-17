package com.xinguang.tubobo.impl.service;

import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.common.utils.ValidUtils;
import com.hzmux.hzcms.modules.sys.service.SystemService;
import com.xinguang.tubobo.api.baseDTO.PageDTO;
import com.xinguang.tubobo.api.enums.*;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;
import com.xinguang.tubobo.impl.alisms.enums.EnumAliSmsTemplate;
import com.xinguang.tubobo.impl.alisms.service.AliSmsTemplateService;
import com.xinguang.tubobo.impl.alisms.utils.AliSmsCenterService;
import com.xinguang.tubobo.impl.entity.*;
import com.xinguang.tubobo.impl.redisCache.RedisCache;
import com.xinguang.tubobo.impl.service.*;
import com.xinguang.tubobo.impl.tuboboZhushouVo.*;
import com.xinguang.tubobo.impl.tuboboZhushouVo.ReqLanshouBatch.ReqLanshouBatchItem;
import com.xinguang.tubobo.impl.wechat.Tubobo;
import com.xinguang.tubobo.impl.wechat.TuboboUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TuboboZhushouService extends BaseService{

	@Autowired
    private EmployeeService employeeService;
	@Autowired
    private AliSmsCenterService aliSmsCenterService;
	@Autowired
	private WayBillSService wayBillSService;
	@Autowired
	private WayBillRService wayBillRService;
	@Autowired
	private ValidateCodeService validateCodeService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private UploadWayBillService uploadWayBillService;
    @Autowired 
    private AssociateMobileNumService associateMobileNumService;
    @Autowired
    private ExpressTrackService expressTrackService;
    @Autowired
    private CustomerTagService customerTagRecordService;
    @Autowired
    private AliSmsTemplateService aliSmsTemplateService;
	@Autowired
	private WaybillInfoService waybillInfoService;
	@Autowired
	private TuboboUserService tuboboUserService;

    /**
	 * 登录
	 */
    public EmployeeEntity login(String storeId,String phone,String password) throws Exception {
    	String msg = "";
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(password)|| StringUtils.isBlank(storeId)) {
        	msg = EnumRespCode.PARAMS_ERROR.getValue();
        	throw new Exception(msg);
        } else {
        	StoreEntity store = storeService.get(storeId);
        	if (null == store) {
        		msg = EnumRespCode.STORE_INFO_ERROR.getValue();
        		throw new Exception(msg);
			}else {
				EmployeeEntity entity = employeeService.getByPhone(phone);
	            if (null == entity) {
	                msg = EnumRespCode.PHONE_OR_PWD_ERROR.getValue();
	                throw new Exception(msg);
	            }else {
	                if(SystemService.validatePassword(password, entity.getPassword())) {
	                	String tokenId = IdGen.uuid();
//	                    token.setEmployeeId(entity.getEmployeeId());
//	                    token.setEmployeeType(entity.getEmployeeType());
//	                    token.setStoreId(storeId);

	                    entity.setTokenId(tokenId);
	                    //将token过期时间设置为晚上23:59:59
	                    entity.setTokenIdExpiredTime(DateUtils.getTodayEnd());
//	                    entity.setTokenIdExpiredTime(DateUtils.addMinutes(new Date(), 2));
	                    entity.setSignStore(store);
	                    //将token信息更新到用户表
	                    employeeService.updateTokenInfo(entity);
	                    
	                    //保存登录日志
	                    loginLogService.saveLoginLog(tokenId, storeId, store.getStoreName(),
	                    		entity.getId(), entity.getEmployeeType(), phone);
	                    
	                    msg = tokenId;
	                    return entity;
	                } else {
	                    msg = EnumRespCode.PHONE_OR_PWD_ERROR.getValue();
	                    throw new Exception(msg);
	                }
	            }
			}
        }
    }
    
    /**
     * 发送短信验证码
     */
    public EnumRespCode sendValidateCode(String phone,String type){
    	if (!ValidUtils.isPhone(phone)) {
			return EnumRespCode.PHONE_FORMAT_ERROR;
		}
    	//判断用户是否存在
    	EmployeeEntity employee = employeeService.getByPhone(phone);
    	if (ValidateCodeEntity.TYPE_REGISTER.equals(type)) {
			//注册
    		if (null != employee) {
            	return EnumRespCode.PHONE_EXIST;
            }
		}else if (ValidateCodeEntity.TYPE_FINDPASSWORD.equals(type)) {
			//找回密码
			if (null == employee) {
	        	return EnumRespCode.PHONE_NOT_EXIST;
	        }
		}else {
			return EnumRespCode.PARAMS_ERROR;
		}
    	//保存短信验证码
    	String validateCode = validateCodeService.saveValidateCode(phone, type);
    	
    	//发送短信
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("code",validateCode);
		return aliSmsCenterService.sendAliSms(type, phone, JSONObject.toJSONString(map),"");
    }
    
    /**
	 * 注册
	 */
    public EnumRespCode register(String employeeType,String belongStore,String phone,String name,String password,String code) {
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(password) || StringUtils.isBlank(employeeType)
        		|| StringUtils.isBlank(belongStore)|| StringUtils.isBlank(code)) {
        	return EnumRespCode.PARAMS_ERROR;
        }
        StoreEntity store = storeService.get(belongStore);
    	if (null == store) {
    		return EnumRespCode.STORE_INFO_ERROR;
		}
    	EmployeeEntity entity = employeeService.getByPhone(phone);
        if (null != entity) {
        	return EnumRespCode.PHONE_EXIST;
        }
        //验证验证码
		EnumRespCode result = validateCodeService.checkValidateCode(phone, ValidateCodeEntity.TYPE_REGISTER, code);
        if (result != EnumRespCode.SUCCESS) {
			return result;
		}
        //保存用户
        entity = new EmployeeEntity();
        // 0 为直营快递员 其他为兼职快递员
    	entity.setEmployeeType("0".equals(employeeType)?EnumEmployeeType.EMPLOYEE.getValue():EnumEmployeeType.PARTTIMEJOB.getValue());
    	entity.setBelongStore(store);
    	entity.setPhone(phone);
    	entity.setName(name);
    	entity.setPassword(password);
    	return employeeService.saveOrUpdate(entity);
    }
    
    /**
     * 找回密码
     */
    public EnumRespCode findPassword(String phone,String password,String code){
    	if(StringUtils.isBlank(phone) || StringUtils.isBlank(password) || StringUtils.isBlank(code)) {
        	return EnumRespCode.PARAMS_ERROR;
        }
    	//判断手机号存不存在
        EmployeeEntity entity = employeeService.getByPhone(phone);
        if (null == entity) {
        	return EnumRespCode.PHONE_NOT_EXIST;
        }
    	//验证验证码
		EnumRespCode result = validateCodeService.checkValidateCode(phone, ValidateCodeEntity.TYPE_FINDPASSWORD, code);
		if (result != EnumRespCode.SUCCESS) {
			return result;
		}
        //更新密码
    	int i = employeeService.updatePassword(entity.getId(), SystemService.entryptPassword(password));
    	if (i > 0) {
    		return EnumRespCode.SUCCESS;
		}else {
			return EnumRespCode.FAIL;
		}
    }

    /**
     * 修改密码
     */
    public EnumRespCode resetPassword(EmployeeEntity entity,String newpassword,String oldpassword){
        if(null == entity || StringUtils.isBlank(newpassword) || StringUtils.isBlank(oldpassword)) {
            return EnumRespCode.PARAMS_ERROR;
        }
        if(SystemService.validatePassword(oldpassword, entity.getPassword())) {
            //更新密码
            int i = employeeService.updatePassword(entity.getId(), SystemService.entryptPassword(newpassword));
            if (i > 0) {
                return EnumRespCode.SUCCESS;
            }else {
                return EnumRespCode.FAIL;
            }
        } else {
            return EnumRespCode.ORIGIN_PWD_ERROR;
        }
    }
    
    /**
     * 运单手机号修改
     */
    public EnumRespCode waybillPhoneModify(AuthToken token, String waybillNo, String phone){
    	if (StringUtils.isBlank(waybillNo)) {
			return EnumRespCode.WAYBILLNO_IS_BLANK;
    	}
    	if (!ValidUtils.isPhoneOrTel(phone)) {
    		return EnumRespCode.PHONE_FORMAT_ERROR;
		}
        String userId = "";
        TuboboUserEntity user = tuboboUserService.findByPhone(phone);
        if (user != null) {
            userId = user.getUserId();
        }
    	int i = wayBillSService.updateReceiverPhoneByWayBillNo(waybillNo,phone,userId,token.getEmployee().getId());
		if (i > 0){
			return EnumRespCode.SUCCESS;
		}else {
			return EnumRespCode.WAYBILL_NOT_FOUND;
		}
    }
    
    /**
     * 运单签收
     */
    public EnumRespCode waybillSign(AuthToken token,List<ReqSign.ReqSignItem> waybills){
    	if (null != waybills && waybills.size() > 0) {
    		int i = 0;
    		for (ReqSign.ReqSignItem waybillItem : waybills) {
    			if (StringUtils.isBlank(waybillItem.getWaybillNo()) || StringUtils.isBlank(waybillItem.getReceiverPhone())) {
					continue;
				}
                TuboboUserEntity user = null;
                String userId = "";
    			String openId = "";
    			WayBillSEntity exist = wayBillSService.findByWayBillNo(waybillItem.getWaybillNo());
    			if (exist != null) {
    				// 已签收的配送件 不允许重复签收
        			if (EnumDeliveryType.PEISONG.getValue().equals(exist.getPickupType())
        					&& exist.getWaybillStatus() == EnumWaybillStatus.SIGNED.getValue()) {
        				continue;
    				}
    				userId = exist.getNormalUserId();
    				if (StringUtils.isNotBlank(userId)){
                        user = tuboboUserService.findByUserId(userId);
                    } else {
                        user = tuboboUserService.findByPhone(waybillItem.getReceiverPhone());
                    }
                    if (user != null){
                        userId = user.getUserId();
                        openId = user.getOpenId();
                    }

        			wayBillSService.signByWaybillNo(exist.getWaybillNo(),userId,token.getEmployee());

        			if(exist.getEvaluation() != null){
                       openId = "";
                    }
				} else {
					user = tuboboUserService.findByPhone(waybillItem.getReceiverPhone());
					if (user != null) {
						userId = user.getUserId();
						openId = user.getOpenId();
					}
                    exist = wayBillSService.saveBySign(waybillItem.getWaybillNo(),waybillItem.getReceiverPhone(),userId, token.getStore(), token.getEmployee());
				}

                expressTrackService.save(exist.getId(), EnumExpressTrack.SIGN.getValue(), new Date(),token.getStore(), token.getEmployee());
                i++;

    			// 保存用户标签和评价
    			if (StringUtils.isNotBlank(waybillItem.getUserTags())) {
    				customerTagRecordService.save(token.getStore(), token.getEmployee(), waybillItem.getWaybillNo(), waybillItem.getReceiverPhone(),
    						waybillItem.getUserTags(), waybillItem.getComment());
				}

				// 微信推送 签收后 通知用户服务评价
				if (StringUtils.isNotBlank(openId)){
                    TuboboUtils.pushEvaluateNoticeAfterSign(openId,waybillItem.getWaybillNo(), Tubobo.tubobo_expressDetail_url + exist.getId());
                }
			}
    		String result = "签收" + i + "件运单";
    		logger.info("{} {} {}",token.getStore().getStoreName(),token.getEmployee().getName(),result);
			if (i > 0){
    			return EnumRespCode.SUCCESS;
			}else {
	    		return EnumRespCode.FAIL;
			}
    	}else {
    		return EnumRespCode.UPLOAD_WAYBILL_IS_NULL;
		}
    }

    /**
     * 运单退回处理
     */
    public EnumRespCode waybillReturnBack(AuthToken token,List<UploadWayBillEntity> waybills){
    	if (null != waybills && waybills.size() > 0) {
    		int i = 0;
    		for (UploadWayBillEntity waybill : waybills) {
    			if (StringUtils.isBlank(waybill.getWaybillNo())) {
					continue;
				}
    			WayBillSEntity exist = wayBillSService.findByWayBillNo(waybill.getWaybillNo());
    			if (exist != null) {
    				// 已签收的配送件 不允许退回
        			if (EnumDeliveryType.PEISONG.getValue().equals(exist.getPickupType())
        					&& exist.getWaybillStatus() == EnumWaybillStatus.SIGNED.getValue()) {
        				continue;
    				}
    				wayBillSService.markReturnBack(waybill.getWaybillNo(),waybill.getReturnBackReason(),token.getEmployee().getId(),waybill.getFromRemainFlag(),waybill.getRemainReason());
    				expressTrackService.save(exist.getId(), EnumExpressTrack.RETURNBACK.getValue(), new Date(), token.getStore(),token.getEmployee());
    				i++;
    			} else {
    				WayBillSEntity returnBack = wayBillSService.findByWayBillNo(StringUtils.formartWaybillNoToReturnBack(waybill.getWaybillNo()));
    				if (returnBack == null) {
    					wayBillSService.saveReturnBack(waybill.getWaybillNo(), waybill.getReturnBackReason(),token.getStore(), token.getEmployee(),waybill.getFromRemainFlag(),waybill.getRemainReason());
    					i++;
					}
				}
    		}
    		String result = "退回" + i + "件运单";
    		logger.info("{} {} {}",token.getStore().getStoreName(),token.getEmployee().getName(),result);
			if (i > 0){
				return EnumRespCode.SUCCESS;
			}else {
				return EnumRespCode.FAIL;
			}
    	}else {
    		return EnumRespCode.UPLOAD_WAYBILL_IS_NULL;
    	}
    }
    
    
    /**
     * 新到件入库
     */
    public EnumRespCode newArrived(AuthToken token, List<ReqNewArrived.Data> list) {
        Date current = new Date();
        
        int count = 0;
        for (ReqNewArrived.Data data : list) {
        	if (StringUtils.isBlank(data.getWaybillNo()) || StringUtils.isBlank(data.getExpressCompanyId())) {
				continue;
			}
            WayBillSEntity dbentity = wayBillSService.findByWayBillNo(data.getWaybillNo());
            if(null == dbentity) {
                WayBillSEntity e = new WayBillSEntity();
                e.setWaybillNo(data.getWaybillNo());
                e.setExpressCompany(new ExpressCompanyEntity(data.getExpressCompanyId()));
                e.setBelongStore(token.getStore());
                e.setWaybillStatus(EnumWaybillStatus.IN.getValue());
                e.setRemainStatus(0);
                e.setInOperator(token.getEmployee());
                e.setInTime(current);
                e.setCreateBy(token.getEmployee().getId());
                e.setCreateDate(current);
                wayBillSService.save(e);
                count++;
            } else{
                dbentity.setBelongStore(token.getStore());
                dbentity.setWaybillNo(data.getWaybillNo());
                dbentity.setExpressCompany(new ExpressCompanyEntity(data.getExpressCompanyId()));
                dbentity.setWaybillStatus(EnumWaybillStatus.IN.getValue());
                dbentity.setUpdateDate(current);
                wayBillSService.save(dbentity);
                count++;
            }
        }
        String result = "录入" + count + "件新到件";
		logger.info("{} {} {}",token.getStore().getStoreName(),token.getEmployee().getName(),result);
		if (count > 0){
			return EnumRespCode.SUCCESS;
		}else {
			return EnumRespCode.FAIL;
		}
    }
    
    /**
     * 运单上传
     */
    public EnumRespCode saveUploadWayBill(AuthToken token,int uploadType,boolean sendFlag,String smsModelId,List<UploadWayBillEntity> waybills){
		if (UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZITI != uploadType &&
				UploadWayBillEntity.WAYBILL_UPLOADTYPE_PEISONG != uploadType &&
				UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZHILIU != uploadType) {
			return EnumRespCode.WAYBILL_UPLOADTYPE_ERROR;
		}
		if (null == waybills || waybills.size() == 0) {
			return EnumRespCode.UPLOAD_WAYBILL_IS_NULL;
		}
		AliSmsTemplateEntity template = new AliSmsTemplateEntity();
		if (uploadType != UploadWayBillEntity.WAYBILL_UPLOADTYPE_PEISONG) {
			//验证阿里短信模版
			template = aliSmsTemplateService.get(smsModelId);
			if (template == null) {
				template = RedisCache.queryAliSmsTemplate(EnumAliSmsTemplate.QUJIAN_DEFAULT.getValue());
				if (template == null) {
					return EnumRespCode.SMS_MODEL_ERROR;
				}
			}
		}
		return uploadWayBillService.saveUploadInfo(token.getStore(),token.getEmployee(),uploadType,sendFlag,
				template.getAliSmsId(),template.getAliSignName(),waybills);
    }

    /**
     * 揽收件入库
     * @param token
     * @param params
     */
    public EnumRespCode saveLanshouWaybill(AuthToken token, ReqLanshou params) {
    	if (StringUtils.isBlank(params.getReceiverPcdName()) 
    			|| StringUtils.isBlank(params.getSenderPhone()) || StringUtils.isBlank(params.getReceiverPhone())
    			|| StringUtils.isBlank(params.getExpressCompanyId()) || StringUtils.isBlank(params.getWaybillNo())
    			|| params.getWeight() < 0 || params.getPayFreight() < 0) {
			return EnumRespCode.PARAMS_ERROR;
		}
		WayBillREntity exist = wayBillRService.findByWaybillNo(params.getWaybillNo());
		//检查运单号是否已录入
		if (exist != null) {
			return EnumRespCode.WAYBILLNO_EXIST;
		}
		//是否可用面单，暂时不处理
		//检查运单号是否在面单管理中可用
//		if(token.getEmployee()!=null&&!waybillInfoService.useWaybillNoSuc(params.getWaybillNo(), token.getEmployee().getId(), token.getEmployee().getName())){
//			return EnumRespCode.WAYBILLNO_DISABLED;
//		}
		boolean isAppointTask = false;
		WayBillREntity entity;
		if (StringUtils.isNotBlank(params.getId())){
			entity = wayBillRService.get(params.getId());
			if (entity == null) {
				return EnumRespCode.INFO_ERROR;
			}
			isAppointTask = true;
		}else {
			entity = new WayBillREntity();
		}
		
		entity.setWaybillStatus(EnumWaybillStatus.IN.getValue());
		entity.setTaskStatus(EnumTaskStatus.FINISH.getValue());
        entity.setInTime(new Date());
        entity.setInOperator(token.getEmployee());
        entity.setBelongStore(token.getStore());
        entity.setExpressCompany(new ExpressCompanyEntity(params.getExpressCompanyId()));
        entity.setWaybillNo(params.getWaybillNo());
        entity.setPayFreight(params.getPayFreight());
        entity.setWeight(params.getWeight());
        if (StringUtils.isNotBlank(params.getCustomerType())){
			entity.setCustomerType(params.getCustomerType());
		}else {
			entity.setCustomerType(EnumCustomerType.GENERAL.getValue());
		}
        entity.setSenderName(params.getSenderName());
        entity.setSenderPhone(params.getSenderPhone());
        entity.setSenderPcdCode(params.getSenderPcdCode());
        entity.setSenderPcdName(params.getSenderPcdName());
        entity.setSenderAddress(params.getSenderAddress());

        entity.setReceiverName(params.getReceiverName());
        entity.setReceiverPhone(params.getReceiverPhone());
        entity.setReceiverPcdCode(params.getReceiverPcdCode());
        entity.setReceiverPcdName(params.getReceiverPcdName());
        entity.setReceiverAddress(params.getReceiverAddress());
        entity.setInsureFlag(params.getInsureFlag());
        entity.setInsurePrice(params.getInsurePrice());
        entity.setPayMethod(params.getPayMethod());

        return wayBillRService.saveLanshou(entity,token.getStore().getStoreName(),token.getEmployee().getId(),token.getEmployee().getName(),isAppointTask);
    }
    
    public EnumRespCode batchSaveLanshouWaybill(AuthToken token, ReqLanshouBatch param){
    	List<ReqLanshouBatchItem> receiverList = param.getWaybills();
    	if(param == null || receiverList==null
    			||receiverList.size()==0 || StringUtils.isBlank(param.getSenderName())
    			||StringUtils.isBlank(param.getSenderPhone())){
    		return EnumRespCode.PARAMS_ERROR;
    	}
    	return wayBillRService.batchSaveLanshouWaybill(token.getStore(),token.getEmployee(),param);
    }

    /**
     * 揽收任务列表
     */
    public PageDTO<ResWaybillR> lanshouTaskList(int pageNo, int pageSize, EmployeeEntity employee, String taskStatus){
    	List<ResWaybillR> voList = new ArrayList<>();
    	Page<WayBillREntity> page = wayBillRService.findTaskPage(pageNo, pageSize, employee, taskStatus);
    	if (page.getList() != null && page.getList().size() > 0) {
			for (WayBillREntity entity : page.getList()) {
				ResWaybillR vo = new ResWaybillR();
				BeanUtils.copyProperties(entity, vo);
				voList.add(vo);
			}
		}
    	return new PageDTO<ResWaybillR>(pageNo, pageSize, page.getCount(), voList);
    }

    /**
     * 返回相应的手机号联想结果
     * @param token
     * @param key
     * @return
     */
    public List<String> associateMobileNum(AuthToken token, String key) {
        String storeId = token.getStore().getStoreId();
        return associateMobileNumService.associateMobileNum(storeId, key);
    }
}
