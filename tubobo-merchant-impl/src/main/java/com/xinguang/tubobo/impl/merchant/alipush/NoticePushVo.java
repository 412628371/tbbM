package com.xinguang.tubobo.impl.merchant.alipush;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NoticePushVo implements Serializable{
    private String type;
    private String noticeType;
    private String userId;
    private NoticeParamVo params;
    private String _NOTIFICATION_BAR_STYLE="1"; //0开启 1关闭
    private String androidMusicName;            //指定音乐样式

    public String get_NOTIFICATION_BAR_STYLE() {
        return _NOTIFICATION_BAR_STYLE;
    }

    public void set_NOTIFICATION_BAR_STYLE(String _NOTIFICATION_BAR_STYLE) {
        this._NOTIFICATION_BAR_STYLE = _NOTIFICATION_BAR_STYLE;
    }

    public String getAndroidMusicName() {
        return androidMusicName;
    }

    public void setAndroidMusicName(String androidMusicName) {
        this.androidMusicName = androidMusicName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NoticeParamVo getParams() {
        return params;
    }

    public void setParams(NoticeParamVo params) {
        this.params = params;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}
