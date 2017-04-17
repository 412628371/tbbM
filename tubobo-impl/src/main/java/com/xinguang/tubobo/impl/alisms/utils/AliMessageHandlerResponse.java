package com.xinguang.tubobo.impl.alisms.utils;


public class AliMessageHandlerResponse {

//	{"send_time":"2016-11-09 15:19:14","err_code":"DELIVRD","biz_id":"104471625847^1106086025583","receiver":"13957185373","extend":"211f520b1ab0490f9fab8723c2787ab3","rept_time":"2016-11-09 15:19:36","state":"1"}
//	alibaba_aliqin_FcSmsDR
	
	private String biz_id;   // 任务主键(不为空)
	private String state;   // 状态 1：成功，2：失败(不为空)
	private String err_code; // 运营商返回码  每个运营商返回不同(0 000 DELIVED...为成功)
	private String send_time;// 发送时间
	private String rept_time;// 报告时间
	private String receiver;// 短信接收号码
	private String extend;  // 扩展字段回传，调用api时传入，消息通知原样返回
	
	public String getBiz_id() {
		return biz_id;
	}
	public String getState() {
		return state;
	}
	public String getErr_code() {
		return err_code;
	}
	public String getSend_time() {
		return send_time;
	}
	public String getRept_time() {
		return rept_time;
	}
	public String getReceiver() {
		return receiver;
	}
	public String getExtend() {
		return extend;
	}
	public void setBiz_id(String biz_id) {
		this.biz_id = biz_id;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public void setRept_time(String rept_time) {
		this.rept_time = rept_time;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	
}
