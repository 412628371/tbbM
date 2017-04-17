/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.rider.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.impl.rider.dao.RiderTaskDao;
import com.xinguang.tubobo.impl.rider.entity.RiderTaskEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RiderTaskService extends BaseService {

	@Autowired
	private RiderTaskDao riderTaskDao;

	public RiderTaskEntity findByTaskNo(String taskNo){
		if (StringUtils.isBlank(taskNo)) return null;
		String sqlString = "select * from tubobo_rider_task where task_no = :p1 and del_flag = '0' ";
		List<RiderTaskEntity> list = riderTaskDao.findBySql(sqlString, new Parameter(taskNo), RiderTaskEntity.class);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}
	}

	public RiderTaskEntity findByRiderIdAndTaskNo(String riderId,String taskNo){
		if (StringUtils.isBlank(riderId) || StringUtils.isBlank(taskNo)) return null;
		String sqlString = "select * from tubobo_rider_task where rider_id = :p1 and task_no = :p2 and del_flag = '0' ";
		List<RiderTaskEntity> list = riderTaskDao.findBySql(sqlString, new Parameter(riderId,taskNo), RiderTaskEntity.class);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 保存新任务
	 */
	@Transactional(readOnly = false)
	public RiderTaskEntity save(RiderTaskEntity task) {
		task.setId(IdGen.uuid());
		task.setDelFlag(RiderTaskEntity.DEL_FLAG_NORMAL);
		task.setCreateDate(new Date());
		riderTaskDao.save(task);
		return task;
	}

	/**
	 * 任务状态改变回调
	 */
	public int riderTaskStatusNotify(String taskNo,Integer taskStatus,Date acceptTime,Date pickTime,Date deliveryTime) {
		Parameter parameter = new Parameter();
		StringBuffer sb = new StringBuffer();
		sb.append("update tubobo_rider_task set task_status = :task_status");
		parameter.put("task_status", taskStatus);
		if (null != acceptTime){
			sb.append(", accept_time = :accept_time");
			parameter.put("accept_time", acceptTime);
		}
		if (null != pickTime){
			sb.append(", pick_time = :pick_time");
			parameter.put("pick_time", pickTime);
		}
		if (null != deliveryTime){
			sb.append(", delivery_time = :delivery_time, cost_time = timestampdiff(SECOND,pick_time,delivery_time) ");
			parameter.put("delivery_time", deliveryTime);
		}
		sb.append(" where task_no = :task_no and del_flag = '0' ");
		parameter.put("task_no", taskNo);
		return riderTaskDao.updateBySql(sb.toString(), parameter);
	}

	/**
     * 任务分页查询
     */
	public Page<RiderTaskEntity> findRiderTaskPage(int pageNo, int pageSize, RiderTaskEntity entity){
		Parameter parameter = new Parameter();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from tubobo_rider_task where del_flag = '0' ");
		if (StringUtils.isNotBlank(entity.getRiderId())){
			sb.append("and rider_id = :rider_id ");
			parameter.put("rider_id", entity.getRiderId());
		}
		if (StringUtils.isNotBlank(entity.getTaskNo())){
			sb.append("and task_no like :task_no ");
			parameter.put("task_no", "%"+entity.getTaskNo()+"%");
		}
		if (null != entity.getTaskStatus()){
			sb.append("and task_status = :task_status ");
			parameter.put("task_status", entity.getTaskStatus());
		}
		if (null != entity.getCreateDate()){
			sb.append("and create_date >= :create_date ");
			parameter.put("create_date", DateUtils.getDateStart(entity.getCreateDate()));
		}
		if (null != entity.getUpdateDate()){
			sb.append("and create_date <= :update_date ");
			parameter.put("update_date", DateUtils.getDateEnd(entity.getUpdateDate()));
		}
		sb.append(" order by create_date desc ");
		return riderTaskDao.findPage(sb.toString(), parameter, RiderTaskEntity.class,pageNo,pageSize);
	}
}
