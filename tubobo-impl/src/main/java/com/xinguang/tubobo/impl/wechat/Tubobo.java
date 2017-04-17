package com.xinguang.tubobo.impl.wechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.config.Global;
import com.hzmux.hzcms.common.utils.RedisUtil;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.wechat.entity.MessageSendData;
import com.xinguang.tubobo.impl.wechat.entity.WxResJsApiTicket;
import com.xinguang.tubobo.impl.wechat.entity.WxResMessageSend;
import com.xinguang.tubobo.impl.wechat.entity.WxResAccessToken;

/**
 * 微信公众号接口
 * https://mp.weixin.qq.com/wiki
 * @author ouyoung
 * @date 2027-02-20
 */
public class Tubobo{

	protected static Logger tuboboLogger = LoggerFactory.getLogger("WechatTubobo");

	public static String tubobo_id;
	public static String tubobo_secret;
	
	public static String tubobo_auth_redirect_url;
	
	public static String tubobo_index_url;
	public static String tubobo_bindPhone_url;
	public static String tubobo_expressList_url;
	public static String tubobo_expressDetail_url;
	public static String tubobo_appointTaskList_url;
	public static String tubobo_appointTaskDetail_url;

	//请求建立连接超时时间
	private static Integer CONNECTTIMEOUT = 5000;
	//请求响应数据超时时间
	private static Integer SOCKETTIMEOUT = 10000;
	
	private static String BASE_URL = "https://api.weixin.qq.com/cgi-bin/";
	
