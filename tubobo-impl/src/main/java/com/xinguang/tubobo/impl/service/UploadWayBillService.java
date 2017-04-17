package com.xinguang.tubobo.impl.service;

import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.RedisUtil;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.dao.UploadWayBillDAO;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.entity.UploadWayBillEntity;
import com.xinguang.tubobo.impl.redisCache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadWayBillService extends BaseService {

	@Autowired
	private UploadWayBillDAO uploadWayBillDAO;

	public long count() {
		return uploadWayBillDAO.count();
	}
	
	public List<String> findIds() {
		return uploadWayBillDAO.findIds();
	}

	public UploadWayBillEntity get(String id) {
		return uploadWayBillDAO.get(id);
	}

	public void deleteById(String id) {
		uploadWayBillDAO.deleteById(id);
	}

	/**
	 * 保存上传运单信息
	 * 
	 * @param store
	 * @param employee
	 * @param uploadType     0:自提件 1:配送件 2:滞留件
	 * @param sendFlag       false:默认入库;true:直接出库配送
	 * @param aliSmsId       阿里短信模版id
	 * @param aliSmsSignName 阿里短信签名
	 * @param waybills       运单信息
	 * @return
	 */
	public EnumRespCode saveUploadInfo(StoreEntity store, EmployeeEntity employee,
			int uploadType, boolean sendFlag, String aliSmsId,String aliSmsSignName,List<UploadWayBillEntity> waybills) {
		Date begin = new Date();
		List<String> idList = new ArrayList<String>();
		for (UploadWayBillEntity uploadInfo : waybills) {
			// 手机号、区域号不能为空
			if (StringUtils.isBlank(uploadInfo.getReceiverPhone()) || StringUtils.isBlank(uploadInfo.getAreaNum())) {
				continue;
			}
			uploadInfo.setStore(store);
			uploadInfo.setEmployee(employee);
			uploadInfo.setUploadType(uploadType);
			// 入库或者直接出库配送标记位
			uploadInfo.setSendFlag(sendFlag);
			// 滞留件标记位
			if (uploadType == UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZHILIU) {
				uploadInfo.setDelayFlag(true);
			} else {
				uploadInfo.setDelayFlag(false);
			}
			// 发送短信标记位
			if (uploadType == UploadWayBillEntity.WAYBILL_UPLOADTYPE_PEISONG) {
				uploadInfo.setSmsFlag(false);
			} else {
				uploadInfo.setSmsFlag(true);
				// 格式化取件码
				uploadInfo.setAreaNum(StringUtils.formartAreaNum(uploadInfo.getAreaNum()));
				uploadInfo.setSmsModel(aliSmsId);
				uploadInfo.setSmsSignName(aliSmsSignName);
			}

			String id = IdGen.uuid();
			uploadInfo.setId(id);
			uploadInfo.setCreateDate(begin);
			uploadWayBillDAO.save(uploadInfo);
			idList.add(id);
		}
		
		String result = "";
		EnumRespCode respCode = EnumRespCode.SUCCESS;
		if (idList.size() > 0) {
			
			// 推送idList到redis队列等待线程池处理
			RedisUtil.rpush(RedisCache.REDIS_QUEUE_UPLOAD_WAYBILL,idList.toArray(new String[0]));

			if (uploadType == UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZITI) {
				result = "上传 " + idList.size() + " 件自提件";
			} else if (uploadType == UploadWayBillEntity.WAYBILL_UPLOADTYPE_PEISONG) {
				result = "上传 " + idList.size() + " 件配送件";
			} else if (uploadType == UploadWayBillEntity.WAYBILL_UPLOADTYPE_ZHILIU) {
				result = "上传 " + idList.size() + " 件滞留件";
			} else {
				result = EnumRespCode.INFO_ERROR.getValue();
				respCode = EnumRespCode.INFO_ERROR;
			}
		} else {
			result = EnumRespCode.UPLOAD_WAYBILL_IS_NULL.getValue();
			respCode = EnumRespCode.UPLOAD_WAYBILL_IS_NULL;
		}
		logger.info("{} {} {} (耗时:{})",store.getStoreName(),employee.getName(),result,System.currentTimeMillis()-begin.getTime());
		return respCode;
	}

}