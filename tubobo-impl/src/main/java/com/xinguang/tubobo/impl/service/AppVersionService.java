package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.AppVersionEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.impl.dao.AppVersionDAO;

@Service
public class AppVersionService {
    @Autowired
    private AppVersionDAO appVersionDAO;

    public void saveOrUpdate(AppVersionEntity entity) {
    	if (StringUtils.isNotBlank(entity.getId())) {
    		appVersionDAO.updateVersion(entity);
		}else {
			appVersionDAO.disableOldVersion(entity.getAppName(),entity.getAppType());
			entity.setId(IdGen.uuid());
			entity.setCreateDate(new Date());
			entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
			appVersionDAO.save(entity);
		}
    }

    public AppVersionEntity findById(String id){
    	return appVersionDAO.get(id);
    }

    public AppVersionEntity findByNameAndType(String appName,String appType){
    	return appVersionDAO.findByNameAndType(appName, appType);
    }
    
    public List<AppVersionEntity> findAllVersion(){
    	return appVersionDAO.findAllVersion();
    }
    
    public Page<AppVersionEntity> find(Page<AppVersionEntity> page,AppVersionEntity entity) {
        return appVersionDAO.find(page, entity);
    }
}
