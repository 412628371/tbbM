package com.xinguang.tubobo.merchant.web.request.shop.v2;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2017/5/25.
 */
public class ReqPersonIdentify {

    @NotBlank(message = "身份证号码不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_ID_CARD ,message = "身份证号码有误")
    private String idCardNo;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 20,message = "长度为1-20")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_PHONE,message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "请输入支付密码")
    private String payPassword;

    @NotBlank(message = "身份证正面图片不能为空")
    @Size(max = 255,message = "idCardBackImageUrl长度过大")
    private String idCardBackImageUrl;
    @NotBlank(message = "身份证反面图片不能为空")
    @Size(max = 255,message = "idCardFrontImageUrl长度过大")
    private String idCardFrontImageUrl;

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getIdCardBackImageUrl() {
        return idCardBackImageUrl;
    }

    public void setIdCardBackImageUrl(String idCardBackImageUrl) {
        this.idCardBackImageUrl = idCardBackImageUrl;
    }

    public String getIdCardFrontImageUrl() {
        return idCardFrontImageUrl;
    }

    public void setIdCardFrontImageUrl(String idCardFrontImageUrl) {
        this.idCardFrontImageUrl = idCardFrontImageUrl;
    }
}
