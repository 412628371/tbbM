package com.xinguang.tubobo.merchant.web.controller.notice;

import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.impl.merchant.service.NoticeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.notice.ReqNoticeList;
import com.xinguang.tubobo.merchant.web.response.notice.RespNoticeItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqinghua on 2017/7/18.
 */
@Controller
@RequestMapping("/notice/list")
public class NoticeListController extends MerchantBaseController<ReqNoticeList,PageDTO<RespNoticeItem>> {
    @Autowired private NoticeService noticeService;
    @Override
    protected PageDTO<RespNoticeItem> doService(String userId, ReqNoticeList req) throws MerchantClientException {
        Page<PushNoticeEntity> page = noticeService.findNoticePage(userId,req.getPageNo(),req.getPageSize());
        List<RespNoticeItem> list = new ArrayList<>();
        if (page.hasContent()) {
            for (PushNoticeEntity entity:page){
                RespNoticeItem  item = new RespNoticeItem();
                BeanUtils.copyProperties(entity,item);
                item.setCreateTime(entity.getCreateDate());
                list.add(item);
            }
        }
        return new PageDTO<>(req.getPageNo(),req.getPageSize(),page.getTotalElements(),list);
    }
}
