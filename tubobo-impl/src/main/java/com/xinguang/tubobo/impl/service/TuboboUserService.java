package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.TuboboUserEntity;
import com.xinguang.tubobo.impl.dao.TuboboUserDAO;
import com.xinguang.tubobo.impl.wechat.entity.WxAuthUserInfo;

@Service
public class TuboboUserService {
	
    @Autowired
    private TuboboUserDAO tuboboUserDAO;

    public TuboboUserEntity save(WxAuthUserInfo userInfo){
    	TuboboUserEntity entity = new TuboboUserEntity();
    	entity.setId(IdGen.uuid());
    	entity.setCreateDate(new Date());
    	entity.setOpenId(userInfo.getOpenid());
    	entity.setNickName(userInfo.getNickname());
    	entity.setSex(userInfo.getSex());
    	entity.setProvince(userInfo.getProvince());
    	entity.setCity(userInfo.getCity());
    	entity.setCountry(userInfo.getCountry());
    	entity.setHeadImgUrl(userInfo.getHeadimgurl());
    	entity.setUnionid(userInfo.getUnionid());
    	tuboboUserDAO.save(entity);
    	return entity;
    }

    public TuboboUserEntity findByUserId(String userId){
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("userId").equal(userId);
    	return tuboboUserDAO.findOne(q);
    }

    public TuboboUserEntity findByOpenId(String openId){
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("openId").equal(openId);
    	return tuboboUserDAO.findOne(q);
    }

    public TuboboUserEntity findByToken(String token){
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("token").equal(token);
    	return tuboboUserDAO.findOne(q);
    }

    public TuboboUserEntity findByPhone(String phone){
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("phone").equal(phone);
    	q.order("-createDate");
    	List<TuboboUserEntity> list = q.asList();
    	if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
    }


    /**
     * 根据条件查询openId
     * userId不为空时，根据userId查询
     * userId为空时，根据Phone查询
     */
    public String findOpenIdByUserIdThenPhone(String userId, String phone){
        if (StringUtils.isNotBlank(userId)){
            TuboboUserEntity entity = findByUserId(userId);
            if (entity != null){
                return entity.getOpenId();
            }
        }else if (StringUtils.isNotBlank(phone)){
            TuboboUserEntity entity = findByPhone(phone);
            if (entity != null){
                return entity.getOpenId();
            }
        }
        return "";
    }

    public Page<TuboboUserEntity> find(Page<TuboboUserEntity> page, TuboboUserEntity entity) {
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
        if(StringUtils.isNotBlank(entity.getPhone())) {
        	q.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }

    public int bindUserInfo(String openId, String userId, String account, String phone, String token){
    	UpdateOperations<TuboboUserEntity> ops = tuboboUserDAO.createUpdateOperations();
		ops.set("userId", userId);
		ops.set("account", account);
		ops.set("phone", phone);
		ops.set("token", token);
		ops.set("lastLoginDate", new Date());
		ops.set("updateDate", new Date());
		Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
		q.field("openId").equal(openId);
    	return tuboboUserDAO.update(q, ops).getUpdatedCount();
    }

    public int refreshToken(String userId, String token){
    	UpdateOperations<TuboboUserEntity> ops = tuboboUserDAO.createUpdateOperations();
    	ops.set("token", token);
    	ops.set("lastLoginDate", new Date());
    	ops.set("updateDate", new Date());
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("userId").equal(userId);
    	return tuboboUserDAO.update(q, ops).getUpdatedCount();
    }

    public int updateNickName(String userId, String nickName){
    	UpdateOperations<TuboboUserEntity> ops = tuboboUserDAO.createUpdateOperations();
    	ops.set("nickName", nickName);
    	ops.set("updateDate", new Date());
    	Query<TuboboUserEntity> q = tuboboUserDAO.createQuery();
    	q.field("userId").equal(userId);
    	return tuboboUserDAO.update(q, ops).getUpdatedCount();
    }
}
