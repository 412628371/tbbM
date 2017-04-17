package com.xinguang.tubobo.impl.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.baseDTO.PageDTO;
import com.xinguang.tubobo.api.enums.*;
import com.xinguang.tubobo.impl.dao.WayBillSDAO;
import com.xinguang.tubobo.impl.entity.*;
import com.xinguang.tubobo.impl.tuboboZhushouVo.ResExpress;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class WayBillSService extends BaseService{
	
    @Autowired
    private WayBillSDAO wayBillSDAO;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private ExpressTrackService expressTrackService;
    @Autowired 
    private AssociateMobileNumService associateMobileNumService;
    @Autowired 
    private EvaluationService evaluationService;
    
    public WayBillSEntity save(WayBillSEntity entity){
        if(StringUtils.isBlank(entity.getId())) {
            String id = IdGen.uuid();	
            entity.setId(id);
            entity.setCreateDate(new Date());
            entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
        }
    	wayBillSDAO.save(entity);
    	return entity;
    }
    
    public WayBillSEntity get(String id){
    	if (StringUtils.isBlank(id)) {
			return null;
		}
    	return wayBillSDAO.get(id);
    }

    public WayBillSEntity findByWayBillNo(String waybillNo){
    	if (StringUtils.isBlank(waybillNo)) {
			return null;
		}
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    	q.field("waybillNo").equal(waybillNo);
    	return wayBillSDAO.findOne(q);
    }
    
    public Page<WayBillSEntity> find(Page<WayBillSEntity> page, WayBillSEntity wayBillEntity) {
        return wayBillSDAO.find(page, wayBillEntity);
    }
    
    /**
     * 根据客户端上传的数据保存为真实运单信息
     */
    public WayBillSEntity saveByUploadInfo(String normalUserId,UploadWayBillEntity info){
		WayBillSEntity waybill = new WayBillSEntity();
		waybill.setWaybillNo(info.getWaybillNo());
		if (StringUtils.isNotBlank(info.getExpressCompanyId())) {
			waybill.setExpressCompany(new ExpressCompanyEntity(info.getExpressCompanyId()));
		}
		waybill.setBelongStore(info.getStore());

		waybill.setNormalUserId(normalUserId);
		waybill.setReceiverPhone(info.getReceiverPhone());
		waybill.setCreateBy(info.getEmployee().getId());
		waybill.setWaybillStatus(EnumWaybillStatus.IN.getValue());
		waybill.setAreaNum(info.getAreaNum());
		waybill.setReturnBackReason(0);
		waybill.setInOperator(info.getEmployee());
		waybill.setInTime(info.getCreateDate());
        waybill.setFromRemainFlag(info.getFromRemainFlag());
        waybill.setRemainReason(info.getRemainReason());
		if (info.isDelayFlag()) {
			waybill.setRemainStatus(1);
			waybill.setWaybillStatus(EnumWaybillStatus.DELAY.getValue());
		} else {
			//增加配送件滞留件次数 add on 2017-03-20
			if (info.getFromRemainFlag()){
				waybill.setRemainStatus(1);
			}
			waybill.setRemainStatus(0);
		}
		if (info.isSendFlag()) {
			waybill.setOutTime(info.getCreateDate());
			waybill.setOutOperator(info.getEmployee());
			waybill.setWaybillStatus(EnumWaybillStatus.OUT.getValue());
		}
		if (info.isSmsFlag()) {
			// 自提件
			waybill.setDispatchingWay(EnumDeliveryMethod.ZIQU.getValue());
			waybill.setPickupType(EnumDeliveryType.ZITI.getValue());
		} else {
			// 配送件
			waybill.setDispatchingWay(EnumDeliveryMethod.PEISONG.getValue());
			waybill.setPickupType(EnumDeliveryType.PEISONG.getValue());
		}
		if (StringUtils.isNotBlank(info.getPayFreight())) {
			waybill.setPayFreight(Double.valueOf(info.getPayFreight()));
		}
		if (StringUtils.isNotBlank(info.getPayment())) {
			waybill.setPayment(Double.valueOf(info.getPayment()));
		}
		waybill.setUpdateDate(new Date());
		
		// add mobile number to AssociateMobileNumService
		associateMobileNumService.addMobileNumber(waybill.getBelongStore().getStoreId(), waybill.getReceiverPhone());
		
    	return save(waybill);
    }

    /**
     * 签收时运单号不存在 新增运单
     */
    public WayBillSEntity saveBySign(String waybillNo,String phone,String userId,StoreEntity store,EmployeeEntity employee){
    	WayBillSEntity waybill = new WayBillSEntity();
    	waybill.setWaybillNo(waybillNo);
    	waybill.setNormalUserId(userId);
    	waybill.setBelongStore(store);
    	waybill.setCreateBy(employee.getId());
    	waybill.setUpdateDate(new Date());

    	waybill.setReceiverPhone(phone);
    	waybill.setInOperator(employee);
    	waybill.setInTime(new Date());
    	waybill.setOutOperator(employee);
    	waybill.setOutTime(new Date());
    	waybill.setSignTime(new Date());
    	waybill.setSignOperator(employee);
    	waybill.setWaybillStatus(EnumWaybillStatus.SIGNED.getValue());
    	waybill.setRemainStatus(0);
    	waybill.setReturnBackReason(0);
    	// 默认为配送件
		waybill.setDispatchingWay(EnumDeliveryMethod.PEISONG.getValue());
		waybill.setPickupType(EnumDeliveryType.PEISONG.getValue());
    	
    	return save(waybill);
    }

    /**
     * 退回件处理时运单号不存在 新增运单
     */
    public WayBillSEntity saveReturnBack(String waybillNo,Integer returnBackReason,StoreEntity store,EmployeeEntity employee,boolean fromRemainFlag,String remainReason){
    	WayBillSEntity waybill = new WayBillSEntity();
    	waybill.setWaybillNo(StringUtils.formartWaybillNoToReturnBack(waybillNo));
    	waybill.setBelongStore(store);
    	waybill.setCreateBy(employee.getId());
		waybill.setUpdateDate(new Date());
    	
    	waybill.setInOperator(employee);
    	waybill.setInTime(new Date());
    	waybill.setWaybillStatus(EnumWaybillStatus.RETURNBACK.getValue());
    	waybill.setRemainStatus(0);
    	waybill.setReturnBackReason(returnBackReason);
    	waybill.setRemainReason(remainReason);
        waybill.setFromRemainFlag(fromRemainFlag);
    	return save(waybill);
    }
    
    /**
     * 标记为退回件
     */
    public int markReturnBack(String waybillNo,Integer returnBackReason,String updateBy,boolean isFromRemain,String remainReason){
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
        ops.set("waybillNo", StringUtils.formartWaybillNoToReturnBack(waybillNo));
        ops.set("waybillStatus", EnumWaybillStatus.RETURNBACK.getValue());
        ops.set("returnBackReason", returnBackReason);
        ops.set("updateBy", updateBy);
        ops.set("updateDate", new Date());
		ops.set("fromRemainFlag",isFromRemain);
		ops.set("remainReason",remainReason== null ? "":remainReason);
        return wayBillSDAO.update(wayBillSDAO.createQuery()
        		.field("waybillNo").equal(waybillNo), ops).getUpdatedCount();
    }

    /**
     * 根据已保存的上传信息更新运单
     */
    public int updateByUploadInfo(String normalUserId,UploadWayBillEntity info) {
        return wayBillSDAO.updateByUploadInfo(normalUserId, info);
    }

    /**
     * 快递员根据运单号签收
     */
    public int signByWaybillNo(String waybillNo,String userId, EmployeeEntity employee){
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    	ops.set("waybillStatus", EnumWaybillStatus.SIGNED.getValue());
		if (StringUtils.isNotBlank(userId)){
			ops.set("normalUserId", userId);
		}
		ops.set("signOperator", employee);
    	ops.set("signTime", new Date());
    	ops.set("updateBy", employee.getId());
    	ops.set("updateDate", new Date());
    	return wayBillSDAO.update(wayBillSDAO.createQuery()
    			.field("waybillNo").equal(waybillNo), ops).getUpdatedCount();
    }

    /**
     * 系统根据id签收
     */
    public int signBySystemAndId(String id){
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    	ops.set("waybillStatus", EnumWaybillStatus.SIGNED.getValue());
    	ops.set("signTime", DateUtils.getYesterdayEnd());
    	ops.set("updateBy", "system");
    	ops.set("updateDate", new Date());
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
		q.field("id").equal(id);
    	return wayBillSDAO.update(q, ops).getUpdatedCount();
    }
    
    /**
     * 根据运单号修改手机
     */
    public int updateReceiverPhoneByWayBillNo(String waybillNo,String phone,String userId, String updateBy){
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    	ops.set("receiverPhone", phone);
    	ops.set("normalUserId", userId);
    	ops.set("updateBy", updateBy);
    	ops.set("updateDate", new Date());
    	return wayBillSDAO.update(wayBillSDAO.createQuery()
    			.field("waybillNo").equal(waybillNo), ops).getUpdatedCount();
    }
    
    public List<WayBillSEntity> findToExportExcel(WayBillSEntity entity) throws Exception{
        Query<WayBillSEntity> q = wayBillSDAO.createQuery();
        if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        if(StringUtils.isNotBlank(entity.getDispatchingWay())) {
        	q.field("dispatchingWay").equal(entity.getDispatchingWay());
        }
        if(null != entity.getWaybillStatus()) {
            q.field("waybillStatus").equal(entity.getWaybillStatus());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
        if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
        	q.field("expressCompany").equal(entity.getExpressCompany());
        }
        if(null != entity.getInOperator() && StringUtils.isNotBlank(entity.getInOperator().getId())) {
            q.field("inOperator").equal(entity.getInOperator());
        }
        if(null != entity.getOutOperator() && StringUtils.isNotBlank(entity.getOutOperator().getId())) {
        	q.field("outOperator").equal(entity.getOutOperator());
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
    
    /**
     * 查询自提滞留件
     */
    public List<WayBillSEntity> findZitiDelayWaybills(){
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    	q.field("waybillStatus").equal(EnumWaybillStatus.DELAY.getValue());
    	q.field("pickupType").equal(EnumDeliveryType.ZITI.getValue());
    	q.field("remainStatus").greaterThan(0);
    	return wayBillSDAO.find(q).asList();
    }
    
    /**
     * 系统签收入库状态 且不为滞留件的自提件
     */
    public int signZitiWaybills(){
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    	ops.set("waybillStatus", EnumWaybillStatus.SIGNED.getValue());
    	ops.set("signTime", DateUtils.getYesterdayEnd());
    	ops.set("updateBy", "system");
    	ops.set("updateDate", new Date());
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    	q.field("waybillStatus").equal(EnumWaybillStatus.IN.getValue());
    	q.field("pickupType").equal(EnumDeliveryType.ZITI.getValue());
    	q.field("remainStatus").equal(0);
    	return wayBillSDAO.update(q, ops).getUpdatedCount();
    }
    
	public List<WayBillSEntity> countRukuList(String storeId,Date begin,Date end) {
		Query<WayBillSEntity> q = wayBillSDAO.createQuery();
		q.field("belongStore").equal(new StoreEntity(storeId));
		q.field("inTime").greaterThanOrEq(begin);
		q.field("inTime").lessThanOrEq(end);
		return q.asList();
	}

	public List<WayBillSEntity> countChukuList(String storeId,Date begin,Date end) {
		Query<WayBillSEntity> q = wayBillSDAO.createQuery();
		q.field("belongStore").equal(new StoreEntity(storeId));
		q.field("outTime").greaterThanOrEq(begin);
		q.field("outTime").lessThanOrEq(end);
		return q.asList();
	}
	
    /**
     * @param flag 0 待配送；1 配送中  2 已完成
     */
    public PageDTO<ResExpress> findPage(int pageNo, int pageSize, String normalUserId, String flag){
    	List<ResExpress> voList = new ArrayList<>();
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    	if ("0".equals(flag)) {
			q.field("evaluation").doesNotExist();
			q.or(q.criteria("waybillStatus").equal(EnumWaybillStatus.IN.getValue()),
	    			q.criteria("waybillStatus").equal(EnumWaybillStatus.DELAY.getValue()));
//    		q.field("waybillStatus").equal(EnumWaybillStatus.IN.getValue());
		}else if ("1".equals(flag)) {
			q.field("evaluation").doesNotExist();
			q.or(q.criteria("waybillStatus").equal(EnumWaybillStatus.OUT.getValue()),
					q.criteria("waybillStatus").equal(EnumWaybillStatus.SIGNED.getValue()));
//			q.field("waybillStatus").notEqual(EnumWaybillStatus.IN.getValue());
//			q.field("waybillStatus").notEqual(EnumWaybillStatus.RETURNBACK.getValue());
		}
//		else if ("2".equals(flag)) {
//		}
		else {
//			q.field("evaluation").exists();
//			q.field("waybillStatus").notEqual(EnumWaybillStatus.RETURNBACK.getValue());
			q.or(q.criteria("evaluation").exists(),
	    			q.criteria("waybillStatus").equal(EnumWaybillStatus.RETURNBACK.getValue()));
		}
    	q.field("normalUserId").equal(normalUserId);
    	q.order("-createDate");
    	long count = q.countAll();
        q.offset((pageNo-1)*pageSize).limit(pageSize);
        List<WayBillSEntity> list = q.asList();
        if (list != null && list.size() > 0) {
			for (WayBillSEntity entity : list) {
				ResExpress vo = new ResExpress();
				vo = transferEntityToVo(entity, vo);
				voList.add(vo);
			}
		}
        return new PageDTO<ResExpress>(pageNo, pageSize, count,voList);
    }
    
    /**
     * 用户评价
     */
    public EnumRespCode evaluate(String id,String userId,String name,String phone,String evaluationStar, String evaluationTag, String evaluationComment) {
    	WayBillSEntity entity = get(id);
    	if (entity != null) {
    		EvaluationEntity evaluation = evaluationService.save(EnumEvaluationType.RECEIVE.getValue(), id,entity.getWaybillNo(), userId, name, phone,
    				evaluationStar, evaluationTag, evaluationComment);
    		
    		UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    		ops.set("evaluationFlag", true);
    		ops.set("evaluation", evaluation);
    		
    		Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    		q.field("id").equal(id);
    		q.field("normalUserId").equal(userId);
			q.field("evaluation").doesNotExist();
    		int i = wayBillSDAO.update(q, ops).getUpdatedCount();
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
     * 配送件 修改配送方式
     */
    public EnumRespCode deliveryMethodChange(String id,String userId,String deliveryMethod) {
    	UpdateOperations<WayBillSEntity> ops = wayBillSDAO.createUpdateOperations();
    	ops.set("dispatchingWay",deliveryMethod);
    	ops.set("updateDate", new Date());
    	Query<WayBillSEntity> q = wayBillSDAO.createQuery();
    	q.field("id").equal(id);
    	q.field("normalUserId").equal(userId);
    	q.field("pickupType").equal(EnumDeliveryType.PEISONG.getValue());
    	int i = wayBillSDAO.update(q, ops).getUpdatedCount();
    	if (i > 0) {
    		return EnumRespCode.SUCCESS;
		}else {
			return EnumRespCode.FAIL;
		}
    }
    
    public static ResExpress transferEntityToVo(WayBillSEntity entity, ResExpress vo){
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
		if (entity.getOutOperator() != null) {
			vo.setOutOperatorName(entity.getOutOperator().getName());
			vo.setOutOperatorPhone(entity.getOutOperator().getPhone());
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