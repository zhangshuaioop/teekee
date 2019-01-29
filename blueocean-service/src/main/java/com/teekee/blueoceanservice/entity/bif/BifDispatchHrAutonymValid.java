package com.teekee.blueoceanservice.entity.bif;

import java.util.Date;

public class BifDispatchHrAutonymValid {
    private Integer id;

    private Integer userId;

    private String userName;

    private String idType;

    private String idCode;

    private String positiveImageUrl;

    private String contraryImageUrl;

    private String handheldImageUrl;

    private Boolean flagLast;

    private Boolean flagValidStatus;

    private Date createTime;

    private Date dealTime;

    private Integer validDenyReasonId;

    private String validDenyMemo;

    private Integer validPersonId;

    public Integer getValidPersonId() {
        return validPersonId;
    }

    public void setValidPersonId(Integer validPersonId) {
        this.validPersonId = validPersonId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType == null ? null : idType.trim();
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode == null ? null : idCode.trim();
    }

    public String getPositiveImageUrl() {
        return positiveImageUrl;
    }

    public void setPositiveImageUrl(String positiveImageUrl) {
        this.positiveImageUrl = positiveImageUrl == null ? null : positiveImageUrl.trim();
    }

    public String getContraryImageUrl() {
        return contraryImageUrl;
    }

    public void setContraryImageUrl(String contraryImageUrl) {
        this.contraryImageUrl = contraryImageUrl == null ? null : contraryImageUrl.trim();
    }

    public String getHandheldImageUrl() {
        return handheldImageUrl;
    }

    public void setHandheldImageUrl(String handheldImageUrl) {
        this.handheldImageUrl = handheldImageUrl == null ? null : handheldImageUrl.trim();
    }

    public Boolean getFlagLast() {
        return flagLast;
    }

    public void setFlagLast(Boolean flagLast) {
        this.flagLast = flagLast;
    }

    public Boolean getFlagValidStatus() {
        return flagValidStatus;
    }

    public void setFlagValidStatus(Boolean flagValidStatus) {
        this.flagValidStatus = flagValidStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Integer getValidDenyReasonId() {
        return validDenyReasonId;
    }

    public void setValidDenyReasonId(Integer validDenyReasonId) {
        this.validDenyReasonId = validDenyReasonId;
    }

    public String getValidDenyMemo() {
        return validDenyMemo;
    }

    public void setValidDenyMemo(String validDenyMemo) {
        this.validDenyMemo = validDenyMemo == null ? null : validDenyMemo.trim();
    }

    @Override
    public String toString() {
        return "BifDispatchHrAutonymValid{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", idType='" + idType + '\'' +
                ", idCode='" + idCode + '\'' +
                ", positiveImageUrl='" + positiveImageUrl + '\'' +
                ", contraryImageUrl='" + contraryImageUrl + '\'' +
                ", handheldImageUrl='" + handheldImageUrl + '\'' +
                ", flagLast=" + flagLast +
                ", flagValidStatus=" + flagValidStatus +
                ", createTime=" + createTime +
                ", dealTime=" + dealTime +
                ", validDenyReasonId=" + validDenyReasonId +
                ", validDenyMemo='" + validDenyMemo + '\'' +
                ", validPersonId=" + validPersonId +
                '}';
    }
}