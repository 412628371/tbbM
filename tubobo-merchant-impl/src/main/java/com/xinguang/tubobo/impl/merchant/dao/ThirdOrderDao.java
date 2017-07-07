package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Repository
public class ThirdOrderDao extends BaseDao<ThirdOrderEntity> {
    /**
     * 根据原始订单号查找记录
     * @param originOrderId
     * @return
     */
    public ThirdOrderEntity findByOriginId(String originOrderId){
        String hql = "from ThirdOrderEntity where originOrderId=:originOrderId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("originOrderId",originOrderId);
        ThirdOrderEntity entity = getByHql(hql,parameter);
        return entity;
    }

    public Page<ThirdOrderEntity> findUnProcessedPageByUserId(String userId,String platformCode,String keyword, int pageNo, int pageSize){
        StringBuffer hqlSb = new StringBuffer("select * from t_third_order where  processed=:processed and del_flag='0'");
        Parameter parameter = new Parameter();
        if (StringUtils.isNotBlank(userId)){
            hqlSb.append(" and user_id=:userId ");
            parameter.put("userId",userId);
        }
        if (StringUtils.isNotBlank(platformCode)){
            hqlSb.append(" and platform_code=:platformCode ");
            parameter.put("platformCode",platformCode);
        }
        if (StringUtils.isNotBlank(keyword)){
            hqlSb.append(" and origin_order_view_id like :keyword ");
            parameter.put("keyword","%"+keyword+"%");
        }
        parameter.put("processed",false);
        hqlSb.append(" order by -origin_order_view_id asc");
        return findPage(hqlSb.toString(), parameter, ThirdOrderEntity.class,pageNo,pageSize);
    }


    /**
     * 将订单标记为已处理
     * @param originOrderId
     */
    public boolean processOrder(String userId,String platformCode,String originOrderId){
        String hql = "update ThirdOrderEntity set processed=:processed ,updateDate=:updateDate where originOrderId=:originOrderId and platformCode=:platformCode and userId=:userId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("originOrderId",originOrderId);
        parameter.put("userId",userId);
        parameter.put("platformCode",platformCode);
        parameter.put("updateDate",new Date());
        parameter.put("processed",true);
        int count = update(hql,parameter);
        return count == 1;
    }

    /**
     * 逻辑删除所有的记录，即标志位变为删除状态
     * @return
     */
    public int delRecordsPastHours(int pastHours){
        String hql = "update ThirdOrderEntity set delFlag='1' ,updateDate=:updateDate where delFlag='0' and DATE_ADD(create_date,INTERVAL :pastHours HOUR)<NOW()";
        Parameter parameter = new Parameter();
        parameter.put("updateDate",new Date());
        parameter.put("pastHours",pastHours);
        int count = update(hql,parameter);
        return count;
    }
}
