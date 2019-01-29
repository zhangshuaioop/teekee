package com.teekee.blueoceanservice.mapper.bif;

import com.github.pagehelper.Page;
import com.teekee.commoncomponets.base.BaseMapper;
import com.teekee.blueoceanservice.entity.bif.BifDispatchHrAccountInfo;
import com.teekee.blueoceanservice.entity.bif.BifDispatchHrAccountInfoShow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BifDispatchHrAccountInfoMapper extends BaseMapper<BifDispatchHrAccountInfo> {

    int insertSelective(BifDispatchHrAccountInfo record);

    BifDispatchHrAccountInfo selectByPrimaryKey(Integer id);

    BifDispatchHrAccountInfo selectByHrId(Integer id);

    Page<BifDispatchHrAccountInfo> selectByHrIdOld(Integer id);

    int updateByPrimaryKeySelective(BifDispatchHrAccountInfo record);


    BifDispatchHrAccountInfo selectByHrIdFlagDefault(int hrId);

    BifDispatchHrAccountInfo selectByHrAccountShow(Integer accountid);

    List<BifDispatchHrAccountInfo> selectByHrIdList(Integer hrId);

    int updateFlagDefaultByHrId(BifDispatchHrAccountInfo accountInfo);

    List<BifDispatchHrAccountInfoShow> selectServiceShowByHrId(Integer id);

    Integer accountDelete(Integer id);
}
