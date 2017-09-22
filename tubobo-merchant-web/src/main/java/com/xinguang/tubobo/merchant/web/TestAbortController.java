package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.impl.merchant.manager.MerchantToThirdPartyServiceImpl;
import com.xinguang.tubobo.impl.merchant.mq.RmqTakeoutAnswerProducer;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.TbbMerchantResponse;
import com.xinguang.tubobo.merchant.api.dto.MerchantAbortConfirmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/test/abort")
public class TestAbortController extends MerchantBaseController<MerchantAbortConfirmDTO,TbbMerchantResponse<Boolean>>{
    @Autowired
    private MerchantToThirdPartyServiceImpl merchaService;

    @Autowired
    private OrderService orderService;
    @Autowired private MerchantThirdBindService bindService;
    @Autowired private RmqTakeoutAnswerProducer rmqTakeoutAnswerProducer;

    @Override
    protected TbbMerchantResponse<Boolean> doService(String userId, MerchantAbortConfirmDTO req) throws MerchantClientException {
        return  merchaService.abortConfirm(req);
    }

    @Override
    protected boolean needLogin() {
        return false;
    }
}
