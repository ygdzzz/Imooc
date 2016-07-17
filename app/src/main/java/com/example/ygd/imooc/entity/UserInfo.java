package com.example.ygd.imooc.entity;

/**
 * Created by ygd on 2016/7/3.
 */
public class UserInfo {
    /**
     * level : Z
     * nickName : 张三
     * photoUri : http://192.168.8.118:8080/VedioManager//user/userhead_img/1449641420384.png
     * rgdtDate : 20151117
     * sexId : 男
     * uname : 123
     * upwd : 111
     */

    private String level;
    private String nickName;
    private String photoUri;
    private String rgdtDate;
    private String sexId;
    private String uname;
    private String upwd;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getRgdtDate() {
        return rgdtDate;
    }

    public void setRgdtDate(String rgdtDate) {
        this.rgdtDate = rgdtDate;
    }

    public String getSexId() {
        return sexId;
    }

    public void setSexId(String sexId) {
        this.sexId = sexId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }
}
