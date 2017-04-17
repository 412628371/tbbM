package com.xinguang.tubobo.impl.wechat;

import java.util.HashMap;
import java.util.Map;

import com.xinguang.tubobo.impl.wechat.entity.MessageSendData;

public class TuboboUtils {

	/**
	 * 自提通知
		{{first.DATA}}
		运单号：{{keyword1.DATA}}
		快递公司：{{keyword2.DATA}}
		提货码：{{keyword3.DATA}}
		地址：{{keyword4.DATA}}
		取件时间：{{keyword5.DATA}}
		{{remark.DATA}}
	 */
	public static void pushZitiNotice(String openId,String waybillNo,String expressName,String areaNum,String storeName,String detailAddress,String href) {
		Map<String, MessageSendData> map = new HashMap<>();
		map.put("first", new MessageSendData("亲，您有快递到达" + storeName, "#173177"));
		map.put("keyword1", new MessageSendData(waybillNo, "#173177"));
		map.put("keyword2", new MessageSendData(expressName, "#173177"));
		map.put("keyword3", new MessageSendData(areaNum, "#173177"));
		map.put("keyword4", new MessageSendData(detailAddress, "#173177"));
		map.put("keyword5", new MessageSendData("当日 18:00 前", "#173177"));
		map.put("remark", new MessageSendData("请务必当日领取，谢谢！", "#173177"));
		Tubobo.messageSend(openId, "CNg2IoJJqRMtvOTypZu7pHyMIp9udUhc7XkCmi4ASVY", href, map);
	}

	/**
	 * 配送通知 与自提模版一致
		{{first.DATA}}
		运单号：{{keyword1.DATA}}
		快递公司：{{keyword2.DATA}}
		提货码：{{keyword3.DATA}}
		地址：{{keyword4.DATA}}
		取件时间：{{keyword5.DATA}}
		{{remark.DATA}}
	 */
//	public static void pushDaodianPeisongNotice(String openId,String waybillNo,String expressName,String storeName,String detailAddress,String href) {
//		Map<String, MessageSendData> map = new HashMap<>();
//		map.put("first", new MessageSendData("亲，您有快递到达" + storeName, "#173177"));
//		map.put("keyword1", new MessageSendData(waybillNo, "#173177"));
//		map.put("keyword2", new MessageSendData(expressName, "#173177"));
//		map.put("keyword3", new MessageSendData("", "#173177"));
//		map.put("keyword4", new MessageSendData(detailAddress, "#173177"));
//		map.put("keyword5", new MessageSendData("当日 18:00 前", "#173177"));
//		map.put("remark", new MessageSendData("我们会尽快为您安排快递员进行配送！", "#173177"));
//		Tubobo.messageSend(openId, "CNg2IoJJqRMtvOTypZu7pHyMIp9udUhc7XkCmi4ASVY", href, map);
//	}

	/**
	 * 配送通知
		{{first.DATA}}
		快递公司：{{keyword1.DATA}}
		快递单号：{{keyword2.DATA}}
		{{remark.DATA}}
	 */
//	public static void pushDaodianPeisongNotice(String openId,String waybillNo,String expressName,String storeName,String detailAddress,String href) {
//		Map<String, MessageSendData> map = new HashMap<>();
//		map.put("first", new MessageSendData("亲，您有快递到达" + storeName, "#173177"));
//		map.put("keyword1", new MessageSendData(expressName, "#173177"));
//		map.put("keyword2", new MessageSendData(waybillNo, "#173177"));
//		map.put("remark", new MessageSendData("请务必当日领取，谢谢！", "#173177"));
//		Tubobo.messageSend(openId, "F3weHvjU-rlGmp4RiV8RBWPbjJ7cipB0ZBEHGA8EXhc", href, map);
//	}


	/**
	 * 快递员上门取件通知
		{{first.DATA}}
		取件员：{{keyword1.DATA}}
		联系电话：{{keyword2.DATA}}
		{{remark.DATA}}
	 */
	public static void pushShangmenQujianNotice(String openId,String name,String phone,String href) {
		Map<String, MessageSendData> map = new HashMap<>();
		map.put("first", new MessageSendData("亲，我们的快递员正朝您飞奔来，请耐心等待！", "#173177"));
		map.put("keyword1", new MessageSendData(name, "#173177"));
		map.put("keyword2", new MessageSendData(phone, "#173177"));
		map.put("remark", new MessageSendData("", "#173177"));
		Tubobo.messageSend(openId, "IePJKVMcUDfqxbhaLC6_EO9SylkHY48t6bgRUbmvjE0", href, map);
	}

    /**
     * 签收后 通知用户服务评价
     * {{first.DATA}}
     快件{{billCode.DATA}}已经签收。
     {{remark.DATA}}
     */
    public static void pushEvaluateNoticeAfterSign(String openId,String waybillNo,String href) {
        Map<String, MessageSendData> map = new HashMap<>();
        map.put("first", new MessageSendData("您好，", "#173177"));
        map.put("billCode", new MessageSendData(waybillNo, "#173177"));
        map.put("remark", new MessageSendData("您的评价很重要，请猛击详情进行服务评价！", "#173177"));
        Tubobo.messageSend(openId, "an5am4pD8-0lweF7hdpH5t9P9BIdUdi7HwVjvNwIeok", href, map);
    }

