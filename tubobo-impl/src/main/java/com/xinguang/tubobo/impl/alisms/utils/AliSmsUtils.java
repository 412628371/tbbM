package com.xinguang.tubobo.impl.alisms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.config.Global;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.SpringContextHolder;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;
import com.xinguang.tubobo.impl.alisms.service.AliSmsRecordService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.BizResult;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;
import com.taobao.api.request.AlibabaAliqinFcSmsNumQueryRequest;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.request.TmcGroupAddRequest;
import com.taobao.api.request.TmcGroupsGetRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumQueryResponse;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.taobao.api.response.TmcGroupAddResponse;
import com.taobao.api.response.TmcGroupsGetResponse;

/**
 * 阿里大于短信工具类
 */
public class AliSmsUtils {

	private static Logger logger = LoggerFactory.getLogger("AliSms");
	
	private static final SimpleDateFormat dateFormart = new SimpleDateFormat("yyyyMMdd");

	private static Boolean ALISMS_FLAG;
	private static String ALISMS_URL;
	private static String ALISMS_APIKEY;
	private static String ALISMS_SECRET;

	// 是否接受消息的回调推送
	private static Boolean ALI_MESSAGE_HANDLER_FLAG;
	// 消息回调推送的接收分组
//    private static final String ALI_MESSAGE_HANDLER_GROUP;
	private static String ALI_MESSAGE_HANDLER_URL;

	static {
		ALISMS_FLAG = Boolean.valueOf(Global.getConfig("alisms_flag"));
		ALISMS_URL = Global.getConfig("alisms_url");
		ALISMS_APIKEY = Global.getConfig("alisms_apikey");
		ALISMS_SECRET = Global.getConfig("alisms_secret");

		// 是否接受消息的回调推送
		ALI_MESSAGE_HANDLER_FLAG = Boolean.valueOf(Global.getConfig("alisms_message_handler_flag"));
		// 消息回调推送的接收分组
		// ALI_MESSAGE_HANDLER_GROUP = Global.getConfig("alisms_message_handler_group");
		ALI_MESSAGE_HANDLER_URL = Global.getConfig("alisms_message_handler_url");
	}

    // 淘宝服务客户端
    private static final TaobaoClient client = new DefaultTaobaoClient(ALISMS_URL, ALISMS_APIKEY, ALISMS_SECRET);

    // 消息服务客户端
    private static final TmcClient tmcClient = new TmcClient(ALISMS_APIKEY, ALISMS_SECRET);
    
    private static final AliSmsRecordService aliSmsRecordService = SpringContextHolder.getBean(AliSmsRecordService.class);
    
    public static void main(String[] args) {
    	
    	AliSmsRecordEntity entity = new AliSmsRecordEntity();
    	entity.setPhone("18158512014");
//    	entity.setAliSignName("问问服务");
//    	entity.setAliSmsId("SMS_25285180");
//    	entity.setJsonParams("{\"code\":\"123456\"}");
//    	entity.setStoreId("extend_storeId");
    	entity.setBizId("105211457805^1107157667426");
    	entity.setUpdateDate(DateUtils.addDays(new Date(), -2));
//    	AlibabaAliqinFcSmsNumSendResponse response = sendAliSms(entity);
    	AlibabaAliqinFcSmsNumQueryResponse response = queryAliSms(entity);
    	System.out.println(response.getBody());
//    	File file = new File("C:/Users/oU_Young/Desktop/20161112IdList.txt");
//    	
//    	List<String> ids = aliSmsRecordService.queryTodaySendedAliSmsRecords("", "",Constants.ALISMS_STATUS_EXCEPTION);
//    	System.out.println("idList size :" + ids.size());
//    	System.out.println("export size :" + exportFile(file, ids));
	}
    
    /**
     * 发送短信
     * @param aliSmsId 阿里短信模版
     * @param aliSmsSignName 阿里短信签名
     * @param phone 手机号
     * @param jsonParams 格式: {"param1":"aaa","param2":"bbb"}
     * @param extend 可为空 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
     */
//    public static AlibabaAliqinFcSmsNumSendResponse sendAliSms(String aliSmsId,String aliSmsSignName,String phone,String jsonParams,String extend){
	public static AlibabaAliqinFcSmsNumSendResponse sendAliSms(AliSmsRecordEntity entity){
    	AlibabaAliqinFcSmsNumSendResponse res = null;
        if (ALISMS_FLAG) {
        	AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setSmsType("normal");
            req.setSmsTemplateCode(entity.getAliSmsId());
            req.setSmsFreeSignName(entity.getAliSignName());
            req.setRecNum(entity.getPhone());
            req.setSmsParamString(entity.getJsonParams());
            req.setExtend(entity.getStoreId());
			try {
				res = client.execute(req);
				logger.info("sendAliSms callback phone:{} responseBody:{};",entity.getPhone(),res.getBody());
			} catch (ApiException e) {
				logger.error(e.toString());
				res = new AlibabaAliqinFcSmsNumSendResponse();
				res.setErrorCode(e.getErrCode());
				res.setMsg(e.getErrMsg());
			}
		}else {
			res = new AlibabaAliqinFcSmsNumSendResponse();
			BizResult result = new BizResult();
			result.setSuccess(true);
			result.setErrCode("DEFAULT_SUCCESS");
			result.setModel("");
			result.setMsg("默认成功");
			res.setResult(result);
		}
        // TODO need this step ???
        // 阿里短信回调通知之前 先更新短信状态为等待发送
		int i = aliSmsRecordService.updateBySendResponse(entity.getId(), res);
		if (i > 0 && res.getResult() != null && res.getResult().getSuccess() && StringUtils.isNotBlank(res.getResult().getModel())) {
			// 回调处理 更新短信为最终状态
			messageHandler();
		}
        return res;
    }
    
