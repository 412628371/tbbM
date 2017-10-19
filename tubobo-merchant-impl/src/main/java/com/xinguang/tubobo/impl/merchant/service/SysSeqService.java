/**
 * There are <a href="http://www.hzmux.com">hzmux</a> code generation
 */
package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.dao.SysSeqDao;
import com.xinguang.tubobo.impl.merchant.entity.SysSeq;
import com.xinguang.tubobo.impl.merchant.repository.SysSeqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * seqService
 * @author hanyong
 * @version 2017-01-15
 */
@Component
@Transactional
public class SysSeqService extends BaseService {


	@Autowired
	private SysSeqRepository sysSeqDao;



	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public Long nextVal(String sequenceName, int initialValue, int incrementSize) {

		// select
		Long value = select(sequenceName);

		// if null , insert
		if (null == value) {
			// if cannot insert , return null;
			SysSeq s = save(sequenceName, Long.valueOf(initialValue));
			if (null != s) {
				value = s.getNextVal();
			} else {
				return null;
			}
		}

		// update
		int count = update(sequenceName, value, incrementSize);
		if (count == 1) {
			return value;
		} else {
			// if cannot update , return null;
			return null;
		}

	}


	private  Long select(String sequenceName) {
		SysSeq sysSeq = sysSeqDao.findBySequenceName(sequenceName);
		//SysSeq	sysSeq =  sysSeqDao.getByHql(selectQuery, new Parameter(sequenceName));
		if (null == sysSeq) {
			return null;
		} else {
			return sysSeq.getNextVal();
		}
	}

	private   SysSeq save(String sequenceName, Long initialValue) {
		SysSeq sysSeq = new SysSeq(sequenceName, initialValue);
		/*SysSeq sysSeq = new SysSeq(sequenceName, initialValue);
		try {
			Session session = sysSeqDao.getSession();
			session.beginTransaction();

			session.save(sysSeq);
			sysSeqDao.flush();
		} catch (Exception e) {
			sysSeq = null;
			sysSeqDao.clear();
		}*/
		sysSeqDao.save(sysSeq);
		return  sysSeq;
	}

	private  int update(String sequenceName, long currentValue, long incrementSize) {
		long newvalue = currentValue + incrementSize;
		return sysSeqDao.updateNextVal(newvalue, sequenceName, currentValue);
	}

}