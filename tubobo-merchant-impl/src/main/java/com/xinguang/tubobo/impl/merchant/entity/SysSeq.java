/**
 * There are <a href="http://www.hzmux.com">hzmux</a> code generation
 */
package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * seqEntity
 * @author hanyong
 * @version 2017-01-15
 */
@Entity
@Table(name = "sys_seq")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysSeq {
	@Id
	private String sequenceName;
	private Long nextVal;

	protected SysSeq() {
		super();
	}

	public SysSeq(String sequenceName, Long nextVal) {
		this.sequenceName = sequenceName;
		this.nextVal = nextVal;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public Long getNextVal() {
		return nextVal;
	}
}


