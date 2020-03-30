package org.yuan.pojo.vo;

import org.yuan.pojo.Items;
import org.yuan.pojo.ItemsImg;
import org.yuan.pojo.ItemsParam;
import org.yuan.pojo.ItemsSpec;

import java.util.Date;
import java.util.List;

public class ItemCommentVo {

    private Integer commentLevel;
    private String content;
    private String sepcName;
    private Date createdTime;
    private String userFace;
    private String nickname;

    public Integer getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(Integer commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSepcName() {
        return sepcName;
    }

    public void setSepcName(String sepcName) {
        this.sepcName = sepcName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ItemCommentVo{" +
                "commentLevel=" + commentLevel +
                ", content='" + content + '\'' +
                ", sepcName='" + sepcName + '\'' +
                ", createdTime=" + createdTime +
                ", userFace='" + userFace + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
