package com.teekee.blueoceanservice.mapper.cfg;

import com.teekee.blueoceanservice.entity.cfg.CfgDispatchDetailConfig;
import com.teekee.blueoceanservice.entity.cfg.CfgDispatchDetailModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CfgDispatchDetailConfigMapper {

    int insertSelective(CfgDispatchDetailConfig record);

    CfgDispatchDetailConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CfgDispatchDetailConfig record);
    /**
     * 查询父级
     * @param model   派工要求类型

     * @return
     * @Author Niting
     * @date 2018年5月15日
     *
     */
    List<CfgDispatchDetailConfig> basicList(CfgDispatchDetailModel model);
    /**
     * 查询子级
     * @param id
     * @return
     * @Author Niting
     * @date 2018年5月15日
     *
     */
    List<CfgDispatchDetailConfig> childList(Integer id);
    /**
     * 通过modelId 查询派工要求
     * @param modelId 模板id
     * @return
     * @Author Niting
     * @date 2018年5月15日
     *
     */
    List<CfgDispatchDetailConfig> selectDispatchConfigByModelId(Integer modelId);
    /**
     * 查询详细派工要求
     *
     * @return
     * @Author Niting
     * @date 2018年5月14日
     * @param id
     */
    CfgDispatchDetailConfig selectListById(int id);
}