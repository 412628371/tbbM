/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.cms.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.modules.cms.dao.GuestbookDao;
import com.hzmux.hzcms.modules.cms.entity.Guestbook;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 留言Service
 * @author Song
 * @version 2014-10-01
 */
@Service
@Transactional(readOnly = true)
public class GuestbookService extends BaseService {

	@Autowired
	private GuestbookDao guestbookDao;
	
	public Guestbook get(String id) {
		return guestbookDao.get(id);
	}
	
	public Page<Guestbook> find(Page<Guestbook> page, Guestbook guestbook) {
		DetachedCriteria dc = guestbookDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(guestbook.getType())){
			dc.add(Restrictions.eq("type", guestbook.getType()));
		}
		if (StringUtils.isNotEmpty(guestbook.getContent())){
			dc.add(Restrictions.like("content", "%"+guestbook.getContent()+"%"));
		}
		dc.add(Restrictions.eq(Guestbook.FIELD_DEL_FLAG, guestbook.getDelFlag()));
		dc.addOrder(Order.desc("id"));
		return guestbookDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Guestbook guestbook) {
		guestbookDao.save(guestbook);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id, Boolean isRe) {
		guestbookDao.updateDelFlag(id, isRe!=null&&isRe?Guestbook.DEL_FLAG_AUDIT:Guestbook.DEL_FLAG_DELETE);
	}
	

}
