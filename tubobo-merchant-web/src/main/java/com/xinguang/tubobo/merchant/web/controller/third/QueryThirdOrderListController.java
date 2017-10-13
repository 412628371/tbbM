package com.xinguang.tubobo.merchant.web.controller.third;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.condition.ProcessQueryCondition;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.ThirdOrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.third.ReqQueryThirdOrderList;
import com.xinguang.tubobo.merchant.web.response.third.RespQueryThirdOrder;
import com.xinguang.tubobo.merchant.web.response.third.RespQueryThirdOrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取第三方平台订单controller.
 */
@RestController
@RequestMapping("/third/getOrderList")
public class QueryThirdOrderListController extends MerchantBaseController<ReqQueryThirdOrderList,RespQueryThirdOrder>{
    @Autowired private ThirdOrderService mtOrderService;
    @Override
    protected RespQueryThirdOrder doService(String userId, ReqQueryThirdOrderList req) throws MerchantClientException {
        RespQueryThirdOrder respQueryThirdOrder = new RespQueryThirdOrder();
        List<RespQueryThirdOrderItem> list = new ArrayList<>();
        int pageNo = req.getPageNo();
        int pageSize = req.getPageSize();
        int totalSize = 0;
        String keyword = StringUtils.trim(req.getKeyword());
        if (StringUtils.isNotBlank(keyword) && !StringUtils.isAlphanumeric(keyword)){

        }else {
            ProcessQueryCondition condition = new ProcessQueryCondition();
            condition.setKeyword(keyword);
            condition.setPlatformCode(req.getPlatformCode());
            condition.setUserId(userId);
            condition.setQueryType(req.getQueryType());
            Page<ThirdOrderEntity> page = mtOrderService.findUnProcessedPageByUserId(condition,req.getPageNo(),req.getPageSize());

            if (page!= null && page.getContent() != null && page.getContent().size() > 0) {
                for (ThirdOrderEntity entity :page.getContent()) {
                    RespQueryThirdOrderItem vo = new RespQueryThirdOrderItem();
                    BeanUtils.copyProperties(entity,vo);
                    list.add(vo);
                }
            }
            pageNo = req.getPageNo();
            pageSize = page.getSize();
            totalSize = (int) page.getTotalElements();
        }

        respQueryThirdOrder.setKeyword(req.getKeyword());
        respQueryThirdOrder.setList(list);
        respQueryThirdOrder.setPageNo(pageNo);
        respQueryThirdOrder.setPageSize(pageSize);
        respQueryThirdOrder.setTotalSize(totalSize);
        return respQueryThirdOrder;
    }
}
