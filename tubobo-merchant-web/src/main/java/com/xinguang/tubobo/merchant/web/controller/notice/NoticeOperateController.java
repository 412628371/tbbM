package com.xinguang.tubobo.merchant.web.controller.notice;

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.notice.ReqNoticeOperate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/7/18.
 */
@Controller
@RequestMapping("/notice/operate")
public class NoticeOperateController extends MerchantBaseController<ReqNoticeOperate,Object> {
    @Override
    protected Object doService(String userId, ReqNoticeOperate req) throws MerchantClientException {

        return null;
    }
}
