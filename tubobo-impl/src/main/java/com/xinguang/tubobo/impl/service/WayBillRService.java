package com.xinguang.tubobo.impl.service;

import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.common.utils.ValidUtils;
import com.xinguang.tubobo.api.baseDTO.PageDTO;
import com.xinguang.tubobo.api.enums.*;
import com.xinguang.tubobo.impl.alisms.enums.EnumAliSmsTemplate;
import com.xinguang.tubobo.impl.alisms.utils.AliSmsCenterService;
import com.xinguang.tubobo.impl.dao.WayBillRDAO;
import com.xinguang.tubobo.impl.entity.*;
import com.xinguang.tubobo.impl.redisCache.RedisCache;
import com.xinguang.tubobo.impl.tuboboZhushouVo.ReqAppointTask;
import com.xinguang.tubobo.impl.tuboboZhushouVo.ReqLanshouBatch;
import com.xinguang.tubobo.impl.tuboboZhushouVo.ResAppointTask;
import com.xinguang.tubobo.impl.wechat.Tubobo;
import com.xinguang.tubobo.impl.wechat.TuboboUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class WayBillRService extends BaseService{
    
    @Autowired
    private WayBillRDAO wayBillRDAO;
    @Autowired
    private AliSmsCenterService aliSmsCenterService;
	@Autowired
	private EvaluationService evaluationService;
	@Autowired
    private TuboboUserService tuboboUserService;
	@Autowired
    private EmployeeService employeeService;

    /**
     * 揽收入库
     */
    public EnumRespCode saveLanshou(WayBillREntity waybill,String storeName,String operatorId,String operatorName,boolean isAppointTask) {
    	String openId = "";
    	TuboboUserEntity user;
		if (isAppointTask){

			user = tuboboUserService.findByUserId(waybill.getNormalUserId());
			if (user != null) {
				openId = user.getOpenId();
			}
			if (EnumPaymentMethod.ONLINE.getValue().equals(waybill.getPayMethod())){
				waybill.setPayStatus(EnumPayStatus.UNPAY.getValue());
				//TODO 微信通知支付
			}
            waybill.setUpdateBy(operatorId);
			waybill.setUpdateDate(new Date());

			wayBillRDAO.save(waybill);

		}else {
			user = tuboboUserService.findByPhone(waybill.getSenderPhone());
			if (user != null) {
				openId = user.getOpenId();
				waybill.setNormalUserId(user.getUserId());
			}
			waybill.setCreateBy(operatorId);
			save(waybill);
		}

    	logger.info("{} {} 揽件入库 waybillNo:{} ",storeName,operatorName,waybill.getWaybillNo());
        
        // 发送寄件回执短信
        if (ValidUtils.isPhone(waybill.getSenderPhone())) {
            String expressCompanyName = RedisCache.queryExpressCompanyName(waybill.getExpressCompany().getCompanyId());
//            您寄往『${address}』的${expressName}快递${waybllNo}，重量${weight}，价格${price}，感谢您选择兔波波。
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("address", waybill.getReceiverPcdName());
            map.put("expressName", expressCompanyName);
            map.put("waybllNo", waybill.getWaybillNo());
            map.put("weight",new BigDecimal(waybill.getWeight()).setScale(2, RoundingMode.HALF_UP).toString());
            map.put("price",new BigDecimal(waybill.getPayFreight()).setScale(2, RoundingMode.HALF_UP).toString());
            // 发送短信
            aliSmsCenterService.sendAliSms(EnumAliSmsTemplate.JIJIAN.getValue(), waybill.getSenderPhone(), JSONObject.toJSONString(map),waybill.getBelongStore().getStoreId());
        }
        
        // 微信推送
        if (StringUtils.isNotBlank(openId)) {
            // 收件成功后 通知用户服务评价
			TuboboUtils.pushEvaluateNoticeAfterRuku(openId, waybill.getWaybillNo(), Tubobo.tubobo_appointTaskDetail_url+waybill.getId());
            // 通知用户进行付款
            if (EnumPaymentMethod.ONLINE.getValue().equals(waybill.getPayMethod())){
                TuboboUtils.pushPayNotice(openId,waybill.getWaybillNo(),waybill.getPayFreight().toString(),Tubobo.tubobo_appointTaskDetail_url+waybill.getId());
            }
		}
        return EnumRespCode.SUCCESS;
    }

    /**
     * 批量揽收入库
     */
    public EnumRespCode batchSaveLanshouWaybill(StoreEntity store,EmployeeEntity employee, ReqLanshouBatch params){
    	String userId = "";
    	int saveSum = 0;
    	BigDecimal freightSum = BigDecimal.ZERO;
    	if (StringUtils.isNotBlank(params.getSenderPhone())) {
    		TuboboUserEntity user = tuboboUserService.findByPhone(params.getSenderPhone());
        	if (user != null) {
        		userId = user.getUserId();
        	}
		}
    	for(ReqLanshouBatch.ReqLanshouBatchItem item :params.getWaybills()){
    		WayBillREntity exist = findByWaybillNo(item.getWaybillNo());
    		//检查运单号是否已录入
    		if (exist != null) {
    			continue;
    		}
    		
    		//TODO mark 由于面单管理暂时未启用，这里暂时不做校验
//    		if(findByWaybillNo(item.getWaybillNo())!=null){
//    			existNumber++;
//    			continue;
//    		}
    		WayBillREntity entity = new WayBillREntity();
    		
    		entity.setWaybillStatus(EnumWaybillStatus.IN.getValue());
			entity.setTaskStatus(EnumTaskStatus.FINISH.getValue());
    		entity.setInTime(new Date());
            
    		entity.setNormalUserId(userId);
            entity.setBelongStore(store);
            entity.setInOperator(employee);
            entity.setExpressCompany(new ExpressCompanyEntity(item.getExpressCompanyId()));
            entity.setWaybillNo(item.getWaybillNo());
            entity.setPayFreight(item.getPayFreight());
            entity.setWeight(item.getWeight());
            entity.setSenderPhone(params.getSenderPhone());
            entity.setSenderName(params.getSenderName());
            entity.setSenderPcdCode(params.getSenderPcdCode());
            entity.setSenderPcdName(params.getSenderPcdName());
            entity.setSenderAddress(params.getSenderAddress());
            
            entity.setReceiverName(item.getReceiverName());
            entity.setReceiverPhone(item.getReceiverPhone());
            entity.setReceiverPcdCode(item.getReceiverPcdCode());
            entity.setReceiverPcdName(item.getReceiverPcdName());
            entity.setReceiverAddress(item.getReceiverAddress());
            entity.setCustomerType(EnumCustomerType.BIGCLIENT.getValue());
			entity.setInsureFlag(item.getInsureFlag());
			entity.setInsurePrice(item.getInsurePrice());
            save(entity);
            freightSum = freightSum.add(new BigDecimal(item.getPayFreight()));
            saveSum++;
    	}
    	freightSum = freightSum.setScale(2, RoundingMode.HALF_UP);
    	logger.info("{} {} 大客户揽件入库 {}件，{}元 ",store.getStoreName(),employee.getName(),saveSum,freightSum);
    	
        if (ValidUtils.isPhone(params.getSenderPhone())) {
        	//您本次寄件共计${data}件，价格${code}，感谢您选择兔波波。
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("data", String.valueOf(saveSum));
        	map.put("code", freightSum.toString());
        	aliSmsCenterService.sendAliSms(EnumAliSmsTemplate.JIJIAN_BATCH.getValue(), params.getSenderPhone(), JSONObject.toJSONString(map), store.getStoreId());
        }
        if (saveSum > 0){
	    	return EnumRespCode.SUCCESS;
		}else {
	    	return EnumRespCode.FAIL;
		}
    }
    
    public String save(WayBillREntity entity){
    	String id = IdGen.uuid();
    	entity.setId(id);
    	entity.setCreateDate(new Date());
    	entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
    	wayBillRDAO.save(entity);
    	return id;
    }

    public WayBillREntity get(String id){
    	return wayBillRDAO.get(id);
    }
    
    public WayBillREntity findByWaybillNo(String waybillNo){
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("waybillNo").equal(waybillNo);
    	return wayBillRDAO.findOne(q);
    }

    public List<WayBillREntity> findToExportExcel(WayBillREntity entity) throws Exception{
        Query<WayBillREntity> q = wayBillRDAO.createQuery();
        if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        if(null != entity.getWaybillStatus()) {
            q.field("waybillStatus").equal(entity.getWaybillStatus());
        }
        if(StringUtils.isNotBlank(entity.getSenderPhone())) {
        	q.field("senderPhone").contains(Pattern.quote(entity.getSenderPhone()));
        }
        if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
            q.field("expressCompany").equal(entity.getExpressCompany());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        q.order("-createDate");
        long count = q.countAll();
        if (count > 10000) {
			throw new Exception("失败：批量导出数量不允许超过10000条");
		}else if (count < 1) {
			throw new Exception("失败：符合条件的数据量为0");
		}
        return q.asList();
    }

	public long countRukuWaybillNum(String storeId,Date begin,Date end) {
		Query<WayBillREntity> q = wayBillRDAO.createQuery();
		q.field("belongStore").equal(new StoreEntity(storeId));
		q.field("waybillStatus").equal(EnumWaybillStatus.IN.getValue());
		q.field("inTime").greaterThanOrEq(begin);
		q.field("inTime").lessThanOrEq(end);
		return q.countAll();
	}
	
	/**
	 * 后台揽件入库分页
	 */
    public Page<WayBillREntity> find(Page<WayBillREntity> page,WayBillREntity entity) {
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	if(StringUtils.isNotBlank(entity.getWaybillNo())) {
            q.field("waybillNo").contains(Pattern.quote(entity.getWaybillNo()));
        }else {
            if(null != entity.getCreateDate()) {
                q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
            }
            if(null != entity.getUpdateDate()) {
            	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
            }
		}
    	//只显示已入库的记录 不显示任务
    	q.field("waybillStatus").equal(EnumWaybillStatus.IN.getValue());
//        if(null != entity.getWaybillStatus()) {
//            q.field("waybillStatus").equal(entity.getWaybillStatus());
//        }
        if(StringUtils.isNotBlank(entity.getSenderPhone())) {
        	q.field("senderPhone").contains(Pattern.quote(entity.getSenderPhone()));
        }
        if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
            q.field("expressCompany").equal(entity.getExpressCompany());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
//        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    /**
     * 后台任务管理分页
     */
    public Page<WayBillREntity> findTaskPage(Page<WayBillREntity> page,WayBillREntity entity){
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
		//只显示未入库的记录 不显示任务
		q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());
    	if(StringUtils.isNotBlank(entity.getTaskStatus())) {
            q.field("taskStatus").equal(entity.getTaskStatus());
        }else {
        	q.field("taskStatus").exists();
		}
        if(StringUtils.isNotBlank(entity.getSenderPhone())) {
        	q.field("senderPhone").contains(Pattern.quote(entity.getSenderPhone()));
        }
        if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
            q.field("expressCompany").equal(entity.getExpressCompany());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
        if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    /**
     * 问问助手任务中心分页
     */
    public Page<WayBillREntity> findTaskPage(int pageNo,int pageSize,EmployeeEntity employee,String taskStatus){
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("inOperator").equal(employee);
    	if (StringUtils.isNotBlank(taskStatus)) {
    		q.field("taskStatus").equal(taskStatus);
    	}else {
    		q.field("taskStatus").equal(EnumTaskStatus.ASSIGNED.getValue());
    	}
    	long count = q.countAll();
    	q.offset((pageNo-1)*pageSize).limit(pageSize);
    	List<WayBillREntity> list = q.asList();
    	return new Page<WayBillREntity>(pageNo, pageSize, count,list);
    }
    
	/**
	 * 用户预约任务列表
	 */
    public PageDTO<ResAppointTask> userFindPage(int pageNo, int pageSize, String normalUserId, String flag){
    	List<ResAppointTask> voList = new ArrayList<>();
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("normalUserId").equal(normalUserId);
    	if ("1".equals(flag)) {
    		q.or(q.criteria("evaluation").exists(),
//    		        q.criteria("taskStatus").equal(EnumTaskStatus.FINISH.getValue()),
					q.criteria("taskStatus").equal(EnumTaskStatus.CLOSED.getValue()),
    				q.criteria("taskStatus").equal(EnumTaskStatus.CANCELLED.getValue()));
		}else {
            q.criteria("evaluation").doesNotExist();
//			q.field("taskStatus").notEqual(EnumTaskStatus.FINISH.getValue());
			q.field("taskStatus").exists();
			q.field("taskStatus").notEqual(EnumTaskStatus.CLOSED.getValue());
			q.field("taskStatus").notEqual(EnumTaskStatus.CANCELLED.getValue());
		}
    	q.order("-createDate");
    	long count = q.countAll();
        q.offset((pageNo-1)*pageSize).limit(pageSize);
        List<WayBillREntity> list = q.asList();
        if (list != null && list.size() > 0) {
			for (WayBillREntity entity : list) {
				ResAppointTask vo = new ResAppointTask();
				transferEntityToVo(entity, vo);
				voList.add(vo);
			}
		}
        return new PageDTO<ResAppointTask>(pageNo, pageSize, count,voList);
    }
    
    /**
     * 用户评价
     */
    public EnumRespCode userEvaluate(String id,String userId,String name,String phone,String evaluationStar, String evaluationTag, String evaluationComment) {
    	WayBillREntity entity = get(id);
    	if (entity != null) {
    		EvaluationEntity evaluation = evaluationService.save(EnumEvaluationType.SEND.getValue(), id,entity.getWaybillNo(), userId, name, phone,
    				evaluationStar, evaluationTag, evaluationComment);
    		
    		UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    		ops.set("evaluationFlag", true);
    		ops.set("evaluation", evaluation);
    		
    		Query<WayBillREntity> q = wayBillRDAO.createQuery();
    		q.field("id").equal(id);
    		q.field("normalUserId").equal(userId);
			q.field("evaluation").doesNotExist();
    		int i = wayBillRDAO.update(q, ops).getUpdatedCount();
    		if (i > 0) {
    			return EnumRespCode.SUCCESS;
    		}else {
    			return EnumRespCode.FAIL;
    		}
		}else {
			return EnumRespCode.INFO_ERROR;
		}
    }
    
    /**
     * 用户预约寄件
     */
    public EnumRespCode userAppoint(String userId, ReqAppointTask params) {
    	WayBillREntity entity = new WayBillREntity();
    	BeanUtils.copyProperties(params, entity);
    	entity.setNormalUserId(userId);
//    	entity.setSenderName(params.getSenderName());
//    	entity.setSenderPhone(params.getSenderPhone());
//    	entity.setSenderPcdCode(params.getSenderPcdCode());
//    	entity.setSenderPcdName(params.getSenderPcdName());
//    	entity.setSenderAddress(params.getSenderAddress());
//    	entity.setReceiverName(params.getReceiverName());
//    	entity.setReceiverPhone(params.getReceiverPhone());
//    	entity.setReceiverPcdCode(params.getReceiverPcdCode());
//    	entity.setReceiverPcdName(params.getReceiverPcdName());
//    	entity.setReceiverAddress(params.getReceiverAddress());
//    	entity.setShipment(params.getShipment());
//    	entity.setWeight(params.getWeight());
//    	entity.setRemark(params.getRemark());
//    	entity.setSource(params.getSource());
//    	entity.setInsureFlag(params.getInsureFlag());
    	entity.setCustomerType(EnumCustomerType.GENERAL.getValue());
    	entity.setWaybillStatus(EnumWaybillStatus.INIT.getValue());
    	entity.setTaskStatus(EnumTaskStatus.INIT.getValue());
    	entity.setCreateBy(userId);
    	save(entity);
    	return EnumRespCode.SUCCESS;
    }

    /**
     * 用户取消预约寄件
     */
    public EnumRespCode userCancelled(String userId, String id) {
    	UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    	ops.set("taskStatus",EnumTaskStatus.CANCELLED.getValue());
    	ops.set("updateDate",new Date());
    	
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
		q.field("id").equal(id);
		q.field("normalUserId").equal(userId);
		q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());
		int i = wayBillRDAO.update(q, ops).getUpdatedCount();
    	if (i > 0) {
    		return EnumRespCode.SUCCESS;
		}else {
			return EnumRespCode.FAIL;
		}
    }
    
    /**
     * 任务中心分配任务到门店
     * (未指派到快递员的任务都可以重新分配到门店)
     */
    public int allotToStore(List<String> idList, String storeId,String updateBy) {
    	UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    	ops.set("taskStatus",EnumTaskStatus.ALLOTED.getValue());
    	ops.set("updateBy",updateBy);
    	ops.set("updateDate",new Date());
    	ops.set("belongStore",new StoreEntity(storeId));
    	
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("id").in(idList);
    	q.field("inOperator").doesNotExist();
    	q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());
    	return wayBillRDAO.update(q, ops).getUpdatedCount();
    }
    
    /**
     * 任务中心关闭任务
     */
    public int closed(List<String> idList,String updateBy) {
    	UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    	ops.set("taskStatus",EnumTaskStatus.CLOSED.getValue());
    	ops.set("updateBy",updateBy);
    	ops.set("updateDate",new Date());
    	
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("id").in(idList);
    	q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());
    	return wayBillRDAO.update(q, ops).getUpdatedCount();
    }

    /**
     * 门店指派任务到快递员
     */
    public int assignToEmployee(List<String> idList, String employeeId,String updateBy) {
        EmployeeEntity employee = employeeService.get(employeeId);
    	UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    	ops.set("taskStatus",EnumTaskStatus.ASSIGNED.getValue());
    	ops.set("updateBy",updateBy);
    	ops.set("updateDate",new Date());
    	ops.set("inOperator",employee);
    	
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("id").in(idList);
    	q.field("belongStore").exists();
    	q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());

    	List<WayBillREntity> list = q.asList();
    	if (list != null && list.size() > 0){
    	    for (WayBillREntity entity :list){
                String openId = tuboboUserService.findOpenIdByUserIdThenPhone(entity.getNormalUserId(),entity.getSenderPhone());
                if (StringUtils.isNotBlank(openId)){
                    // 微信推送 快递员上门取件通知
                    TuboboUtils.pushShangmenQujianNotice(openId,employee.getName(),employee.getPhone(),Tubobo.tubobo_appointTaskDetail_url + entity.getId());
                }
            }
        }
    	int result = wayBillRDAO.update(q, ops).getUpdatedCount();
    	return result;
    }

    /**
     * 门店退回任务 需要任务中心重新分配
     */
    public int reallot(List<String> idList,String updateBy) {
    	UpdateOperations<WayBillREntity> ops = wayBillRDAO.createUpdateOperations();
    	ops.set("taskStatus",EnumTaskStatus.REALLOT.getValue());
    	ops.set("updateBy",updateBy);
    	ops.set("updateDate",new Date());
    	ops.unset("belongStore");
    	ops.unset("inOperator");
    	
    	Query<WayBillREntity> q = wayBillRDAO.createQuery();
    	q.field("id").in(idList);
    	q.field("belongStore").exists();
    	q.field("waybillStatus").equal(EnumWaybillStatus.INIT.getValue());
    	return wayBillRDAO.update(q, ops).getUpdatedCount();
    }
    
    
    public static ResAppointTask transferEntityToVo(WayBillREntity entity,ResAppointTask vo){
		BeanUtils.copyProperties(entity, vo);
		if (entity.getBelongStore() != null) {
			vo.setStoreName(entity.getBelongStore().getStoreName());
			vo.setStoreAddress(entity.getBelongStore().getStoreAddressDetail());
		}
		if (entity.getExpressCompany() != null) {
			vo.setExpressCompanyName(entity.getExpressCompany().getCompanyName());
		}
		if (entity.getInOperator() != null) {
			vo.setInOperatorName(entity.getInOperator().getName());
			vo.setInOperatorPhone(entity.getInOperator().getPhone());
		}
		if (entity.getEvaluationFlag() != null && entity.getEvaluationFlag()) {
			vo.setEvaluationStar(entity.getEvaluation().getEvaluationStar());
			vo.setEvaluationTag(entity.getEvaluation().getEvaluationTag());
			vo.setEvaluationComment(entity.getEvaluation().getEvaluationComment());
			vo.setEvaluationTime(entity.getEvaluation().getEvaluationTime());
		}
		return vo;
    }
}
