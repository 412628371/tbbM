package com.xinguang.tubobo.impl.service;

import static org.mongodb.morphia.aggregation.Group.first;
import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Group.id;
import static org.mongodb.morphia.aggregation.Group.sum;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.impl.entity.ReportDataEntity;
import com.xinguang.tubobo.impl.dao.ReportDataDAO;

@Service
public class ReportDataService extends BaseService{
	
    @Autowired
    private ReportDataDAO reportDataDAO;
    
    public void save(ReportDataEntity entity){
    	String id = IdGen.uuid();
    	entity.setId(id);
    	// 报表日期为昨天
    	entity.setDate(DateUtils.formatDate(DateUtils.getYesterdayStart()));
    	// 报表生成日期为现在
    	entity.setCreateDate(new Date());
    	reportDataDAO.save(entity);
    }

    public ReportDataEntity find(String storeId, Date date) {
        Query<ReportDataEntity> q = reportDataDAO.createQuery();
        q.field("storeId").equal(storeId);
        q.field("date").equal(DateUtils.formatDate(date));
        return reportDataDAO.findOne(q);
    }

    public Page<ReportDataEntity> find(int pageNo, int pageSize, String storeId, Date date) {
    	Query<ReportDataEntity> q = reportDataDAO.createQuery();
    	if(StringUtils.isNotBlank(storeId)) {
    		q.field("storeId").equal(storeId);
    	}
    	q.field("date").equal(DateUtils.formatDate(date));
    	long count = q.countAll();
    	q.order("-paijianNum");
    	q.offset((pageNo-1)*pageSize).limit(pageSize);
        List<ReportDataEntity> list = q.asList();
    	return new Page<ReportDataEntity>(pageNo, pageSize, count, list);
    }

    public List<ReportDataEntity> find(List<String> storeIdList, Date begin, Date end) {
    	Query<ReportDataEntity> q = reportDataDAO.createQuery();
    	if(storeIdList != null && storeIdList.size() > 0) {
    		q.field("storeId").in(storeIdList);
    	}
    	q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(DateUtils.addDays(begin, 1)));
		q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(DateUtils.addDays(end, 1)));
    	return q.asList();
    }
    
    public Page<ReportDataEntity> find(int pageNo, int pageSize, String storeId, Date begin ,Date end) {
    	Query<ReportDataEntity> q = reportDataDAO.createQuery();
    	if(StringUtils.isNotBlank(storeId)) {
    		q.field("storeId").equal(storeId);
    	}
		q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(DateUtils.addDays(begin, 1)));
		q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(DateUtils.addDays(end, 1)));
    	AggregationPipeline pipeline = reportDataDAO.getDatastore()
				.createAggregation(ReportDataEntity.class)
				.match(q)
				.group(id(grouping("storeId")),
						grouping("date", first("date")),
						grouping("storeId", first("storeId")),
						grouping("storeName", first("storeName")),
						grouping("employeeNum", sum("employeeNum")),
						grouping("paijianNum", sum("paijianNum")),
						grouping("zitiNum", sum("zitiNum")),
						grouping("peisongNum", sum("peisongNum")),
//						grouping("inNum", sum("inNum")),
//						grouping("outNum", sum("outNum")),
						grouping("delayNum", sum("delayNum")),
						grouping("signNum", sum("signNum")),
						grouping("returnBackNum", sum("returnBackNum")),
						grouping("chukuNum", sum("chukuNum")),
						grouping("chukuAndSignNum", sum("chukuAndSignNum")),
						grouping("shoujianNum", sum("shoujianNum")))
//						grouping("feedBackNum", sum("feedBackNum")))
//						grouping("countAll", new Accumulator("$sum", 1))//累加
				.sort(Sort.descending("paijianNum"));
//				.skip((pageNo-1)*pageSize)
//				.limit(pageSize);
		Iterator<ReportDataEntity> iterator = pipeline.aggregate(ReportDataEntity.class);
		List<ReportDataEntity> list = new ArrayList<ReportDataEntity>();
		while (iterator.hasNext()) {
			ReportDataEntity entity = iterator.next();
			list.add(entity);
        }
		int from = (pageNo-1)*pageSize > list.size() ? list.size():(pageNo-1)*pageSize;
		int to = pageNo*pageSize > list.size() ? list.size():pageNo*pageSize;
		list = list.subList(from, to);
		return new Page<ReportDataEntity>(pageNo, pageSize, 0, list);
    }

}