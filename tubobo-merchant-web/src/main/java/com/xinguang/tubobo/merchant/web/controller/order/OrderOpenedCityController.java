package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.admin.api.AdminToMerchantService;
import com.xinguang.tubobo.admin.api.dto.CityOpenDTO;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderOpenedCity;
import com.xinguang.tubobo.merchant.web.response.RespCommonList;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderOpenedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
@Controller
@RequestMapping("/2.0/order/city/list")
public class OrderOpenedCityController extends MerchantBaseController<ReqOrderOpenedCity,RespCommonList> {
    @Autowired
    AdminToMerchantService adminToMerchantService;
    @Override
    protected RespCommonList doService(String userId, ReqOrderOpenedCity req) throws MerchantClientException {
        List<CityOpenDTO> list = adminToMerchantService.queryOpenedCityList(req.getOrderType());
        List<RespOrderOpenedItem> respList = null;
        if (null != list){
            respList = new ArrayList<>(list.size());
            for (CityOpenDTO cityOpenDTO :list){
                RespOrderOpenedItem item = new RespOrderOpenedItem();
                item.setName(cityOpenDTO.getName());
                item.setCode(cityOpenDTO.getCode());
                respList.add(item);
            }
        }
        RespCommonList resp = new RespCommonList<>(respList);
        return resp;
    }
}
