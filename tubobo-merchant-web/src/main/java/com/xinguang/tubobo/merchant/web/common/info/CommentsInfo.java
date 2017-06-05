package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * 评论信息.
 */
public class CommentsInfo implements Serializable{
    private String commentContent;
    private Integer commentDeliveryScore;
    private Integer commentServiceScore;
    private Boolean ratedFlag;
    public CommentsInfo(Boolean ratedFlag){
        this.ratedFlag = ratedFlag;
    }
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getCommentDeliveryScore() {
        return commentDeliveryScore;
    }

    public void setCommentDeliveryScore(Integer commentDeliveryScore) {
        this.commentDeliveryScore = commentDeliveryScore;
    }

    public Integer getCommentServiceScore() {
        return commentServiceScore;
    }

    public void setCommentServiceScore(Integer commentServiceScore) {
        this.commentServiceScore = commentServiceScore;
    }

    public Boolean getRatedFlag() {
        return ratedFlag;
    }

    public void setRatedFlag(Boolean ratedFlag) {
        this.ratedFlag = ratedFlag;
    }
}
