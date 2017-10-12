/**
 * There are <a href="http://www.hzmux.com">hzmux</a> code generation
 */
package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.SysSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * seqDAO接口
 * @author yanx
 * @version 2017-09-30
 */
@Repository
/*
public interface SysSeqDao extends JpaRepository<SysSeq, Long>, JpaSpecificationExecutor<SysSeq> {
*/
public interface SysSeqRepository extends JpaRepository<SysSeq, Long>, JpaSpecificationExecutor<SysSeq> {
    // private static final String selectQuery = " from " + SysSeq.class.getSimpleName() + " where sequenceName = :p1";
    //	private static final String insertQuery = "";
    // private static final String updateQuery = "update " + SysSeq.class.getSimpleName() + " set nextVal = :p1 where sequenceName = :p2 and nextVal = :p3";
    @Modifying
    @Query("update #{#entityName} a set a.nextVal = :nextVal  where a.sequenceName = :sequenceName and a.nextVal = :existnextVal ")
    int updateNextVal(@Param("nextVal") long nextVal, @Param("sequenceName") String sequenceName, @Param("existnextVal") long existnextVal);

    SysSeq findBySequenceName(String sequenceName);


}
