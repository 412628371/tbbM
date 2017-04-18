/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/Hzmux/hzcms">hzcms</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.dao.LogDao;
import com.hzmux.hzcms.modules.sys.entity.Log;

/**
 * 日志Service
 * @author Song
 * @version 2014-10-01
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService {

	@Autowired
	private LogDao logDao;
	
	public Log get(String id) {
		return logDao.get(id);
	}
	
	public Page<Log> find(Page<Log> page, Map<String, Object> paramMap) {
		DetachedCriteria dc = logDao.createDetachedCriteria();

		Long createById = StringUtils.toLong(paramMap.get("createById"));
		if (createById > 0){
			dc.add(Restrictions.eq("createBy.id", createById));
		}
		
//		String requestUri = ObjectUtils.toString(paramMap.get("requestUri"));
//		if (StringUtils.isNotBlank(requestUri)){
//			dc.add(Restrictions.like("requestUri", "%"+requestUri+"%"));
//		}
//
//		String exception = ObjectUtils.toString(paramMap.get("exception"));
//		if (StringUtils.isNotBlank(exception)){
//			dc.add(Restrictions.eq("type", Log.TYPE_EXCEPTION));
//		}
		
		Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
		if (beginDate == null){
			beginDate = DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
		if (endDate == null){
			endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		dc.add(Restrictions.between("createDate", beginDate, endDate));
		
		dc.addOrder(Order.desc("id"));
		return logDao.find(page, dc);
	}
	
}
