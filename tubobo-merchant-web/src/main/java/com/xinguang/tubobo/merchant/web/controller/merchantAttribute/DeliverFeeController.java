package com.xinguang.tubobo.merchant.web.controller.merchantAttribute;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantDeliverFeeConfigServiceImpl;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.MerchantTypeService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeConfigDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumBindStatusType;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.merchantConfig.RespDeliverFeeDetailRuleQuery;
import com.xinguang.tubobo.merchant.web.response.merchantConfig.RespDeliverFeeRuleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanx on 2017/7/17.
 * 提供商家端查询配送费规则
 */
@Controller
@RequestMapping(value = "/order/deliveryFee/rule")
public class DeliverFeeController extends MerchantBaseController<String,RespDeliverFeeRuleQuery> {
    @Autowired
    MerchantDeliverFeeConfigServiceImpl merchantDeliverFeeConfigService;
    @Autowired
    OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantTypeService merchantTypeService;

    @Override
    protected RespDeliverFeeRuleQuery doService(String userId, String req) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity)
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        String orderType;
        MerchantTypeDTO merchantTypeDTO = null;
        Long merTypeId =  entity.getMerTypeId();
        if (merTypeId!=null){
            merchantTypeDTO = merchantTypeService.findById(merTypeId);
        }
        if (merchantTypeDTO==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TYPEERROR);
        }
        if (null != entity.getProviderId()){
            orderType = EnumOrderType.POSTORDER.getValue();
        }else {
            orderType = EnumOrderType.SMALLORDER.getValue();
        }
        List<MerchantDeliverFeeConfigDTO> list = merchantDeliverFeeConfigService.findFeeByAreaCodeAndOrderTypeAndTemId(entity.getAddressAdCode(),orderType,merchantTypeDTO.getTemId());
        RespDeliverFeeRuleQuery respDeliverFeeRuleQuery = new RespDeliverFeeRuleQuery();
        List<RespDeliverFeeDetailRuleQuery> listDto = new ArrayList<RespDeliverFeeDetailRuleQuery>();
        if (null!=list&&list.size()>0){
            for (MerchantDeliverFeeConfigDTO data : list) {
                respDeliverFeeRuleQuery.setInitDistance(data.getInitDistance());
                respDeliverFeeRuleQuery.setInitPrice(data.getInitFee());
                RespDeliverFeeDetailRuleQuery detail = new RespDeliverFeeDetailRuleQuery();
                detail.setBeginDistance(data.getBeginDistance());
                detail.setEndDistance(data.getEndDistance());
                detail.setPrice(data.getPerFee());
                listDto.add(detail);
            }
            respDeliverFeeRuleQuery.setList(listDto);
        }
        return respDeliverFeeRuleQuery;
    }
}
