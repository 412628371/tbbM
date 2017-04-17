/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.dao;

import java.util.List;

import com.hzmux.hzcms.common.persistence.annotation.MyBatisDao;
import com.hzmux.hzcms.modules.sys.entity.Dict;

/**
 * MyBatis字典DAO接口
 * @author Song
 * @version 2014-10-01
 */
@MyBatisDao
public interface MyBatisDictDao {
	
    Dict get(String id);
    
    List<Dict> find(Dict dict);
    
}
