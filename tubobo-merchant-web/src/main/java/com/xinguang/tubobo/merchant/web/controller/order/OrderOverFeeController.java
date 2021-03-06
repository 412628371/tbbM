package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.admin.api.OverFeeService;
import com.xinguang.tubobo.admin.api.dto.OverFeeDTO;
import com.xinguang.tubobo.impl.merchant.repository.MerchantInfoRepository;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.OverFeeInfoRequest;
import com.xinguang.tubobo.merchant.web.response.order.OrderOverFeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/caculateOverFee")
public class OrderOverFeeController extends MerchantBaseController<OverFeeInfoRequest,OrderOverFeeResponse>{

    @Autowired
    private OverFeeService overFeeService;
    @Autowired
    private MerchantInfoRepository merchantInfoService;


    @Override
    protected OrderOverFeeResponse doService(String userId, OverFeeInfoRequest req) throws MerchantClientException {
        //获得商户信息
        MerchantInfoEntity entity = merchantInfoService.findByUserIdAndDelFlag(userId, MerchantInfoEntity.DEL_FLAG_NORMAL);
        //获得本地区域码
        String nativeAreaCode = entity.getAddressAdCode();

        //调用后台传来的溢价信息
        OverFeeDTO overFee = overFeeService.findOverFee(nativeAreaCode);
        OrderOverFeeResponse orderOverFeeResponse = new OrderOverFeeResponse();
        Double peekOverFee=0.0;
        Double weatherOverFee=0.0;
        Double totalOverFee=0.0;
        if (null!=overFee){
            boolean peekIsOpen = overFee.getPeekIsOpen();
            //boolean weatherIsOpen = overFee.getWeatherIsOpen();
            //为空时说明该区域未开启天气溢价
            Double weatherOverFeeRemote=overFee.getWeatherOverFee();
            String targetArea = overFee.getWeatherArea();
            //校验区域;
            if (peekIsOpen){
                peekOverFee=overFee.getPeekOverFee();
                totalOverFee=add(totalOverFee,peekOverFee);
                //totalOverFee+=peekOverFee;
            }
            if (null!=weatherOverFeeRemote){
                weatherOverFee=weatherOverFeeRemote;
                totalOverFee=add(totalOverFee,weatherOverFee);
//                totalOverFee+=weatherOverFee;
            }
        }


        orderOverFeeResponse.setPeekOverFee(peekOverFee);
        orderOverFeeResponse.setWeatherOverFee(weatherOverFee);
        orderOverFeeResponse.setTotalOverFee(totalOverFee);

        return orderOverFeeResponse;
    }

    public  Double add(Number value1, Number value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        return b1.add(b2).doubleValue();
    }

    /*boolean checkWeatherArea(String nativeAreaCode,String targetArea){
        boolean flag=false;
        if (StringUtils.isEmpty(targetArea)){
            flag=false;
        }else {
            String[] split = targetArea.split(",");
            List list=new ArrayList();
            list= Arrays.asList(split);
           if (list.contains(nativeAreaCode)){
               flag=true;
           }
        }
        return flag;
    }*/
}
