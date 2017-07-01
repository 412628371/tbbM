package com.xinguang.tubobo.merchant.web.controller.third;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdMtOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.ThirdMtOrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.third.ReqQueryThirdOrderList;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderItem;
import com.xinguang.tubobo.merchant.web.response.third.RespQueryThirdOrder;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取第三方平台订单controller.
 */
@Controller
@RequestMapping("/third/getOrderList")
public class QueryThirdOrderListController extends MerchantBaseController<ReqQueryThirdOrderList,PageDTO<RespQueryThirdOrder>>{
    @Autowired private ThirdMtOrderService mtOrderService;
    @Override
    protected PageDTO<RespQueryThirdOrder> doService(String userId, ReqQueryThirdOrderList req) throws MerchantClientException {
        if (TakeoutNotifyConstant.PlatformCode.MT.getValue().equals(req.getPlatformCode())){
            Page<ThirdMtOrderEntity> page = mtOrderService.findUnProcessedPageByUserId(userId,req.getPageNo(),req.getPageSize());
            List<RespQueryThirdOrder> list = new ArrayList<>(page.getPageNo());
            if (page!= null && page.getList() != null && page.getList().size() > 0) {
                for (ThirdMtOrderEntity entity :page.getList()) {
                    RespQueryThirdOrder vo = new RespQueryThirdOrder();
                    BeanUtils.copyProperties(entity,vo);
                    list.add(vo);
                }
            }
            return new PageDTO<>(page.getPageNo(),page.getPageSize(),page.getCount(),list);
        }else {
            throw new MerchantClientException(EnumRespCode.FAIL,"不支持的第三方平台");
        }
    }
}
