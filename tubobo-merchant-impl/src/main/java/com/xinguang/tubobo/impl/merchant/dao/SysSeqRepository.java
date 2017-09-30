/**
 * There are <a href="http://www.hzmux.com">hzmux</a> code generation
 */
package com.xinguang.tubobo.impl.merchant.dao;

import com.xinguang.tubobo.impl.merchant.entity.SysSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * seqDAO接口
 * @author yanxb
 * @version 2017-09-30
 */
public interface SysSeqRepository extends JpaRepository<SysSeq, String>, JpaSpecificationExecutor<SysSeq> {

}
