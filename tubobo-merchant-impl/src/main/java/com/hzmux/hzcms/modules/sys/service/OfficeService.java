/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.service;

import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.modules.sys.dao.OfficeDao;
import com.hzmux.hzcms.modules.sys.entity.Office;
import com.hzmux.hzcms.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机构Service
 * @author Song
 * @version 2014-10-01
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private AreaService areaService;

	public Office get(String id) {
		return officeDao.get(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findSubStore(String parantId){
		String sqlString = "select * from sys_office where del_flag = '0' and parent_ids like '%" + parantId + "%' and ( type = '3' or type = '4' )";
		return officeDao.findBySql(sqlString, null, Office.class);
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		
		// 2016-04-05 韩勇：添加门店支持
		// 这里填个坑， 由于controller 里用了@ModelAttribute 方法，
		// 这里的area除了id是刚选的id， 其他的都是之前的数据
		// 用之前需要从新设一遍
		office.setArea(areaService.get(office.getArea().getId()));
		
//		if(office.isStore()) {
//		    storeService.saveOrUpdate(office);
//		} else {
//		    // 不管删一下总没错（防止门店改成非门店）
//		    storeService.delete(office.getId());
//		}

		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		officeDao.deleteById(id, "%,"+id+",%");
		
		// 2016-04-05 韩勇：添加门店支持
//		storeService.delete(id);

		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
}
