package com.xinguang.tubobo.merchant.web;

import com.alibaba.dubbo.common.serialize.support.json.JsonObjectOutput;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.request.CreateOrderRequest;
import com.xinguang.tubobo.merchant.web.request.ReqAccountPay;
import com.xinguang.tubobo.merchant.web.response.ClientResp;
import com.xinguang.tubobo.merchant.web.response.CreateOrderResponse;
import com.xinguang.tubobo.merchant.web.response.RespOrderPay;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;


/**
 * Created by Administrator on 2017/4/24.
 */
public class App {
    static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws MerchantClientException, SQLException {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
//        CodeGenerator codeGenerator = (CodeGenerator) context.getBean("codeGenerator");
//        for (int i = 0;i< 40;i++){
//            String orderNo = codeGenerator.nextCustomerCode();
//            System.out.println(orderNo);
//        }
//        MerchantPushService merchantPushService = (MerchantPushService) context.getBean("merchantPushService");
//        merchantPushService.pushToUser("30126","test","订单被接单");
//        DeliveryFeeService feeService = (DeliveryFeeService) context.getBean("deliveryFeeService");
//        Double money = feeService.sumDeliveryFeeByDistance(4300.0);
//        System.out.println(money);

        String creatOrder = "http://172.16.1.181:8003/order/create";
        String payUrl = "http://172.16.1.181:8003/shop/pay";
        String token = "f1e937765038475baaec800653ab8af4";
        try {
            // 获取当前客户端对象
            HttpClient httpClient = new DefaultHttpClient();

            // 根据地址获取请求
            HttpPost orderPost = new HttpPost(creatOrder);//这里发送get请求
            // 通过请求对象获取响应对象
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            createOrderRequest.setReceiverName("xqh");
            createOrderRequest.setDeliveryFee(3.0);
            createOrderRequest.setReceiverAddressCity("杭州");
            createOrderRequest.setReceiverAddressRoomNo("20lou");
            createOrderRequest.setReceiverLatitude(30.2430530000);
            createOrderRequest.setReceiverLongitude(120.1842890000);
            createOrderRequest.setReceiverPhone("18668123035");
            createOrderRequest.setReceiverAddressDetail("近江时代大厦");
            String orderJson = JSON.toJSONString(createOrderRequest);

            StringEntity orderEntity = new StringEntity(orderJson,"utf-8");//解决中文乱码问题
            orderEntity.setContentEncoding("UTF-8");
            orderEntity.setContentType("application/json");
            orderPost.setEntity(orderEntity);
            orderPost.setHeader("Authorization",token);
            HttpResponse orderHttpResponse = httpClient.execute(orderPost);
            String responseJson;
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (orderHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseJson = EntityUtils.toString(orderHttpResponse.getEntity(),"utf-8");
//                JSONObject jsonObject = JSONObject.parseObject(responseJson);

                ClientResp<CreateOrderResponse> createOrderResponse = resolveResponse(responseJson,CreateOrderResponse.class);
                if ("0".equals(createOrderResponse.getResultCode())){
                    String orderNo = createOrderResponse.getResultData().getOrderNo();
                    HttpPost payPost = new HttpPost(payUrl);
                    ReqAccountPay reqAccountPay = new ReqAccountPay();
                    reqAccountPay.setOrderNo(orderNo);
                    String payJson = JSON.toJSONString(reqAccountPay);
                    StringEntity payEntity = new StringEntity(payJson,"utf-8");//解决中文乱码问题
                    payEntity.setContentEncoding("UTF-8");
                    payEntity.setContentType("application/json");
                    payPost.setEntity(payEntity);
                    payPost.setHeader("Authorization",token);
                    HttpResponse payHttpResponse = httpClient.execute(payPost);
                    if (payHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String payResponseJson = EntityUtils.toString(payHttpResponse.getEntity(),"utf-8");
                        ClientResp<RespOrderPay> payResponse = resolveResponse(payResponseJson,RespOrderPay.class);
                        if ("0".equals(payResponse.getResultCode())){
                            logger.info("success,orderNo:{}",orderNo);
                        }else {
                            logger.error("pay error.errorCode:{},resultDesc:{}",payResponse.getResultCode(),payResponse.getResultDesc());
                        }
                    }
                }else{
                    logger.error("创建订单失败，resultCode:{},resultDesc:{}",createOrderResponse.getResultCode(),createOrderResponse.getResultDesc());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> ClientResp<T> resolveResponse(String json,Class clazz){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String resultCode = jsonObject.getString("resultCode");
        String resultDesc = jsonObject.getString("resultDesc");
        String resultData = jsonObject.getString("resultData");
        ClientResp<T> clientResp;
        if ("0".equals(resultCode)){
            clientResp = new ClientResp<T>((T) JSONObject.parseObject(resultData,clazz));
        }else {
            clientResp = new ClientResp<T>(resultCode,resultDesc);
        }
        return clientResp;
    }
}
