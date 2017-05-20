package com.xinguang.tubobo.impl.merchant.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by shade on 2017/4/10.
 */
@Component
public class PoolHttpsClientService {

    private static Logger LOGGER = LoggerFactory.getLogger(PoolHttpsClientService.class);

    private static final String CHAR_SET = "UTF-8";

    /**
     * 最大连接数400
     */
    private static int MAX_CONNECTION_NUM = 400;


    /**
     * 单路由最大连接数80
     */
    private static int MAX_PER_ROUTE = 80;


    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 2000;


    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 2000;

    private static PoolHttpsClientService poolHttpsClientService;

    public static PoolHttpsClientService getInstance() {
        if (null == poolHttpsClientService) {
            poolHttpsClientService = new PoolHttpsClientService();
        }
        return poolHttpsClientService;
    }

    /**
     * 构造函数
     */
    private PoolHttpsClientService() {
    }


    private static Object LOCAL_LOCK = new Object();

    /**
     * 连接池管理对象
     */
    PoolingHttpClientConnectionManager cm = null;


    /**
     *
     * 功能描述: <br>
     * 初始化连接池管理对象
     *
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private PoolingHttpClientConnectionManager getPoolManager() {
        if (null == cm) {
            synchronized (LOCAL_LOCK) {
                if (null == cm) {
                    SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                    try {
                        sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                                sslContextBuilder.build());
                        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                                .register("https", socketFactory)
                                .register("http", new PlainConnectionSocketFactory())
                                .build();
                        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                        cm.setMaxTotal(MAX_CONNECTION_NUM);
                        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
                    } catch (Exception e) {
                        LOGGER.error("init PoolingHttpClientConnectionManager Error" + e);
                    }


                }
            }
        }
        return cm;
    }


    /**
     * 创建线程安全的HttpClient
     *
     * @param config 客户端超时设置
     *
     * @return
     */
    public CloseableHttpClient getHttpsClient(RequestConfig config) {
        final String methodName = "getHttpsClient";
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
                .setConnectionManager(this.getPoolManager())
                .build();
        return httpClient;
    }


    public <T> T get(Class<T> cls,String url){
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        try{
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(SERVER_REQUEST_TIME_OUT).setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            LOGGER.info(" get request from 高德,url:{}",url);
            response = getHttpsClient(requestConfig).execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            LOGGER.info("get request from 高德,url:{},return status:{}" ,url, status);
            String result = null;
            if (status == 200) {
                result = EntityUtils.toString(response.getEntity(), CHAR_SET);
            }
            EntityUtils.consume(response.getEntity());
            response.close();
            return JSONObject.parseObject(result,cls);
        }catch (Exception e){
            if (e instanceof SocketTimeoutException) {
                // 服务器请求超时
                LOGGER.error("server request time out");
            } else if (e instanceof ConnectTimeoutException) {
                // 服务器响应超时(已经请求了)
                LOGGER.error("server response time out");
            }
            LOGGER.error(e.getMessage());
        }finally {
            httpGet.releaseConnection();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return null;
    }
//    /**
//     * Https post请求
//     *
//     * @param req
//     * @param cls
//     * @param <T>
//     *
//     * @return
//     */
//    public <T> T post(Object req, Class<T> cls,String url) {
//        CloseableHttpResponse response = null;
//        HttpPost post = null;
//        try {
//            // connectTimeout设置服务器请求超时时间
//            // socketTimeout设置服务器响应超时时间
//            RequestConfig requestConfig = RequestConfig.custom()
//                    .setSocketTimeout(SERVER_REQUEST_TIME_OUT).setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
//            post = new HttpPost(url);
//            post.setConfig(requestConfig);
//
//
//            if (req != null) {
//                String jsonParams = JSONObject.toJSONString(req);
//                StringEntity se = new StringEntity(jsonParams, CHAR_SET);
//                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;"));
//                post.setEntity(se);
//            }
//
//
//            LOGGER.info("start post to usercenter");
//            response = getHttpsClient(requestConfig).execute(post);
//            LOGGER.info("end post to usercenter");
//            int status = response.getStatusLine().getStatusCode();
//            LOGGER.info("return status:" + status);
//
//
//            String result = null;
//            if (status == 200) {
//                result = EntityUtils.toString(response.getEntity(), CHAR_SET);
//            }
//            EntityUtils.consume(response.getEntity());
//            response.close();
//            return JSONObject.parseObject(result,cls);
//        } catch (Exception e) {
//            if (e instanceof SocketTimeoutException) {
//                // 服务器请求超时
//                LOGGER.error("server request time out");
//            } else if (e instanceof ConnectTimeoutException) {
//                // 服务器响应超时(已经请求了)
//                LOGGER.error("server response time out");
//            }
//            LOGGER.error(e.getMessage());
//        } finally {
//            post.releaseConnection();
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                    response.close();
//                } catch (IOException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        // 超时或者网络不通时返回值
//        return null;
//    }
}