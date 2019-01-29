package com.teekee.commoncomponets.base;

import java.io.Serializable;
import java.util.List;

/**
 * @description: treegrid树形表格基础对象，后续的该类型的对象均继承该对象
 * @author: zhangshuai
 * @create: 2018-11-07 16:36
 */
public class BaseTreeGrid implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -9189631784252440402L;

    public Integer id;//节点id

    public Integer parentId;//节点父id

    public String iconCls = "folder";//节点样式，默认即可

    public Boolean leaf = true;//是否为叶子节点，true表示是叶子节点，false表示不是叶子节点

    public Boolean expand = true; //是否展开，默认true,展开

    public List<BaseTreeGrid> children;//孩子节点

    public BaseTreeGrid() {

    }

    public BaseTreeGrid(Integer id, Integer parentId) {
        this.id=id;
        this.parentId=parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }

    public List<BaseTreeGrid> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTreeGrid> children) {
        this.children = children;
    }
}
