package com.xinguang.tubobo.impl.redisCache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.RedisUtil;
import com.hzmux.hzcms.common.utils.SpringContextHolder;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.entity.Area;
import com.hzmux.hzcms.modules.sys.service.AreaService;
import com.xinguang.tubobo.api.baseDTO.DictItem;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;
import com.xinguang.tubobo.impl.alisms.service.AliSmsTemplateService;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.ExpressCompanyEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.service.EmployeeService;
import com.xinguang.tubobo.impl.service.ExpressCompanyService;
import com.xinguang.tubobo.impl.service.StoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class RedisCache {
	
	/**redis 待处理运单队列队列**/
	public static final String REDIS_QUEUE_UPLOAD_WAYBILL = "REDIS_QUEUE_UPLOAD_WAYBILL";
	public static final String REDIS_QUEUE_ALISMS = "REDIS_QUEUE_ALISMS";
	public static final String REDIS_QUEUE_WECHAT_MSG = "REDIS_QUEUE_WECHAT_MSG";

	//门店
	private static String STORE_LIST = "store_list_";
	private static String STORE_NAME = "store_name_";
	
	//快递公司
	private static String EXPRESS_COMPANY_LIST = "express_company_list_";
	private static String EXPRESS_COMPANY_NAME = "express_company_name_";

	//门店雇员列表
	private static String EMPLOYEE_LIST = "employee_list_";
	
	//阿里短信模版
	private static String ALI_SMS_TEMPLATE = "ali_sms_template_";
	//省市区json
	private static String PCD_JSON = "pcd_josn";
	
	private static StoreService storeService = SpringContextHolder.getBean(StoreService.class);
	private static EmployeeService employeeService = SpringContextHolder.getBean(EmployeeService.class);
	private static ExpressCompanyService expressCompanyService = SpringContextHolder.getBean(ExpressCompanyService.class);
	private static AreaService areaService = SpringContextHolder.getBean(AreaService.class);
	private static AliSmsTemplateService aliSmsTemplateService = SpringContextHolder.getBean(AliSmsTemplateService.class);

	/**
	 * 根据区域码查询门店列表
	 */
	@SuppressWarnings("unchecked")
	public static List<DictItem> queryStoreListByAreaCode(String areaCode){
		String key = STORE_LIST + "areaCode_" + areaCode;
		List<DictItem> list = RedisUtil.get(key,List.class);
        if (null != list && list.size() > 0) {
            return list;
        }
        list = new ArrayList<DictItem>();
		List<StoreEntity> storeList = storeService.findByAreaCode(areaCode);
		if (storeList != null && storeList.size() > 0) {
			for (StoreEntity entity : storeList){
				list.add(new DictItem(entity.getStoreName(), entity.getStoreId()));
			}
		}
		RedisUtil.set(key, list, 60*60*24);
		return list;
	}

	/**
	 * 根据officeId查询门店列表
	 */
	@SuppressWarnings("unchecked")
	public static List<DictItem> queryStoreListByOfficeId(String officeId){
		String key = STORE_LIST + "storeId_" + officeId;
		List<DictItem> list = RedisUtil.get(key,List.class);
		if (null != list && list.size() > 0) {
			return list;
		}
		list = new ArrayList<DictItem>();
		
		StoreEntity store = storeService.get(officeId);
		if (store != null) {
			list.add(new DictItem(store.getStoreName(), store.getStoreId()));
		}else {
			List<StoreEntity> storeList = storeService.findByParanteId(officeId);
			if (storeList != null && storeList.size() > 0) {
				for (StoreEntity entity : storeList){
					list.add(new DictItem(entity.getStoreName(), entity.getStoreId()));
				}
			}
		}
		RedisUtil.set(key, list, 60*60*24);
		return list;
	}
	
	public static void removeStoreListCache(){
		Set<String> list = RedisUtil.keys(STORE_LIST+"*");
		if (list != null && list.size() > 0) {
			for (String key : list) {
				RedisUtil.del(key);
			}
		}
	}
	
	/**
	 * 根据officeId查询门店雇员列表
	 */
	@SuppressWarnings("unchecked")
	public static List<DictItem> queryEmployeeListByOfficeId(String officeId){
		String key = EMPLOYEE_LIST+officeId;
		List<DictItem> list = RedisUtil.get(key,List.class);
		if (null != list && list.size() > 0) {
			return list;
		}
		list = new ArrayList<DictItem>();
		
		StoreEntity store = storeService.get(officeId);
		if (store != null) {
			List<EmployeeEntity> employeeList = employeeService.findEmployeeByStoreId(officeId);
			if (employeeList != null && employeeList.size() > 0) {
				for (EmployeeEntity entity : employeeList){
					list.add(new DictItem(entity.getName(), entity.getId()));
				}
			}
		}else {
			List<StoreEntity> storeList = storeService.findByParanteId(officeId);
			if (storeList != null && storeList.size() > 0) {
				for (StoreEntity entity : storeList){
					List<EmployeeEntity> employeeList = employeeService.findEmployeeByStoreId(entity.getStoreId());
					if (employeeList != null && employeeList.size() > 0) {
						for (EmployeeEntity employeeEntity : employeeList){
							list.add(new DictItem(employeeEntity.getName(), employeeEntity.getId()));
						}
					}
				}
			}
		}
		RedisUtil.set(key, list, 60*60*24);
		return list;
	}
	
	public static void removeEmployeeListCache(String storeId){
		RedisUtil.del(EMPLOYEE_LIST + storeId);
	}
	
	/**
	 * 查询门店开通的快递公司列表
	 */
	@SuppressWarnings("unchecked")
	public static List<ExpressCompanyEntity> queryExpressCompanyListByStoreId(String storeId){
		String key = EXPRESS_COMPANY_LIST+storeId;
		List<ExpressCompanyEntity> list = RedisUtil.get(key,List.class);
		if (null != list && list.size() > 0) {
			return list;
		}
		StoreEntity store = storeService.get(storeId);
		if (store != null) {
			list = store.getExpressCompanyList();
		}else {
			list = expressCompanyService.findAll();
		}
		RedisUtil.set(key, list, 60*60*24);
		return list;
	}

	public static void removeExpressCompanyListCache(String storeId){
		if (StringUtils.isNotBlank(storeId)) {
			RedisUtil.del(EXPRESS_COMPANY_LIST+storeId);
		}else {
			Set<String> list = RedisUtil.keys(EXPRESS_COMPANY_LIST+"*");
			if (list != null && list.size() > 0) {
				for (String key : list) {
					RedisUtil.del(key);
				}
			}
		}
	}
	
	/**
	 * 查询门店名称
	 */
	public static String queryStoreName(String storeId){
		if (StringUtils.isBlank(storeId)) {
			return "";
		}
	    String key = STORE_NAME + storeId;
	    String name = RedisUtil.get(key);
	    if (StringUtils.isNotBlank(name)) {
	        return name;
	    }
	    
	    StoreEntity store = storeService.get(storeId);
	    if (store != null) {
	        name = store.getStoreName();
	        RedisUtil.set(key, name, 60*60*24);
	    }
	    return name;
	}
	
	/**
	 * 查询快递公司名字
	 */
	public static String queryExpressCompanyName(String expressCompanyId){
		if (StringUtils.isBlank(expressCompanyId)) {
			return "";
		}
		String key = EXPRESS_COMPANY_NAME+expressCompanyId;
		String name = RedisUtil.get(key);
		if (StringUtils.isNotBlank(name)) {
			return name;
		}
		ExpressCompanyEntity expressCompany = expressCompanyService.get(expressCompanyId);
		if (expressCompany != null) {
			name = expressCompany.getCompanyName();
			RedisUtil.set(key, name, 60*60*24);
		}
		return name;
	}

	/**
	 * 查询阿里短信模版
	 */
	public static AliSmsTemplateEntity queryAliSmsTemplate(String smsType){
		String key = ALI_SMS_TEMPLATE+smsType;
		AliSmsTemplateEntity entity = RedisUtil.get(key,AliSmsTemplateEntity.class);
		if (null != entity) {
            return entity;
        }
		entity = aliSmsTemplateService.findOneBySmsType(smsType);
		RedisUtil.set(key, entity, 60*60*24);
		return entity;
	}

	/**
	 * 获取省市区 Json
	 */
	public static String queryPcdJson(){
		String key = PCD_JSON;
		String json = RedisUtil.get(key);
        if (StringUtils.isNotBlank(json)) {
            return json;
        }
		List<JSONObject> list = new ArrayList<JSONObject>();
		List<Area> sourcelist = areaService.findAll();
		sortPcdJsonList(list, sourcelist, "1");//省的prarentId="1"
		json = JSON.toJSONString(list);
		RedisUtil.set(key, json, 60*60*24);
		return json;
	}

	/**
	 * 检查当天是否可以发送短信
	 */
	public static boolean tuboboUserSmsLimit(String phone) {
		String key = "tuboboUserSmsLimit_" + phone;
		Integer num = RedisUtil.get(key,Integer.class);
		if (num  == null){
			num = 0;
		}
		if (num > 5) {
			return false;
		}
		int time = (int)(DateUtils.getTodayEnd().getTime() - new Date().getTime())/1000;
		RedisUtil.set(key, num+1, time);
		return true;
	}

	private static void sortPcdJsonList(List<JSONObject> list, List<Area> sourcelist, String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Area e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId().equals(parentId)) {
				List<JSONObject> sublist = new ArrayList<JSONObject>();
				JSONObject json = new JSONObject();
				
				json.put("code", e.getCode());
				json.put("parent", e.getName());
				json.put("child", sublist);
				list.add(json);
				
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Area childe = sourcelist.get(j);
					if (childe.getParent() != null && childe.getParent().getId().equals(e.getId())) {
						sortPcdJsonList(sublist, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

}
