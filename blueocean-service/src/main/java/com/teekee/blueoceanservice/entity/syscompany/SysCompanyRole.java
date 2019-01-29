package com.teekee.blueoceanservice.entity.syscompany;

import com.teekee.commoncomponets.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "公司角色表")
public class SysCompanyRole extends BaseEntity {

    @ApiModelProperty(value = "id", example = "1", required = true)
    private Integer id;

    @ApiModelProperty(value = "资源的中文解释", example = "1", required = true)
    private Integer companyId;

    @ApiModelProperty(value = "角色名", example = "1", required = true)
    private String roleName;

    @ApiModelProperty(value = "创建时间", example = "1", required = true)
    private Date createTime;

    @ApiModelProperty(value = "创建人ID", example = "1", required = true)
    private Integer createPerson;

    @ApiModelProperty(value = "更新时间", example = "1", required = true)
    private Date updateTime;

    @ApiModelProperty(value = "更新人id", example = "1", required = true)
    private Integer updatePerson;

    @ApiModelProperty(value = "是否删除", example = "1", required = true)
    private Boolean flagDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(Integer updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Boolean getFlagDeleted() {
        return flagDeleted;
    }

    public void setFlagDeleted(Boolean flagDeleted) {
        this.flagDeleted = flagDeleted;
    }

    @Override
    public String toString() {
        return "SysCompanyRole{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", roleName='" + roleName + '\'' +
                ", createTime=" + createTime +
                ", createPerson=" + createPerson +
                ", updateTime=" + updateTime +
                ", updatePerson=" + updatePerson +
                ", flagDeleted=" + flagDeleted +
                '}';
    }
}