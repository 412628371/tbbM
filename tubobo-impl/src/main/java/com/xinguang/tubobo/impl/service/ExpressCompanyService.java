package com.xinguang.tubobo.impl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.ExpressCompanyEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.dao.ExpressCompanyDAO;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class ExpressCompanyService {
    @Autowired
    private ExpressCompanyDAO expressCompanyDAO;
    @Autowired
    private StoreService storeService;

    public ExpressCompanyEntity get(String companyId) {
        return expressCompanyDAO.get(companyId);
    }
    
    public List<ExpressCompanyEntity> findAll() {
        return expressCompanyDAO.find().asList();
    }

    public Page<ExpressCompanyEntity> findIsOpened2Store(Page<ExpressCompanyEntity> page,
            ExpressCompanyEntity expressCompanyEntity, String storeId) {

        Page<ExpressCompanyEntity> resultPage = find(page, expressCompanyEntity);
        if(resultPage.getCount() > 0 && StringUtils.isNotBlank(storeId)) {
            StoreEntity store = storeService.get(storeId);
            if(null != store) {
                List<ExpressCompanyEntity> list = store.getExpressCompanyList();
                    for (ExpressCompanyEntity re : resultPage.getList()) {
                        if(null != list && list.contains(re)) {
                            re.setOpened("1");
                        } else {
                            re.setOpened("0");
                        }
                    }
            }
        }
        return resultPage;
    }
    public Page<ExpressCompanyEntity> find(Page<ExpressCompanyEntity> page,
            ExpressCompanyEntity expressCompanyEntity) {
        return expressCompanyDAO.find(page, expressCompanyEntity);
    }

    public int saveOrUpdate(ExpressCompanyEntity expressCompanyEntity) {
        return expressCompanyDAO.saveOrUpdate(expressCompanyEntity);
        
    }

    public boolean exist(String companyId) {
        return expressCompanyDAO.exists("companyId", companyId);
    }

}