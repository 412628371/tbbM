package com.xinguang.tubobo.impl.alisms.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.impl.alisms.dao.AliSmsRecordDao;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;
import com.xinguang.tubobo.impl.alisms.enums.EnumAliSmsStatus;
import com.xinguang.tubobo.impl.alisms.utils.AliMessageHandlerResponse;
import com.taobao.api.response.AlibabaAliqinFcSmsNumQueryResponse;
import com.taobao.api.response.AlibabaAliqinFcSmsNumQueryResponse.FcPartnerSmsDetailDto;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


@Service
public class AliSmsRecordService {

    @Autowired
    private AliSmsRecordDao aliSmsRecordDao;
    
    public AliSmsRecordEntity save(AliSmsRecordEntity entity){
		entity.setId(IdGen.uuid());
    	entity.setCreateDate(new Date());
		entity.setSmsStatus(EnumAliSmsStatus.INIT.getValue());
		aliSmsRecordDao.save(entity);
		return entity;
    }
    
    public AliSmsRecordEntity get(String id){
    	if (StringUtils.isBlank(id)) {
			return null;
		}
		return aliSmsRecordDao.get(id);
    }
    
    /**
     * 查询待发送的短信idList
     */
    public List<String> queryUnSendAliSmsRecordsIdList(boolean immediate, Date beginDate) {
        Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
        query.field("smsStatus").equal(EnumAliSmsStatus.INIT.getValue());
        query.field("immediate").equal(immediate);
        if (beginDate != null) {
        	query.field("createDate").greaterThanOrEq(DateUtils.getDateStart(beginDate));
		}
        return aliSmsRecordDao.findIds(query);
    }

    /**
     * 根据状态查询今天已发送的短信idList
     */
    public List<String> queryTodaySendedAliSmsRecords(String storeId, String operator,String smsStatus) {
    	Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
    	if (StringUtils.isNotBlank(storeId)) {
    		query.field("storeId").equal(storeId);
		}
    	if (StringUtils.isNotBlank(operator)) {
    		query.field("operator").equal(operator);
    	}
    	query.field("smsStatus").equal(smsStatus);
		query.field("updateDate").greaterThanOrEq(DateUtils.getTodayStart());
    	return aliSmsRecordDao.findIds(query);
    }

    /**
     * 查询今天发送失败的短信记录（查询失败/异常状态）
     */
    public List<AliSmsRecordEntity> queryTodayFailedAliSmsRecords(String storeId, String operator) {
    	Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
    	if (StringUtils.isNotBlank(storeId)) {
    		query.field("storeId").equal(storeId);
    	}
    	if (StringUtils.isNotBlank(operator)) {
    		query.field("operator").equal(operator);
    	}
//    	query.field("smsStatus").equal(EnumAliSmsStatus.FAIL.getValue());
    	query.or(query.criteria("smsStatus").equal(EnumAliSmsStatus.FAIL.getValue()),
    			query.criteria("smsStatus").equal(EnumAliSmsStatus.EXCEPTION.getValue()));
    	query.field("updateDate").greaterThanOrEq(DateUtils.getTodayStart());
    	return query.asList();
    }

