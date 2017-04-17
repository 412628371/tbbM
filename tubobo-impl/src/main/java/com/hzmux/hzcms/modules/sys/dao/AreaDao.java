/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author Song
 * @version 2014-10-01
 */
@Repository
public class AreaDao extends BaseDao<Area> {
	
	public Area findByLikelyName(String name){
		List<Area> list = findBySql("select * from sys_area where name like '%" + name + "%' and del_flag = '0' ", null, Area.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	public List<Area> findByParentId(String parentId){
		return findBySql("select * from sys_area where parent_id = :p1 and del_flag = '0' ", new Parameter(parentId), Area.class);
	}

	public List<Area> findByParentIdsLike(String parentIds){
		return find("from Area where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Area> findAllList(){
		return find("from Area where delFlag=:p1 order by code", new Parameter(Area.DEL_FLAG_NORMAL));
	}
	
	public List<Area> findAllChild(Long parentId, String likeParentIds){
		return find("from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code",
				new Parameter(Area.DEL_FLAG_NORMAL, parentId, likeParentIds));
	}
}
