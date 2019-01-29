package com.teekee.blueoceanservice.entity.cfg;

import java.util.Date;

/**
 * @author zhanghao
 * @Title: CfgDispatchWarningStatus
 * @ProjectName BlueOcean
 * @Description: 预警等级
 * @date 2019/1/3下午2:14
 */
public class CfgDispatchWarningStatus {

    /**
     * id
     */
    private Integer id;
    /**
     *预警名称
     */
    private String name;
    /**
     *开始范围
     */
    private Integer startRange;
    /**
     *结束范围
     */
    private Integer endRange;
    /**
     *预警颜色
     */
    private String colour;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *创建人
     */
    private Integer createPerson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStartRange() {
        return startRange;
    }

    public void setStartRange(Integer startRange) {
        this.startRange = startRange;
    }

    public Integer getEndRange() {
        return endRange;
    }

    public void setEndRange(Integer endRange) {
        this.endRange = endRange;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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

    @Override
    public String toString() {
        return "CfgDispatchWarningStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startRange=" + startRange +
                ", endRange=" + endRange +
                ", colour='" + colour + '\'' +
                ", createTime=" + createTime +
                ", createPerson=" + createPerson +
                '}';
    }
}