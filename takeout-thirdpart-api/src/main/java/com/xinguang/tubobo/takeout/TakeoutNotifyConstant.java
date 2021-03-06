package com.xinguang.tubobo.takeout;

import java.io.Serializable;

/**
 * 外卖回调常量类.
 */
public interface TakeoutNotifyConstant extends Serializable{

    enum PlatformCode implements TakeoutNotifyConstant{
        MT("MT"),//美团
        ELE("ELE"),//饿了么
        YZ("YZ"),//有赞
        BD("BD");//百度
        private String value;

        PlatformCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
       public static PlatformCode getByCode(String code){
            PlatformCode platformCode = null;
            for (PlatformCode p :PlatformCode.values()){
                if (p.getValue().equals(code)){
                    platformCode = p;
                    break;
                }
            }
            return platformCode;
        }
    }
    enum RmqConfig implements TakeoutNotifyConstant{
        EXCHANGE_NAME("merchant_takeout_exchange"),
        THIRD_ANSWER_QUEUE_NAME("merchant_takeout_queue_answer"),
        MT_QUEUE_NAME("merchant_takeout_queue_mt"),
        ELE_QUEUE_NAME("merchant_takeout_queue_ele"),
        THIRD_QUEUE_ANSWER_BINDING_KEY("merchant.takeout.bindingKey.answer.#"),
        MT_QUEUE_BINDING_KEY("merchant.takeout.bindingKey.mt.#"),
        ELE_QUEUE_BINDING_KEY("merchant.takeout.bindingKey.mt.#");
        private String value;

        RmqConfig(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    enum MtNotifyType implements TakeoutNotifyConstant {
        STORE_BIND("100", "店铺映射回调"),
        STORE_UNBIND("110", "店铺解绑回调"),
        ORDER_RECEIVER("200", "用户下单回调"),
        ORDER_CONFIRM("210", "商家确认订单回调"),
        ORDER_CANCEL("220", "订单取消回调"),
        ORDER_FINISH("230", "订单完成回调"),
        ORDER_DELIVERY_STATUS_CHANGE("240", "订单配送状态回调");

        private String code;
        private String label;

        MtNotifyType(String code, String label) {
            this.code = code;
            this.label = label;
        }
        public String getCode() {
            return this.code;
        }
        public String getLabel() {
            return label;
        }
    }

    enum ThirdAnswerType implements TakeoutNotifyConstant {
        ACCEPTED("400", "已接单"),
        PICKED("410", "已取货，配送中"),
        FINISH("420", "已完成");

        private String code;
        private String label;

        ThirdAnswerType(String code, String label) {
            this.code = code;
            this.label = label;
        }
        public String getCode() {
            return this.code;
        }
        public String getLabel() {
            return label;
        }
    }
}
