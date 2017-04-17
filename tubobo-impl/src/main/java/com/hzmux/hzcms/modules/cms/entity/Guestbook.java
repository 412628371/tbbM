/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.cms.entity;

import com.hzmux.hzcms.common.persistence.BaseEntity;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.modules.sys.entity.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 留言Entity
 * @author Song
 * @version 2014-10-01
 */
@Entity
@Table(name = "cms_guestbook")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Guestbook extends BaseEntity<Category> {
	
	private static final long serialVersionUID = 1L;
	private String id; 		// 编号
	private String type; 	// 留言分类（咨询、建议、投诉、其它）
	private String content; // 留言内容
	private String name; 	// 姓名
	private String email; 	// 邮箱
	private String phone; 	// 电话
	private String workunit;// 单位
	private String ip; 		// 留言IP
	private Date createDate;// 留言时间
	private User reUser; 		// 回复人
	private Date reDate;	// 回复时间
	private String reContent;// 回复内容
	private String delFlag;	// 删除标记删除标记（0：正常；1：删除；2：审核）

	public Guestbook() {
		this.delFlag = DEL_FLAG_AUDIT;
	}

	public Guestbook(String id){
		this();
		this.id = id;
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
		this.createDate = new Date();
	}
	
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=2000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email @Length(min=0, max=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=100)
	public String getWorkunit() {
		return workunit;
	}

	public void setWorkunit(String workunit) {
		this.workunit = workunit;
	}

	@Length(min=1, max=100)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@NotNull
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne
	@JoinColumn(name="re_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getReUser() {
		return reUser;
	}

	public void setReUser(User reUser) {
		this.reUser = reUser;
	}

	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public Date getReDate() {
		return reDate;
	}

	public void setReDate(Date reDate) {
		this.reDate = reDate;
	}

	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}


