package com.xinguang.tubobo.merchant.web.controller;

import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping(value = "/test")
public class TestController extends MerchantBaseController<Object,Object>{

	@Autowired
	private MerchantOrderService merchantOrderService;

	@Override
	protected Object doService(String userId, Object req) throws MerchantClientException {

		merchantOrderService.findByMerchantIdAndOrderNo("16032532234","8f21cf699dbd4fdbaba004f33c54c7b4");

		merchantOrderService.riderGrabItem("160","8f21cf699dbd4fdbaba004f33c54c7b4",new Date());

		return null;
	}
}
