package java.service;

import com.xinguang.tubobo.impl.merchant.dao.ThirdOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.ThirdOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.BaseJunit4Test;

/**
 * Created by xuqinghua on 2017/7/10.
 */
public class TestThirdOrderService extends BaseJunit4Test {
    @Resource  //自动注入,默认按名称
    private ThirdOrderDao thirdOrderDao;

//    @Test   //标明是测试方法
//    @Transactional   //标明此方法需使用事务
//    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
//    public void insert( ) {
//
//
//    }

    @Test
    public void name() throws Exception {
        ThirdOrderEntity existEntity = new ThirdOrderEntity();
        existEntity.setPlatformCode("MT");
        existEntity.setDelFlag("0");
        existEntity.setOriginOrderId("1243");
        existEntity.setUserId("30126");
        existEntity.setReceiverLatitude(29.432344);
        existEntity.setReceiverLongitude(100.125989);
        thirdOrderDao.save(existEntity);
        ThirdOrderEntity expected = thirdOrderDao.findByOriginId("1243","MT");
        Assert.assertEquals("1243",expected.getOriginOrderId());
    }
}
