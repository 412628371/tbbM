package com.xinguang.tubobo.impl.merchant.common;/**
 * Created by hanyong on 2017/1/15.
 */

import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.service.SysSeqService;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanyong
 * @Date 2017/1/15
 */
@Service
public class CodeGenerator {

    private static final String ORDER_CODE_NAME = "order_";

    public enum BusinessTypeEnum {
        MERCHANT_SMALL_ORDER("03","商家订单"),
        POST_ORDER("06","驿站订单"),
        MERCHANT_BIG_ORDER("04","车配订单");


        BusinessTypeEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }
        private String code;

        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }



    }

    @Autowired
    private SysSeqService sysSeqService;

    private Map<String, SeqGenerator> seqGeneratorMap = new ConcurrentHashMap<>();


    private Object monitor = new Object();

    private Long getCurrentSequenceValue(String codeName, String group, int incrementSize) {
        String sequenceName = codeName + (null==group?"":group);

        SeqGenerator generator = seqGeneratorMap.get(sequenceName);
        if (null == generator) {
            synchronized (monitor) {
                if (!seqGeneratorMap.containsKey(sequenceName)) {
                    generator = SeqGenerator.newInstance(sequenceName, incrementSize, this.sysSeqService);
                    seqGeneratorMap.put(sequenceName, generator); // TODO  more and more generators be here if there are date group.  should refactoring later.
                } else {
                    generator = seqGeneratorMap.get(sequenceName);
                }
            }
        }

        if (null == generator) {
            throw new RuntimeException("cannot get seqGenerator.");
        }
        return generator.generate();
    }

    /**
     * generator the customer code
     * @return
     */
    public String nextCustomerCode(String orderType) {
        String code = BusinessTypeEnum.MERCHANT_SMALL_ORDER.getCode();
        if (EnumOrderType.BIGORDER.getValue().equals(orderType)){
            code = BusinessTypeEnum.MERCHANT_BIG_ORDER.getCode();
        }
        if (EnumOrderType.POSTORDER.getValue().equals(orderType)){
            code = BusinessTypeEnum.POST_ORDER.getCode();
        }
        String date = DateUtils.getDate("yyMMdd");
        Long seq = getCurrentSequenceValue(ORDER_CODE_NAME , date, 100);

        return code+date + StringUtils.leftPad(String.valueOf(seq), 7, '0');
    }

}