	static{
		tubobo_id = Global.getConfig("tubobo_id");
		tubobo_secret = Global.getConfig("tubobo_secret");
		try {
			tubobo_auth_redirect_url = URLEncoder.encode(Global.getConfig("tubobo_auth_redirect_url"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		tubobo_index_url = Global.getConfig("tubobo_index_url");
		tubobo_bindPhone_url = Global.getConfig("tubobo_bindPhone_url");
		tubobo_expressList_url = Global.getConfig("tubobo_expressList_url");
		tubobo_appointTaskList_url = Global.getConfig("tubobo_appointTaskList_url");
		tubobo_expressDetail_url = Global.getConfig("tubobo_expressDetail_url");
		tubobo_appointTaskDetail_url = Global.getConfig("tubobo_appointTaskDetail_url");
	}
	
	/**
	 * 获取accessToken
	 */
	private static String getAccessToken(){
		String accessToken = RedisUtil.get("tubobo_accessToken");
		if (StringUtils.isNotBlank(accessToken)) {
			return accessToken;
		}
		String url = BASE_URL + "token?grant_type=client_credential&appid=" + tubobo_id + "&secret=" + tubobo_secret;
		String responseText = get(url);

		WxResAccessToken response = JSONObject.parseObject(responseText, WxResAccessToken.class);
		if (StringUtils.isNotBlank(response.getAccess_token())) {
			RedisUtil.set("tubobo_accessToken", response.getAccess_token(), 7000);
//			RedisUtil.set("tubobo_accessToken", wechatResponseToken.getAccess_token(), Integer.valueOf(wechatResponseToken.getExpires_in()));
			return response.getAccess_token();
		}
		tuboboLogger.error("ERROR:query tubobo_accessToken, responseText:{} ",responseText);
		return "";
	}
	
	/**
	 * 获取jsapiTicket
	 */
	public static String getJsapiTicket(){
		String ticket = RedisUtil.get("tubobo_jsapiTicket");
		if (StringUtils.isNotBlank(ticket)) {
			return ticket;
		}
		String url = BASE_URL + "ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi";
		String responseText = get(url);
		WxResJsApiTicket response = JSONObject.parseObject(responseText, WxResJsApiTicket.class);
		if (StringUtils.isNotBlank(response.getTicket())) {
			RedisUtil.set("tubobo_jsapiTicket", response.getTicket(), 7000);
			return response.getTicket();
		}
		tuboboLogger.error("ERROR:query tubobo_jsapiTicket, responseText:{} ",responseText);
		return "";
	}

	/**
	 * 发送模板消息
	 * @param touser 接收者openid
	 * @param template_id 模板ID
	 * @param hrefUrl 模板跳转链接  非必须
	 * @param map 模板数据
	 */
	public static WxResMessageSend messageSend(String touser,String template_id,String hrefUrl,Map<String, MessageSendData> map){
		try {
			String url = BASE_URL + "message/template/send?access_token=" + getAccessToken();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("touser", touser);
			jsonObject.put("template_id", template_id);
			jsonObject.put("topcolor", "#FF0000");
			jsonObject.put("url", hrefUrl);
			jsonObject.put("data", map);
			String responseText = post(url,jsonObject.toJSONString());
			
			WxResMessageSend response = JSONObject.parseObject(responseText, WxResMessageSend.class);
			return response;
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 自定义菜单 创建 （会覆盖之前的设置）
	 */
	public static String menuCreate(String jsonMenus) {
		try {
			String url = BASE_URL + "menu/create?access_token=" + getAccessToken();
			return post(url,jsonMenus);
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}

	/**
	 * 自定义菜单 查询
	 */
	public static String menuQuery(){
		try {
			String url = BASE_URL + "menu/get?access_token=" + getAccessToken();
			return get(url);
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}

	/**
	 * 自定义菜单  删除
	 */
	public static String menuDelete(){
		try {
			String url = BASE_URL + "menu/delete?access_token=" + getAccessToken();
			return get(url);
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 模版消息
	 * 设置所属行业
	 * @param industry_id1
	 * @param industry_id2
	 */
	public static String templateSetIndustry(String industry_id1,String industry_id2){
		try {
			String url = BASE_URL + "template/api_set_industry?access_token=" + getAccessToken();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("industry_id1", industry_id1);
			jsonObject.put("industry_id2", industry_id2);
			return post(url,jsonObject.toJSONString());
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 模版消息
	 * 获取设置的行业信息
	 */
	public static String templateGetIndustry(){
		try {
			String url = BASE_URL + "template/get_industry?access_token=" + getAccessToken();
			return get(url);
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 模版消息
	 * 从行业模板库选择模板添加到帐号后台
	 * @param template_id_short
	 */
	public static String templateAdd(String template_id_short){
		try {
			String url = BASE_URL + "template/api_add_template?access_token=" + getAccessToken();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("template_id_short", template_id_short);
			return post(url,jsonObject.toJSONString());
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 模版消息
	 * 获取已添加至帐号下所有模板列表
	 */
	public static String templateGetAll(){
		try {
			String url = BASE_URL + "template/get_all_private_template?access_token=" + getAccessToken();
			return get(url);
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 模版消息
	 * 删除模板帐号后台已添加的模版
	 * @param template_id
	 */
	public static String templateDelete(String template_id){
		try {
			String url = BASE_URL + "template/del_private_template?access_token=" + getAccessToken();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("template_id", template_id);
			return post(url,jsonObject.toJSONString());
		} catch (Exception e) {
			tuboboLogger.error(e.getMessage());
			return "";
		}
	}

	public static String get(String url) {
    	String responseText = "";
    	CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
        	HttpGet method = new HttpGet(url);
        	method.setConfig(RequestConfig.custom().setConnectTimeout(CONNECTTIMEOUT).setSocketTimeout(SOCKETTIMEOUT).build());
        	response = httpClient.execute(method);
        	
            HttpEntity resEntity = response.getEntity();
            responseText = EntityUtils.toString(resEntity,"UTF-8");
			EntityUtils.consume(resEntity);
            tuboboLogger.info("get {}, responseText:{}",url,responseText);
            
        } catch (Exception e) {
        	tuboboLogger.error("get {} error.",url);
			tuboboLogger.error(e.toString());
			responseText = e.getMessage();
        } finally {
            try {
            	if (response != null) {
            		response.close();
				}
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        return responseText;
    }
	
	public static String post(String url,String jsonParams){
		String responseText = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			method.setConfig(RequestConfig.custom().setConnectTimeout(CONNECTTIMEOUT).setSocketTimeout(SOCKETTIMEOUT).build());
			if (StringUtils.isNotBlank(jsonParams)) {
				StringEntity reqEntity = new StringEntity(jsonParams,"UTF-8");
				reqEntity.setContentType("application/json");
				method.setEntity(reqEntity); 
			}
			response = httpClient.execute(method);
			
			HttpEntity resEntity = response.getEntity();
			responseText = EntityUtils.toString(resEntity,"UTF-8");
			EntityUtils.consume(resEntity);
			tuboboLogger.info("post {}, requesetParams:{}, responseText:{} ",url,jsonParams,responseText);

		} catch (Exception e) {
			tuboboLogger.error("post {} error, requesetParams:{}. ",url,jsonParams);
			tuboboLogger.error(e.toString());
			responseText = e.getMessage();
		}
		if(null !=response){
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}
}