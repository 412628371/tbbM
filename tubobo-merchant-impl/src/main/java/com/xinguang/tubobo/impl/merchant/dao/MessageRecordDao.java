package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageRecordEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/15.
 */
@Repository
@Transactional(readOnly = true)
public class MessageRecordDao extends BaseDao<MerchantMessageRecordEntity> {

}
