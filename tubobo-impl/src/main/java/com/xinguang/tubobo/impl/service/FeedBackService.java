package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.FeedBackEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.impl.dao.FeedBackDAO;

@Service
public class FeedBackService extends BaseService{
	
    @Autowired
    private FeedBackDAO feedBackDAO;
    
    public String save(FeedBackEntity entity){
    	String id = IdGen.uuid();	
    	entity.setId(id);
    	entity.setCreateDate(new Date());
    	entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
    	entity.setResult("0");
    	feedBackDAO.save(entity);
    	return id;
    }
    
    public FeedBackEntity get(String id){
    	return feedBackDAO.get(id);
    }
    
    public FeedBackEntity findByWayBillNo(String waybillNo){
    	Query<FeedBackEntity> q = feedBackDAO.createQuery();
    	q.field("waybillNo").equal(waybillNo);
    	return feedBackDAO.findOne(q);
    }

    public Page<FeedBackEntity> find(Page<FeedBackEntity> page, FeedBackEntity entity) {
    	Query<FeedBackEntity> q = feedBackDAO.createQuery();
        if(StringUtils.isNotBlank(entity.getWaybillNo())) {
            q.field("waybillNo").contains(Pattern.quote(entity.getWaybillNo()));
        } else {
        	if(null != entity.getCreateDate()) {
                q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
            }
            if(null != entity.getUpdateDate()) {
            	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
            }
		}
        if(StringUtils.isNotBlank(entity.getResult())) {
        	q.field("result").equal(entity.getResult());
        }
        if(StringUtils.isNotBlank(entity.getStoreId())) {
            q.field("storeId").equal(entity.getStoreId());
        }
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    public int updateContent(String id,String content,String updateBy){
    	UpdateOperations<FeedBackEntity> ops = feedBackDAO.createUpdateOperations();
        ops.set("content", content);
        ops.set("result", "0");
    	ops.set("updateBy", updateBy);
        ops.set("updateDate", new Date());
        return feedBackDAO.update(feedBackDAO.createQuery()
        		.field("id").equal(id), ops).getUpdatedCount();
    }

    public int deal(String id,String updateBy){
    	UpdateOperations<FeedBackEntity> ops = feedBackDAO.createUpdateOperations();
    	ops.set("result", "1");
    	ops.set("updateBy", updateBy);
    	ops.set("updateDate", new Date());
    	return feedBackDAO.update(feedBackDAO.createQuery()
    			.field("id").equal(id), ops).getUpdatedCount();
    }
    
    public List<FeedBackEntity> findToExportExcel(FeedBackEntity entity) throws Exception{
        Query<FeedBackEntity> q = feedBackDAO.createQuery();
    	if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        if(StringUtils.isNotBlank(entity.getResult())) {
        	q.field("result").equal(entity.getResult());
        }
        if(StringUtils.isNotBlank(entity.getStoreId())) {
            q.field("storeId").equal(entity.getStoreId());
        }
        q.order("-createDate");
        long count = q.countAll();
        if (count > 10000) {
			throw new Exception("失败：批量导出数量不允许超过10000条");
		}else if (count < 1) {
			throw new Exception("失败：符合条件的数据量为0");
		}
        return q.asList();
    }

	public long countNum(String storeId,Date begin,Date end) {
		Query<FeedBackEntity> q = feedBackDAO.createQuery();
		q.field("storeId").equal(storeId);
		q.field("createDate").greaterThanOrEq(begin);
		q.field("createDate").lessThanOrEq(end);
		q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
		return feedBackDAO.find(q).countAll();
	}
}