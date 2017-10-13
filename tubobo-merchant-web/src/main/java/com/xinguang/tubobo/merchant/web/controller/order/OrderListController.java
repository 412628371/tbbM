package com.xinguang.tubobo.merchant.web.controller.order;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderList;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderItem;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/list")
public class OrderListController extends MerchantBaseController<ReqOrderList,PageDTO<RespOrderItem>> {
    @Autowired
    MerchantOrderManager merchantOrderManager;

    @Override
    protected PageDTO<RespOrderItem> doService(String userId, ReqOrderList req) throws MerchantClientException {
        MerchantOrderEntity entity = new MerchantOrderEntity();
        entity.setUserId(userId);
        entity.setOrderStatus(req.getOrderStatus());
        if (StringUtils.isBlank(req.getOrderStatus())){
            entity.setOrderStatus(MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL);
        }
        Page<OrderEntity> page = merchantOrderManager.merchantQueryOrderPage(req.getPageNo(),req.getPageSize(),entity);
        List<RespOrderItem> list = new ArrayList<>(req.getPageNo());
        if (page.hasContent()) {
            for (OrderEntity task :page) {
                RespOrderItem vo = new RespOrderItem();
                BeanUtils.copyProperties(task,vo);
                list.add(vo);
            }
        }
        return new PageDTO<>(req.getPageNo(),req.getPageSize(),page.getTotalElements(),list);
    }
}