	/**
	 * 收件成功后 通知用户服务评价
		{{first.DATA}}
		运单号：{{waybillNo.DATA}}
		{{remark.DATA}}
	 */
	public static void pushEvaluateNoticeAfterRuku(String openId,String waybillNo,String href) {
		Map<String, MessageSendData> map = new HashMap<>();
		map.put("first", new MessageSendData("亲，您的快件已收件成功！", "#173177"));
		map.put("waybillNo", new MessageSendData(waybillNo, "#173177"));
		map.put("remark", new MessageSendData("您的评价很重要，请猛击详情进行服务评价！", "#173177"));
		Tubobo.messageSend(openId, "YJ89T3zvpLXivbN_wY4RQnfpyYmFj445Bl6JzmOeZQ8", href, map);
	}


	/**
	 * 通知用户进行付款
	 * {{first.DATA}}
     订单编号：{{keyword1.DATA}}
     快运单号：{{keyword2.DATA}}
     支付金额：{{keyword3.DATA}}
     {{remark.DATA}}
	 */
	public static void pushPayNotice(String openId,String waybillNo,String money,String href) {
		Map<String, MessageSendData> map = new HashMap<>();
		map.put("first", new MessageSendData("你好，您的快件已完成揽件，", "#173177"));
		map.put("keyword1", new MessageSendData("", "#173177"));
		map.put("keyword2", new MessageSendData(waybillNo, "#173177"));
		map.put("keyword3", new MessageSendData(money, "#173177"));
		map.put("remark", new MessageSendData("请点击进行支付，谢谢！", "#173177"));
		Tubobo.messageSend(openId, "DkoHgUdnvUWN0og7Kr6JZB1YXfY7OjaauZjvqk8FhuE", href, map);
	}

    public static void main(String[] args) {
    	
//    	TuboboUtils.pushDaodianZitiNotice("oQOWcxIv0NEweMlcqNLgLBGfohIg", "waybillNo", "expressName", "areaNum", "storeName", "detailAddress","");
    	
//    	System.out.println(Tubobo.getJsapiTicket());
//    	System.out.println(Tubobo.menuQuery());

//    	String jsonMenus = "{\"button\":[{\"name\":\"我的\",\"sub_button\":[{\"type\":\"view\",\"name\":\"收件\",\"url\":\"" + Tubobo.tubobo_expressList_url + "\"},"
//    			+ "{\"type\":\"view\",\"name\":\"寄件\",\"url\":\"" + Tubobo.tubobo_appointTaskList_url + "\"}]},{\"type\":\"view\",\"name\":\"兔波波\",\"url\":\"" + Tubobo.tubobo_index_url + "\"}]}";
		String jsonMenus = "{\n" +
				"    \"button\": [\n" +
				"        {\n" +
				"            \"type\": \"view\",\n" +
				"            \"name\": \"我的快递\",\n" +
				"            \"url\": \"" + Tubobo.tubobo_expressList_url + "\"\n" +
				"        },\n" +
				"        {\n" +
				"            \"type\": \"view\",\n" +
				"            \"name\": \"我要寄件\",\n" +
				"            \"url\": \"" + Tubobo.tubobo_appointTaskList_url + "\"\n" +
				"        },\n" +
				"        {\n" +
				"            \"type\": \"view\",\n" +
				"            \"name\": \"兔波波\",\n" +
				"            \"url\": \"" + Tubobo.tubobo_index_url + "\"\n" +
				"        }\n" +
				"    ]\n" +
				"}";
		System.out.println(Tubobo.menuCreate(jsonMenus));

//		System.out.println(Tubobo.templateSetIndustry("13","14"));
    	
//		System.out.println(Tubobo.templateGetIndustry());
		
//		System.out.println(Tubobo.templateAdd("TM00015"));

//		System.out.println(Tubobo.templateGetAll());
		
//		System.out.println(Tubobo.templateDelete("Dyvp3-Ff0cnail_CDSzk1fIc6-9lOkxsQE7exTJbwUE"));
		
//		{
//	           "touser":"OPENID",
//	           "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
//	           "url":"http://weixin.qq.com/download",            
//	           "data":{
//	                   "first": {
//	                       "value":"恭喜你购买成功！",
//	                       "color":"#173177"
//	                   },
//	                   "keynote1":{
//	                       "value":"巧克力",
//	                       "color":"#173177"
//	                   },
//	                   "keynote2": {
//	                       "value":"39.8元",
//	                       "color":"#173177"
//	                   },
//	                   "keynote3": {
//	                       "value":"2014年9月22日",
//	                       "color":"#173177"
//	                   },
//	                   "remark":{
//	                       "value":"欢迎再次购买！",
//	                       "color":"#173177"
//	                   }
//	           }
//	       }
//    	Map<String, MessageSendData> map = new HashMap<>();
//    	map.put("first", new MessageSendData("恭喜你购买成功！", "#173177"));
//    	map.put("keynote1", new MessageSendData("巧克力", "#173177"));
//    	map.put("remark", new MessageSendData("欢迎再次购买！", "#173177"));
//		System.out.println(Tubobo.messageSend("touser", "template_id", "", map));
    }


}
