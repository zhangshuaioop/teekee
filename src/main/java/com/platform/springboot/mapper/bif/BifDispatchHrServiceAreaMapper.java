package com.platform.springboot.mapper.bif;

import com.github.pagehelper.Page;
import com.platform.springboot.base.BaseMapper;
import com.platform.springboot.entity.bif.BifDispatchHrServiceArea;
import com.platform.springboot.entity.bif.BifDispatchHrServiceAreaShow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhanghao
 * @Title: BifDispatchHrServiceAreaMapper
 * @ProjectName BlueOcean
 * @Description: TODO
 * @date 2018/12/28下午2:15
 */
@Mapper
public interface BifDispatchHrServiceAreaMapper extends BaseMapper<BifDispatchHrServiceArea> {

    int deleteByPrimaryKey(Integer id);

    int insert(BifDispatchHrServiceArea record);

    int insertSelective(BifDispatchHrServiceArea record);

    BifDispatchHrServiceArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BifDispatchHrServiceArea record);

    int updateByPrimaryKey(BifDispatchHrServiceArea record);

    List<BifDispatchHrServiceAreaShow> selectServiceShowById(Integer id);

    void updateFaultByHrId(Integer hrId);

    void deleteAreaById(Integer id);

    Page<BifDispatchHrServiceArea> selectByHrId(Integer hrId);

    void updateByPrimaryKeySelectiveById(BifDispatchHrServiceArea serviceArea);

    List<BifDispatchHrServiceArea> selectFault(BifDispatchHrServiceArea serviceArea);

    List<BifDispatchHrServiceArea> selectAreaList(Integer id);
}
