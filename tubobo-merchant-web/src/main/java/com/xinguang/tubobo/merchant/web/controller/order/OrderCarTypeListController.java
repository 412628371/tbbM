package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.CarTypeDTO;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.RespCommonList;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderCarTypeItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆开通类型controller.
 */
@Controller
@RequestMapping("/2.0/order/carType/list")
public class OrderCarTypeListController extends MerchantBaseController<Object,RespCommonList> {
    @Autowired
    AdminToMerchantService adminToMerchantService;
    @Override
    protected RespCommonList doService(String userId, Object req) throws MerchantClientException {
        List<CarTypeDTO> list = adminToMerchantService.queryCarTypeList();
        List<RespOrderCarTypeItem> respList = null;
        if (null != list){
            respList = new ArrayList<>(list.size());
            for (CarTypeDTO carTypeDTO :list){
                RespOrderCarTypeItem item = new RespOrderCarTypeItem();
                BeanUtils.copyProperties(carTypeDTO,item);
                respList.add(item);
            }
        }
        RespCommonList resp = new RespCommonList<>(respList);
        return resp;
    }
}