    /**
     * 查询短信
     * @param bizId     (可选)短信发送流水 "1234^1234" 
     * @param phone     (必须)短信接收号码 
     * @param queryDate (必须)短信发送日期，支持近30天记录查询，格式yyyyMMdd "20151215"
     * @param pageNo    (必须)分页参数,页码
     * @param pageSize  (必须)分页参数，每页数量。最大值50
     */
//    public static AlibabaAliqinFcSmsNumQueryResponse queryAliSms(String bizId,String phone,String queryDate,int pageNo,int pageSize){
	public static AlibabaAliqinFcSmsNumQueryResponse queryAliSms(AliSmsRecordEntity entity){
    	AlibabaAliqinFcSmsNumQueryResponse res = null;
    	AlibabaAliqinFcSmsNumQueryRequest req = new AlibabaAliqinFcSmsNumQueryRequest();
    	req.setBizId(entity.getBizId());
    	req.setRecNum(entity.getPhone());
    	req.setQueryDate(formartToYYYYMMdd(entity.getUpdateDate()));
    	req.setCurrentPage(1L);
    	req.setPageSize(1L);
    	try {
    		res = client.execute(req);
    		logger.info("queryAliSms callback responseBody:{};", res.getBody());
    	} catch (ApiException e) {
    		logger.error(e.toString());
    		res = new AlibabaAliqinFcSmsNumQueryResponse();
    		res.setErrorCode(e.getErrCode());
    		res.setMsg(e.getErrMsg());
    	}
		// 更新短信状态
		aliSmsRecordService.updateByQueryResponse(entity.getId(),res);
    	return res;
    }
    
	/**
	 * 消息通道客户端收到消息后，会回调该方法处理具体的业务，处理结果可以通过以下两种方式来表述：
	 * <ul>
	 * <li>抛出异常或设置status.fail()表明消息处理失败，需要消息通道服务端重发
	 * <li>不抛出异常，也没有设置status信息，则表明消息处理成功，消息通道服务端不会再投递此消息
	 * 
	 * @param message 消息内容
	 * @param status 处理结果，如果调用status.fail()，消息通道将会择机重发消息；否则，消息通道认为消息处理成功
	 * @throws Exception 消息处理失败，消息通道将会择机重发消息
	 */
    private static void messageHandler() {
    	if (ALI_MESSAGE_HANDLER_FLAG) {
    		tmcClient.setMessageHandler(new MessageHandler() {
    		    public void onMessage(Message message, MessageStatus status) {
    		        try {
//    		        	alibaba_aliqin_FcSmsDR {"send_time":"2016-11-09 15:19:14","err_code":"DELIVRD","biz_id":"104471625847^1106086025583","receiver":"13957185373","extend":"211f520b1ab0490f9fab8723c2787ab3","rept_time":"2016-11-09 15:19:36","state":"1"}
    		            AliMessageHandlerResponse response = JSONObject.parseObject(message.getContent(), AliMessageHandlerResponse.class);
    		            logger.info("sendAliSms messageHandler notify phone:{} messageContent:{} ", response.getReceiver(),message.getContent());
    		            aliSmsRecordService.updateByAliMessageHandlerResponse(response);
    		        } catch (Exception e) {
    		        	logger.info("sendAliSms messageHandler notify messageContent:{} ", message.getContent());
    		        	logger.error(e.toString());
    		        }
    		    }
    		});
    		try {
    			tmcClient.connect(ALI_MESSAGE_HANDLER_URL);
    		} catch (LinkException e) {
    			logger.error(e.toString());
    			e.printStackTrace();
    		}
		}
	}
    
    private static String formartToYYYYMMdd(Date date){
    	if (date == null) {
			return "";
		}
    	return dateFormart.format(date);
    }
    
    /**
     * 要查询分组的名称，多个分组用半角逗号分隔，不传代表查询所有分组信息
     */
    @SuppressWarnings("unused")
	private static void tmcGroupsGet(String groupNames){
    	TmcGroupsGetRequest req = new TmcGroupsGetRequest();
    	req.setGroupNames(groupNames);
    	req.setPageNo(1L);
    	req.setPageSize(40L);
    	TmcGroupsGetResponse rsp;
    	try {
    		rsp = client.execute(req);
    		System.out.println(rsp.getBody());
    	} catch (ApiException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    /**
     * GroupName(必须):分组名称，同一个应用下需要保证唯一性，最长32个字符。
     * 添加分组后，消息通道会为用户的消息分配独立分组，但之前的消息还是存储于默认分组中。
     * 不能以default开头，default开头为系统默认组。
     * Nicks(必须):用户昵称列表，以半角逗号分隔，支持子账号，支持增量添加用户
     * UserPlatform: 默认值：tbUIC
     */
    @SuppressWarnings("unused")
	private static void tmcGroupsAdd(String groupNames,String nicks){
    	TmcGroupAddRequest req = new TmcGroupAddRequest();
    	req.setGroupName(groupNames);
    	req.setNicks(nicks);
    	req.setUserPlatform("tbUIC");
    	TmcGroupAddResponse rsp;
		try {
			rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