    public Page<AliSmsRecordEntity> find(int pageNo,int pageSize, AliSmsRecordEntity entity) {
        Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
        if(StringUtils.isNotBlank(entity.getPhone())) {
        	query.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        if(StringUtils.isNotBlank(entity.getStoreId())) {
        	query.field("storeId").equal(entity.getStoreId());
        }
        if(StringUtils.isNotBlank(entity.getOperator())) {
        	query.field("operator").equal(entity.getOperator());
        }
        if(StringUtils.isNotBlank(entity.getSmsType())) {
        	query.field("smsType").equal(entity.getSmsType());
        }
        if(StringUtils.isNotBlank(entity.getSmsStatus())) {
        	query.field("smsStatus").equal(entity.getSmsStatus());
        }
        if(entity.getCreateDate() != null) {
        	query.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(entity.getUpdateDate() != null) {
        	query.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        query.order("-createDate");
        long count = query.countAll();
        query.offset((pageNo-1)*pageSize).limit(pageSize);
        return new Page<AliSmsRecordEntity>(pageNo, pageSize, count, query.asList());
    }
    
    /**
     * 根据ali发送短信接口返回参数AlibabaAliqinFcSmsNumSendResponse 更新短信记录
     */
    public int updateBySendResponse(String id,AlibabaAliqinFcSmsNumSendResponse response){
    	if (response == null || StringUtils.isBlank(id)) {
			return 0;
		}
    	UpdateOperations<AliSmsRecordEntity> ops = aliSmsRecordDao.createUpdateOperations();
    	if (response.getResult() != null) {
    		if (response.getResult().getSuccess() != null && response.getResult().getSuccess()) {
    			ops.set("smsStatus", EnumAliSmsStatus.WAIT.getValue());
			} else {
				ops.set("smsStatus", EnumAliSmsStatus.FAIL.getValue());
			}
    		if (null != response.getResult().getSuccess()) {
				ops.set("bizSuccess", response.getResult().getSuccess());
			}
    		if (StringUtils.isNotBlank(response.getResult().getModel())) {
    			ops.set("bizId", response.getResult().getModel());
    		}
    		if (StringUtils.isNotBlank(response.getResult().getErrCode())) {
    			ops.set("bizResultCode", response.getResult().getErrCode());
    		}
    		if (StringUtils.isNotBlank(response.getResult().getMsg())) {
    			ops.set("bizMsg", response.getResult().getMsg());
    		}
		} else {
			ops.set("smsStatus", EnumAliSmsStatus.EXCEPTION.getValue());
			if (StringUtils.isNotBlank(response.getErrorCode())) {
				ops.set("taobaoResponseErrorCode", response.getErrorCode());
			}
			if (StringUtils.isNotBlank(response.getMsg())) {
				ops.set("taobaoResponseMsg", response.getMsg());
			}
			if (StringUtils.isNotBlank(response.getSubCode())) {
				ops.set("taobaoResponseSubCode", response.getSubCode());
			}
			if (StringUtils.isNotBlank(response.getSubMsg())) {
				ops.set("taobaoResponseSubMsg", response.getSubMsg());
			}
		}
    	ops.set("updateDate", new Date());
    	Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
        query.field("id").equal(id);
    	return aliSmsRecordDao.update(query, ops).getUpdatedCount();
    }

    /**
     * 根据ali查询短信接口返回参数AlibabaAliqinFcSmsNumQueryResponse 更新短信记录
     */
    public int updateByQueryResponse(String id,AlibabaAliqinFcSmsNumQueryResponse response){
    	if (response == null || StringUtils.isBlank(id)) {
			return 0;
		}
    	UpdateOperations<AliSmsRecordEntity> ops = aliSmsRecordDao.createUpdateOperations();
    	if (response.getValues() != null && response.getValues().size() > 0) {
    		FcPartnerSmsDetailDto dto = response.getValues().get(0);
			if (null != dto.getSmsStatus()) {
				ops.set("smsStatus", dto.getSmsStatus().toString());
			} else {
				ops.set("smsStatus", EnumAliSmsStatus.FAIL.getValue());
			}
			if (null != dto.getSmsSendTime()) {
				ops.set("sendTime",DateUtils.formatDateTime(dto.getSmsSendTime()));
			}
			if (null != dto.getSmsReceiverTime()) {
				ops.set("receiverTime",DateUtils.formatDateTime(dto.getSmsReceiverTime()));
			}
			if (StringUtils.isNotBlank(dto.getResultCode())) {
				ops.set("bizResultCode", dto.getResultCode());
			}
		}else {
			ops.set("smsStatus", EnumAliSmsStatus.EXCEPTION.getValue());
			if (StringUtils.isNotBlank(response.getErrorCode())) {
				ops.set("taobaoResponseErrorCode", response.getErrorCode());
			}
			if (StringUtils.isNotBlank(response.getMsg())) {
				ops.set("taobaoResponseMsg", response.getMsg());
			}
			if (StringUtils.isNotBlank(response.getSubCode())) {
				ops.set("taobaoResponseSubCode", response.getSubCode());
			}
			if (StringUtils.isNotBlank(response.getSubMsg())) {
				ops.set("taobaoResponseSubMsg", response.getSubMsg());
			}
		}
    	ops.set("updateDate", new Date());
    	Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
    	query.field("id").equal(id);
    	return aliSmsRecordDao.update(query, ops).getUpdatedCount();
    }

    /**
     * 根据ali短信回调内容更新短信记录
     */
    public int updateByAliMessageHandlerResponse(AliMessageHandlerResponse response){
    	if (response == null || StringUtils.isBlank(response.getBiz_id())) {
			return 0;
		}
    	UpdateOperations<AliSmsRecordEntity> ops = aliSmsRecordDao.createUpdateOperations();
		if ("1".equals(response.getState())) {
			ops.set("smsStatus", EnumAliSmsStatus.SUC.getValue());
		} else {
			ops.set("smsStatus", EnumAliSmsStatus.FAIL.getValue());
			ops.set("taobaoResponseSubMsg", "运营商返回失败");
		}
		if (StringUtils.isNotBlank(response.getErr_code())) {
			ops.set("bizResultCode", response.getErr_code());
		}
		if (StringUtils.isNotBlank(response.getSend_time())) {
			ops.set("sendTime", response.getSend_time());
		}
		if (StringUtils.isNotBlank(response.getRept_time())) {
			ops.set("receiverTime", response.getRept_time());
		}
    	ops.set("updateDate", new Date());
    	Query<AliSmsRecordEntity> query = aliSmsRecordDao.createQuery();
        query.field("bizId").equal(response.getBiz_id());
    	return aliSmsRecordDao.update(query, ops).getUpdatedCount();
    }
    
}
