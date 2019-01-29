package com.teekee.blueoceanservice.service.cfg;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.teekee.blueoceanservice.entity.cfg.CfgFaultTypeConfig;
import com.teekee.blueoceanservice.entity.cfg.CfgFaultTypeModel;
import com.teekee.blueoceanservice.mapper.cfg.CfgFaultTypeConfigMapper;
import com.teekee.blueoceanservice.mapper.cfg.CfgFaultTypeModelMapper;
import com.teekee.blueoceanservice.utils.CurrentUtil;
import com.teekee.commoncomponets.utils.Result;
import com.teekee.commoncomponets.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @Title: CfgFaultTypeModelService
 * @ProjectName WaterDrop
 * @Description: 客服故障类型
 * @date 2018/12/18下午3:02
 */
@Service
public class CfgFaultTypeModelService {

    @Resource
    CfgFaultTypeModelMapper mapper;
    @Resource
    CfgFaultTypeConfigMapper configMapper;

    public Result handle(String json) {

        List<CfgFaultTypeConfig> config = JSON.parseArray(json, CfgFaultTypeConfig.class);
        CfgFaultTypeModel model = new CfgFaultTypeModel();
        model.setFaultDefineType(config.get(0).getFaultTypeName());
        model.setCreateTime(new Date());
        model.setCreatePerson(CurrentUtil.getCurrent().getId());
        model.setCompanyId(0);
        model.setModelUserType("DEMAND");
        Integer verson = mapper.selectVersion(model);
        if (verson == null) {
            verson = 0;
        }
        model.setVersion(verson + 1);
        mapper.insertSelective(model);

        int i = 1;
        for (CfgFaultTypeConfig cc : config) {
            cc.setId(0);
            cc.setOrderId(i++);
            cc.setModelId(model.getId());
            configMapper.insertSelective(cc);
        }
        return ResultUtil.success();
    }

    public Result list(CfgFaultTypeModel model) {
        model.setCompanyId(0);
        PageHelper.startPage(model.getPageNum(), model.getPageSize());
        Page<CfgFaultTypeModel> persons = mapper.selectByType(model);
        PageInfo<CfgFaultTypeModel> pageInfo = new PageInfo<>(persons);
        return ResultUtil.success(pageInfo);

    }

    public Result listConfig(Integer modelId) {
        if (modelId == null || modelId == 0) {
            return ResultUtil.validateError("参数错误");
        }
        return ResultUtil.success(configMapper.selectByModelId(modelId));
    }

    public Result listConfigByCustomer(String faultDefineType) {
        CfgFaultTypeModel model = new CfgFaultTypeModel();
        model.setFaultDefineType(faultDefineType);
        model.setCompanyId(0);
        return ResultUtil.success(configMapper.selectMaxVersionConfig(model));
    }

    public Result listHistory(CfgFaultTypeModel model) {

        model.setCompanyId(0);
        PageHelper.startPage(model.getPageNum(), model.getPageSize());
        Page<CfgFaultTypeModel> persons = mapper.selectListHistory(model);
        PageInfo<CfgFaultTypeModel> pageInfo = new PageInfo<>(persons);
        // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
        return ResultUtil.success(pageInfo);
    }
}