package com.xinguang.tubobo.merchant.web.controller.notice;

import com.xinguang.tubobo.api.AdminNotifyService;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.enums.EnumNotifyType;
import com.xinguang.tubobo.impl.merchant.service.NoticeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.notice.RespNoticeUnprocessedCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/7/24.
 */
@Controller
@RequestMapping("/notice/unProcessed/count")
public class NoticeUnprocessedController extends MerchantBaseController<Object,RespNoticeUnprocessedCount> {
    @Autowired private NoticeService noticeService;
    @Autowired private AdminNotifyService adminNotifyService;
    @Override
    protected RespNoticeUnprocessedCount doService(String userId, Object req) throws MerchantClientException {
        RespNoticeUnprocessedCount respNoticeUnprocessedCount = new RespNoticeUnprocessedCount();
        Long businessCount = noticeService.getUnProcessedCount(userId);
//        Integer systemCount = adminNotifyService.noReadNotifyCounts(userId, EnumNotifyType.NOTIFY_MERCHANT);
        if (businessCount == null){
            businessCount = 0L;
        }
//        if (systemCount == null){
//            systemCount = 0;
//        }
        respNoticeUnprocessedCount.setBusinessNoticeCount(businessCount);
        respNoticeUnprocessedCount.setSystemNoticeCount(0l);
        return respNoticeUnprocessedCount;
    }
}
