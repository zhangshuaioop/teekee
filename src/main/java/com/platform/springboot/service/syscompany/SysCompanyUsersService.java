package com.platform.springboot.service.syscompany;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platform.springboot.base.BaseService;
import com.platform.springboot.entity.syscompany.SysCompanyUsers;
import com.platform.springboot.mapper.syscompany.SysCompanyPermissionMapper;
import com.platform.springboot.mapper.syscompany.SysCompanyUsersMapper;
import com.platform.springboot.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @description: 公司用户账号
 * @author: zhangshuai
 * @create: 2018-12-12 09:20
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SysCompanyUsersService extends BaseService<SysCompanyUsersMapper,SysCompanyUsers> {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SysCompanyUsersMapper sysCompanyUsersMapper;

    @Autowired
    private SysCompanyPermissionMapper sysCompanyPermissionMapper;




    /**
     * 平台查看公司用户-分页列表
     *
     * @param sysCompanyUsers
     * @return
     */
    public Result findPage(SysCompanyUsers sysCompanyUsers) {

        PageHelper.startPage(sysCompanyUsers.getPageNum(), sysCompanyUsers.getPageSize());
        Page<SysCompanyUsers> persons = sysCompanyUsersMapper.findPageConsole(sysCompanyUsers);
        // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
        PageInfo<SysCompanyUsers> pageInfo = new PageInfo<>(persons);
        if(pageInfo.getList()==null||pageInfo.getList().size()==0){
            pageInfo.setPageNum(1);
            pageInfo.setPageSize(10);
        }
        return ResultUtil.success(pageInfo);
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public Result resetPassword(Integer userId) {

        if(userId != null){
            SysCompanyUsers sysCompanyUsers1 = new SysCompanyUsers();
            sysCompanyUsers1.setId(userId);
            sysCompanyUsers1.setPassword(MD5Utils.MD5Encode("123456","utf-8"));
            sysCompanyUsersMapper.updateByPrimaryKeySelective(sysCompanyUsers1);
        }else {
            return ResultUtil.validateError("");
        }
        return ResultUtil.success();
    }


}
