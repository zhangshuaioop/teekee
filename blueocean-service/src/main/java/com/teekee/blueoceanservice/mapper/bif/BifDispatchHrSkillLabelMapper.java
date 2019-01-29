package com.teekee.blueoceanservice.mapper.bif;


import com.teekee.commoncomponets.base.BaseMapper;
import com.teekee.blueoceanservice.entity.bif.BifDispatchHrSkillLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BifDispatchHrSkillLabelMapper extends BaseMapper<BifDispatchHrSkillLabel> {
    int deleteByPrimaryKey(Integer id);

    int insert(BifDispatchHrSkillLabel record);

    int insertSelective(BifDispatchHrSkillLabel record);

    BifDispatchHrSkillLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BifDispatchHrSkillLabel record);

    int updateByPrimaryKey(BifDispatchHrSkillLabel record);

    /**
     * 删除所有
     * @return
     */
    int deleteByPrimaryKeys();

    /**
     * 批量添加
     * @return
     */
    int inserts(List<BifDispatchHrSkillLabel> BbifDispatchHrSkillLabel);

    /**
     * 查询所有
     * @return
     */
    List<BifDispatchHrSkillLabel> findAll();

    /**
     * 批量更新
     * @param BbifDispatchHrSkillLabel
     * @return
     */
    int updates(List<BifDispatchHrSkillLabel> BbifDispatchHrSkillLabel);


    /**
     * 批量删除
     * @param BbifDispatchHrSkillLabel
     * @return
     */
    int deletes(List<BifDispatchHrSkillLabel> BbifDispatchHrSkillLabel);
}