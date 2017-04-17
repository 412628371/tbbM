package com.xinguang.tubobo.impl.dao;

import java.util.Date;
import java.util.regex.Pattern;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.ExpressCompanyEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;

@Repository
public class ExpressCompanyDAO extends BasicDAO<ExpressCompanyEntity, String> {

    @Autowired
    protected ExpressCompanyDAO(Datastore ds) {
        super(ds);
    }

    public Page<ExpressCompanyEntity> find(Page<ExpressCompanyEntity> page,
            ExpressCompanyEntity expressCompanyEntity) {
        Query<ExpressCompanyEntity> q = createQuery();

        if(null != expressCompanyEntity) {
            if(StringUtils.isNotBlank(expressCompanyEntity.getCompanyName())) {
                q.field("companyName").contains(Pattern.quote(expressCompanyEntity.getCompanyName()));
            }
            if(StringUtils.isNotBlank(expressCompanyEntity.getContractPhone())) {
                q.field("contractPhone").contains(Pattern.quote(expressCompanyEntity.getContractPhone()));
            }
        }

        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }

    public int saveOrUpdate(ExpressCompanyEntity entity) {
        if(null == entity) {
            return 0;
        }
        if(StringUtils.isBlank(entity.getCompanyId())) {
            entity.setCompanyId(IdGen.uuid());
            entity.setCreateDate(new Date());
            entity.setUpdateDate(entity.getCreateDate());
            entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
            save(entity);
            return 1;
        } else {
            UpdateOperations<ExpressCompanyEntity> ops = createUpdateOperations();
            ops.set("companyName", entity.getCompanyName());
            ops.set("contractPhone", entity.getContractPhone());
            ops.set("regularExpression", entity.getRegularExpression());
            ops.set("updateDate", new Date());
            UpdateResults urs = updateFirst(createQuery().field("companyId").equal(entity.getCompanyId()), ops);
            return urs.getUpdatedCount();
        }
    }

}
