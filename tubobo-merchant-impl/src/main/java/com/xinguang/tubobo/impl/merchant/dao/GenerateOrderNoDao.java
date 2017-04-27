package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Created by Administrator on 2017/4/26.
 */
@Repository
public class GenerateOrderNoDao {
    Logger logger = LoggerFactory.getLogger(GenerateOrderNoDao.class);
    @Autowired
    private DataSource dataSource;
    public String generateOrderNo()  {
//        try {
//            CallableStatement statement =dataSource.getConnection().prepareCall( "{CALL generate_orderNo('03',8, ? )}" );
//            statement.registerOutParameter(1, Types.VARCHAR);//返回参数
//            statement.execute();
//            String orderNo = statement.getString(1);
//            if (StringUtils.isNotBlank(orderNo) && orderNo.length() == 15){
//                StringBuilder sb = new StringBuilder();
//                sb.append(orderNo.substring(0,2)).append(orderNo.substring(4,15));
//                return sb.toString();
//            }
//            return orderNo;
//        } catch (SQLException e) {
//            logger.error("生成规则订单号失败, 改为UUID生成：{}",e.getMessage());
//            return IdGen.uuid();
//        }
        return IdGen.uuid();
    }
}
