package com.xinguang.tubobo.impl.tuboboZhushouVo;

import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;

/**
 * 问问助手app 快递员登录验证成功token
 */

public class AuthToken {
	
	private EmployeeEntity employee;

	private StoreEntity store;

	public AuthToken(EmployeeEntity employee, StoreEntity store) {
		this.employee = employee;
		this.store = store;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public StoreEntity getStore() {
		return store;
	}

}
