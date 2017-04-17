/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.cms.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.modules.cms.dao.CommentDao;
import com.hzmux.hzcms.modules.cms.entity.Comment;

/**
 * 评论Service
 * @author Song
 * @version 2014-10-01
 */
@Service
@Transactional(readOnly = true)
public class CommentService extends BaseService {

	@Autowired
	private CommentDao commentDao;
	
	public Comment get(String id) {
		return commentDao.get(id);
	}
	
	public Page<Comment> find(Page<Comment> page, Comment comment) {
		DetachedCriteria dc = commentDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(comment.getContentId())){
			dc.add(Restrictions.eq("contentId", comment.getContentId()));
		}
		if (StringUtils.isNotEmpty(comment.getTitle())){
			dc.add(Restrictions.like("title", "%"+comment.getTitle()+"%"));
		}
		dc.add(Restrictions.eq(Comment.FIELD_DEL_FLAG, comment.getDelFlag()));
		dc.addOrder(Order.desc("id"));
		return commentDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Comment comment) {
		commentDao.save(comment);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id, Boolean isRe) {
		commentDao.updateDelFlag(id, isRe!=null&&isRe?Comment.DEL_FLAG_AUDIT:Comment.DEL_FLAG_DELETE);
	}
	
}
