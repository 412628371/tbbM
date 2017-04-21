package com.xinguang.tubobo.merchant.web.controller.order;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.RespOrderItem;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.web.request.ReqOrderList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    MerchantOrderService merchantOrderService;

    @Override
    protected PageDTO<RespOrderItem> doService(String userId, ReqOrderList req) throws MerchantClientException {
        MerchantOrderEntity entity = new MerchantOrderEntity();
        entity.setOrderStatus(req.getOrderStatus());
        Page<MerchantOrderEntity> page = merchantOrderService.findMerchantOrderPage(userId,req.getPageNo(),req.getPageSize(),entity);
        List<RespOrderItem> list = new ArrayList<>(page.getPageNo());
        if (page!= null && page.getList() != null && page.getList().size() > 0) {
            for (MerchantOrderEntity task :page.getList()) {
                RespOrderItem vo = new RespOrderItem();
                BeanUtils.copyProperties(task,vo);
                list.add(vo);
            }
        }
        return new PageDTO<>(page.getPageNo(),page.getPageSize(),page.getCount(),list);
    }
}
