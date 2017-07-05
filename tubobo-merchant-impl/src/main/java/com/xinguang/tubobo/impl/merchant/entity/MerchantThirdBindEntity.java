package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuqinghua on 2017/6/29.
 */
@Entity
@Table(name = "t_third_bind")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantThirdBindEntity  implements Serializable{

    private String userId;
    private boolean mtBound;
    private String mtAuthToken;
    private boolean eleBound;
    private boolean yzBound;


    public MerchantThirdBindEntity(){
        this.delFlag = BaseMerchantEntity.DEL_FLAG_NORMAL;
        this.createDate = new Date();
    }
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;
    protected String delFlag;
    protected Date createDate;
    protected Date updateDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMtBound() {
        return mtBound;
    }

    public void setMtBound(boolean mtBound) {
        this.mtBound = mtBound;
    }

    public boolean isEleBound() {
        return eleBound;
    }

    public void setEleBound(boolean eleBound) {
        this.eleBound = eleBound;
    }

    public String getMtAuthToken() {
        return mtAuthToken;
    }

    public void setMtAuthToken(String mtAuthToken) {
        this.mtAuthToken = mtAuthToken;
    }

    public boolean isYzBound() {
        return yzBound;
    }

    public void setYzBound(boolean yzBound) {
        this.yzBound = yzBound;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
