/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.modules.sys.dao.AreaDao;
import com.hzmux.hzcms.modules.sys.dao.OfficeDao;
import com.hzmux.hzcms.modules.sys.entity.Area;
import com.hzmux.hzcms.modules.sys.entity.Office;
import com.hzmux.hzcms.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @author Song
 * @version 2014-10-01
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

	@Autowired
	private AreaDao areaDao;
	@Autowired
    OfficeDao officeDao;
	
	public Area get(String id) {
		return areaDao.get(id);
	}
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	
	public List<Area> findByParentId(String parentId){
		return areaDao.findByParentId(parentId);
	}

	public Area findByLikelyName(String name){
		return areaDao.findByLikelyName(name);
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		area.setParent(this.get(area.getParent().getId()));
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		areaDao.clear();
		areaDao.save(area);
		// 更新子节点 parentIds
		List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		areaDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		areaDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	public Map<String, String> findProvinceCityByOfficeId(String officeId){
		if(StringUtils.isBlank(officeId)){
			return null;
		}
		Office office = officeDao.get(officeId);
		if (null == office) {
			return null;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		Area area = get(office.getArea().getId());
		String pIds = area.getParentIds();
		String[] pIdsArr = pIds.split(",");
		for (String id : pIdsArr) {
			if (!"0".equals(id)&& !"1".equals(id)) {
				Area parent = get(id);
				if (null != parent && parent.getType().equals("2")) {
					map.put("province", parent.getName());
				}
				if (null != parent && parent.getType().equals("3")) {
					map.put("city", parent.getName());
				}
			}
		}
		return map;
	}
	
}
