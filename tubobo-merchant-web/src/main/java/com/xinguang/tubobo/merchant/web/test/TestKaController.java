package com.xinguang.tubobo.merchant.web.test;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantToThirdPartyServiceImpl;
import com.xinguang.tubobo.impl.merchant.mq.RmqTakeoutAnswerProducer;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.AddressInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderCreateDto;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/test")
public class TestKaController extends MerchantBaseController<Object,Object>{
    @Autowired
    private MerchantToThirdPartyServiceImpl merchaService;

    @Autowired
    private OrderService orderService;
    @Autowired private MerchantThirdBindService bindService;
    @Autowired private RmqTakeoutAnswerProducer rmqTakeoutAnswerProducer;
    @Override
    protected Object doService(String userId, Object req) throws MerchantClientException {

//		bindService.bindMt(TakeoutNotifyConstant.PlatformCode.MT,userId,"aaa");
//		rmqTakeoutAnswerProducer.sendAccepted("YZ","30126","20170001","111",null);
        MerchantOrderCreateDto dto = new MerchantOrderCreateDto();
        MerchantOrderEntity merchantOrderEntity = orderService.findByOrderNo("061711300000110");
        dto.setUserId("76941");
        AddressInfoDTO consignor = new AddressInfoDTO();
        AddressInfoDTO receiver = new AddressInfoDTO();
        //插入发货方
        consignor.setAddressCity(merchantOrderEntity.getSenderAddressCity());
        consignor.setAddressDetail(merchantOrderEntity.getSenderAddressDetail());
        consignor.setAddressDistrict(merchantOrderEntity.getSenderAddressDistrict());
        consignor.setAddressProvince(merchantOrderEntity.getSenderAddressProvince());
        consignor.setLatitude(merchantOrderEntity.getSenderLatitude());
        consignor.setLongitude(merchantOrderEntity.getSenderLongitude());
        consignor.setName(merchantOrderEntity.getSenderName());
        consignor.setTelephone(merchantOrderEntity.getSenderPhone());
        //插入收货方
        receiver.setAddressCity(merchantOrderEntity.getReceiverAddressCity());
        receiver.setAddressDetail(merchantOrderEntity.getReceiverAddressDetail());
        receiver.setAddressDistrict(merchantOrderEntity.getReceiverAddressDistrict());
        receiver.setAddressProvince(merchantOrderEntity.getReceiverAddressProvince());
        receiver.setLatitude(merchantOrderEntity.getReceiverLatitude());
        receiver.setLongitude(merchantOrderEntity.getReceiverLongitude());
        receiver.setName(merchantOrderEntity.getReceiverName());
        receiver.setTelephone(merchantOrderEntity.getReceiverPhone());
        dto.setConsignor(consignor);
        dto.setReceiver(receiver);
        merchaService.createOrder(dto);
        return null;
    }

    @Override
    protected boolean needLogin() {
        return false;
    }
}
