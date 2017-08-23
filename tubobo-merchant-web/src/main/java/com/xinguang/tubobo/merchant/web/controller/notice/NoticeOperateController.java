package com.xinguang.tubobo.merchant.web.controller.notice;

import com.xinguang.tubobo.impl.merchant.service.NoticeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.notice.ReqNoticeOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/7/18.
 */
@Controller
@RequestMapping("/notice/operate")
public class NoticeOperateController extends MerchantBaseController<ReqNoticeOperate,Object> {
    @Autowired private NoticeService noticeService;
    @Override
    protected Object doService(String userId, ReqNoticeOperate req) throws MerchantClientException {

        String operate = req.getOperate();
        if ("DEL".equals(operate)){
            noticeService.deleteMsgs(userId,req.getIds());
        }else if ("PROCESS".equals(operate)){
            logger.info("通知标记为已读接口，req:{}",req.toString());
            noticeService.processMsgsRead(userId,req.getIds());
        }
        return "";
    }
}
