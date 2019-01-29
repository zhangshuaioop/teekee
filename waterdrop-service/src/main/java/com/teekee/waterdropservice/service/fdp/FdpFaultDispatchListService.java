package com.teekee.waterdropservice.service.fdp;


import com.teekee.commoncomponets.utils.PageInfo;
import com.teekee.commoncomponets.utils.Result;
import com.teekee.commoncomponets.utils.ResultUtil;
import com.teekee.waterdropservice.entity.fdp.*;
import com.teekee.waterdropservice.entity.sys.SysCompanyUsers;
import com.teekee.waterdropservice.mapper.fdp.FdpFaultDispatchOrderMapper;
import com.teekee.waterdropservice.mapper.fdp.FdpFaultOrderProcessMapper;
import com.teekee.waterdropservice.mapper.sys.SysCompanyUsersMapper;
import com.teekee.waterdropservice.service.file.FileServiceImpl;
import com.teekee.waterdropservice.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Chenliwen
 * @Date 2018/12/21 10:45
 **/
@Service
public class FdpFaultDispatchListService {

    @Resource
    private FdpFaultOrderProcessMapper processMapper;
    @Resource
    protected FileServiceImpl fileService;
    @Resource
    private SysCompanyUsersMapper sysCompanyUsersMapper;
    @Resource
    private FdpFaultDispatchOrderMapper orderMapper;
    @Resource
    private FdpFaultDispatchOrderMapper fdpFaultDispatchOrderMapper;
    @Value("${filepath}")
    private String filepath;


    public Result getProcessListByObject(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        param.setUserId(user.getId());
        List<GetFdpProcessDetailedList> list = new ArrayList<>();
        List<GetFdpProcessDetailedList> result = processMapper.getProcessListByObject (param);
        for(GetFdpProcessDetailedList item :result){
            //双方都确认完工
            if(item.getFlagFinish()==true){
                item.setUserCompleteTime(item.getLastDealTime());
            }
            if(item.getFlagFinish()==false ){//双方为确认完工
                item.setUserCompleteTime(item.getUserCompleteTime());
            }
            if(item.getLastDealTime()== null && item.getOpearteStatus().equals("COMPLETE")){//客服未派工直接确认完工
                item.setUpdateTime(item.getUpdateTime());
            }
            if(!item.getOpearteStatus().equals("COMPLETE")){
                item.setUpdateTime(null);
            }
        }
        if(param.getPageSize()!=null&&param.getPageNum()!=null) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
            PageInfo<GetFdpProcessDetailedList> pageInfo = new PageInfo<>(result);
            if (result.size() > 0) {
                return ResultUtil.success(pageInfo);
            }
            return ResultUtil.success(list);
        }
        if (result.size() > 0) {
            return ResultUtil.success(result);
        }
        return ResultUtil.success(list);
    }

    public Result processListDownload(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = sdf.format(date);
        //查询客服工单清单
        List<GetFdpProcessDetailedList> detailedLists = getProcessListByObjectByDown(param,user.getId());
        File file = null;
        OutputStream os;
        try {
            file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("临时文件夹创建成功");
            }
            if (detailedLists.size() > 0) {
                //生成派工xls表
                String sCurrPath = file.getPath() + ExcelUtil.FILE_SEPARATOR + "ProcessList_" + currTime + ".xls";
                os = new FileOutputStream(sCurrPath);
                ExcelUtil.exportProcessListExcel(detailedLists, os);
            }

        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("找不到默认保存文件的目录，请联系管理员！");
        }
        File uploadFile = new File(file.getPath()+ ExcelUtil.FILE_SEPARATOR + "ProcessList_" + currTime + ".xls");
        try {
            return fileService.upload(uploadFile.getName() , new FileInputStream(uploadFile), "excel");

        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("文件没有找到，请联系管理员！");
        }
    }

    public List<GetFdpProcessDetailedList> getProcessListByObjectByDown(ParamFdpProcessDetailed param,int userId){
        param.setUserId(userId);
        List<GetFdpProcessDetailedList> result = processMapper.getProcessListByObject (param);
        //List<GetFdpProcessDetailedList> result = new ArrayList<GetFdpProcessDetailedList>();
        for(GetFdpProcessDetailedList item :result){
            //双方都确认完工
            if(item.getFlagFinish()==true){
                item.setUserCompleteTime(item.getLastDealTime());
            }
            if(item.getFlagFinish()==false ){//双方为确认完工
                item.setUserCompleteTime(item.getUserCompleteTime());
            }
            if(item.getLastDealTime()== null && item.getOpearteStatus().equals("COMPLETE")){//客服未派工直接确认完工
                item.setUpdateTime(item.getUpdateTime());
            }
            if(!item.getOpearteStatus().equals("COMPLETE")){
                item.setUpdateTime(null);
            }
        }
        if(result.size() >0){
            return result;
        }
        return new ArrayList<GetFdpProcessDetailedList>();
    }

    public Result getProcesscollectByObject(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();

        GetFdpProcessCollectList result = new GetFdpProcessCollectList();
        param.setUserId(user.getId());
        GetFdpProcessCollectList collect;
        //获取本系统数量
        param.setSource("SYS");
        collect = processMapper.selectProcessCollect(param);
        result.setSysNum(collect.getProcessTotal());
        result.setProcessTotal(collect.getProcessTotal());
        //获取微信数量
        param.setSource("WECHAT");
        collect = processMapper.selectProcessCollect(param);
        result.setWechatNum(collect.getProcessTotal());
        //获取伯俊数量
        param.setSource("POS");
        collect = processMapper.selectProcessCollect(param);
        result.setPosNum(collect.getProcessTotal());
        //获取故障单总数量
        //param.setSource("process");
        //collect = processMapper.selectProcessCollect(param);
        //result.setProcessTotal(collect.getProcessTotal());
        //获取报账单总数量
        param.setSource("report");
        collect = processMapper.selectProcessCollect(param);
        result.setReportTotal(collect.getProcessTotal());
        //获取已完成数量以及完成预计与实际费用
        param.setStatus("COMPLETE");
        param.setSource(null);
        collect = processMapper.selectProcessCollect(param);
        result.setCompleteNum(collect.getProcessTotal());
        result.setActualPriceCompleteNum(collect.getActualPriceCompleteNum());
        result.setActualPriceUnit(collect.getActualPriceUnit());
        result.setEstimatedPriceCompleteNum(collect.getEstimatedPriceCompleteNum());
        result.setEstimatedPriceUnit(collect.getEstimatedPriceUnit());
        //获取未完成数量以及未完成预计与实际费用
        param.setStatus("UNCOMPLETE");
        collect = processMapper.selectProcessCollect(param);
        result.setUncompleteNum(collect.getProcessTotal());
        result.setActualPriceUncompleteNum(collect.getActualPriceCompleteNum());
        result.setActualPriceUnit(collect.getActualPriceUnit());
        result.setEstimatedPriceUncompleteNum(collect.getEstimatedPriceCompleteNum());
        result.setEstimatedPriceUnit(collect.getEstimatedPriceUnit());
        //获取取消数量
        param.setStatus("CANCEL");
        collect = processMapper.selectProcessCollect(param);
        result.setCancelNum(collect.getProcessTotal());
        List<GetFdpProcessCollectList> list = new ArrayList<GetFdpProcessCollectList>();
        //费用验证
        if (result.getActualPriceCompleteNum() == null) {
            result.setActualPriceCompleteNum("0");
            result.setActualPriceUnit("人民币");
        }
        if (result.getActualPriceUncompleteNum() == null) {
            result.setActualPriceUncompleteNum("0");
            result.setActualPriceUnit("人民币");
        }
        if (result.getEstimatedPriceCompleteNum() == null) {
            result.setEstimatedPriceCompleteNum("0");
            result.setEstimatedPriceUnit("人民币");
        }
        if (result.getEstimatedPriceUncompleteNum() == null) {
            result.setEstimatedPriceUncompleteNum("0");
            result.setEstimatedPriceUnit("人民币");
        }
        list.add(result);
        return ResultUtil.success(result);
    }

    public Result getProcessDealPersoncollectByObject(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        param.setUserId(user.getId());
        List<GetBelongPersonCollerctList> dealPersonCollectLists = new ArrayList<GetBelongPersonCollerctList>();
        Integer number;
        List<GetBelongPersonCollerctList> result = new ArrayList<GetBelongPersonCollerctList>();
        param.setStatus(null);
        dealPersonCollectLists = processMapper.selectDealPersonList(param);
        for (GetBelongPersonCollerctList item : dealPersonCollectLists) {
            item.setBelongPersonTotal(item.getBelongPersonTotal());
            result.add(item);
            param.setStatus("COMPLETE");
            dealPersonCollectLists = processMapper.selectDealPersonList(param);
            for (GetBelongPersonCollerctList comlist : dealPersonCollectLists) {
                if(item.getBelongPersonId()==comlist.getBelongPersonId()){
                    number = comlist.getBelongPersonTotal();
                    item.setBelCompleteNum(number);
                }

            }
            param.setStatus("UNCOMPLETE");
            dealPersonCollectLists = processMapper.selectDealPersonList(param);
            for (GetBelongPersonCollerctList uncomlist : dealPersonCollectLists) {
                if(item.getBelongPersonId() == uncomlist.getBelongPersonId()){
                    number = uncomlist.getBelongPersonTotal();
                    item.setBelUncompleteNum(number);
                }

            }

        }
        if(dealPersonCollectLists.size()>0) {
            if (param.getPageNum() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNum(), param.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<GetBelongPersonCollerctList> pageInfo = new PageInfo<>(dealPersonCollectLists);
                return ResultUtil.success(pageInfo);
            }
            return ResultUtil.success(dealPersonCollectLists);
        }else{
            return ResultUtil.success(result);
        }
    }

    public Result getDemandcollectByObject(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        param.setUserId(user.getId());
        List<GetDemandCollectList> demandCollectLists = new ArrayList<GetDemandCollectList>();
        Integer number;
        List<GetDemandCollectList> result = new ArrayList<GetDemandCollectList>();
        param.setStatus(null);
        demandCollectLists = processMapper.selectDemandList(param);
        for (GetDemandCollectList item : demandCollectLists) {
            item.setDemandTotal(item.getDemandTotal());
            result.add(item);
            param.setStatus("COMPLETE");
            demandCollectLists = processMapper.selectDemandList(param);
            for (GetDemandCollectList comlist : demandCollectLists) {
                if(item.getCompanyId()==comlist.getCompanyId()){
                    number = comlist.getDemandTotal();
                    item.setDemCompleteNum(number);
                }

            }
            param.setStatus("UNCOMPLETE");
            demandCollectLists = processMapper.selectDemandList(param);
            for (GetDemandCollectList uncomlist : demandCollectLists) {
                if(item.getCompanyId() == uncomlist.getCompanyId()){
                    number = uncomlist.getDemandTotal();
                    item.setDemUncompleteNum(number);
                }

            }

        }
        if(demandCollectLists.size()>0){
            if(param.getPageNum()!=null&&param.getPageSize()!=null) {
                PageHelper.startPage(param.getPageNum(), param.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<GetDemandCollectList> pageInfo = new PageInfo<>(demandCollectLists);
                return ResultUtil.success(pageInfo);
            }else {
                return ResultUtil.success(demandCollectLists);
            }
        }else{
            return ResultUtil.success(result);
        }

    }

    public Result processCollectDown(ParamFdpProcessDetailed param) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = sdf.format(date);
        //查询客服工单汇总
        List<GetFdpProcessCollectList> collectLists = getProcesscollectByObjectByDown(param,user.getId());
        //查询客服工单雇主汇总
        List<GetDemandCollectList> demandCollectLists = getDemandcollectByObjectByDown(param,user.getId());
        //查询客服工单处理人汇总
        List<GetBelongPersonCollerctList> belongPersonCollerctLists =  getProcessDealPersoncollectByObjectByDown (param,user.getId());
        File file = null;
        OutputStream os;
        try {
            file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("临时文件夹创建成功");
            }
            if (collectLists.size() > 0 && demandCollectLists.size() >0 && belongPersonCollerctLists.size()>0) {
                //生成派工xls表
                String sCurrPath = file.getPath() + ExcelUtil.FILE_SEPARATOR + "ProcessColl_" + currTime + ".xls";
                os = new FileOutputStream(sCurrPath);
                ExcelUtil.exportProcessCollectExcel(collectLists,demandCollectLists,belongPersonCollerctLists, os);
            }

        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("找不到默认保存文件的目录，请联系管理员！");
        }
        File uploadFile = new File(file.getPath()+ ExcelUtil.FILE_SEPARATOR + "ProcessColl_" + currTime + ".xls");
        try {
            return fileService.upload(uploadFile.getName() , new FileInputStream(uploadFile), "excel");

        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("文件没有找到，请联系管理员！");
        }
    }

    public List<GetFdpProcessCollectList> getProcesscollectByObjectByDown (ParamFdpProcessDetailed param,int userId){
        GetFdpProcessCollectList result = new GetFdpProcessCollectList();
        param.setUserId(userId);
        GetFdpProcessCollectList collect;
        //获取本系统数量
        param.setSource("SYS");
        collect = processMapper.selectProcessCollect(param);
        result.setSysNum(collect.getProcessTotal());
        result.setProcessTotal(collect.getProcessTotal());
        //获取微信数量
        param.setSource("WECHAT");
        collect = processMapper.selectProcessCollect(param);
        result.setWechatNum(collect.getProcessTotal());
        //获取伯俊数量
        param.setSource("POS");
        collect = processMapper.selectProcessCollect(param);
        result.setPosNum(collect.getProcessTotal());
        //获取故障单总数量
        //param.setSource("process");
        //collect = processMapper.selectProcessCollect(param);
        //result.setProcessTotal(collect.getProcessTotal());
        //获取报账单总数量
        param.setSource("report");
        collect = processMapper.selectProcessCollect(param);
        result.setReportTotal(collect.getProcessTotal());
        //获取已完成数量以及完成预计与实际费用
        param.setStatus("COMPLETE");
        param.setSource(null);
        collect = processMapper.selectProcessCollect(param);
        result.setCompleteNum(collect.getProcessTotal());
        result.setActualPriceCompleteNum(collect.getActualPriceCompleteNum());
        result.setActualPriceUnit(collect.getActualPriceUnit());
        result.setEstimatedPriceCompleteNum(collect.getEstimatedPriceCompleteNum());
        result.setEstimatedPriceUnit(collect.getEstimatedPriceUnit());
        //获取未完成数量以及未完成预计与实际费用
        param.setStatus("UNCOMPLETE");
        collect = processMapper.selectProcessCollect(param);
        result.setUncompleteNum(collect.getProcessTotal());
        result.setActualPriceUncompleteNum(collect.getActualPriceCompleteNum());
        result.setActualPriceUnit(collect.getActualPriceUnit());
        result.setEstimatedPriceUncompleteNum(collect.getEstimatedPriceCompleteNum());
        result.setEstimatedPriceUnit(collect.getEstimatedPriceUnit());
        //获取取消数量
        param.setStatus("CANCEL");
        collect = processMapper.selectProcessCollect(param);
        result.setCancelNum(collect.getProcessTotal());
        List<GetFdpProcessCollectList> list = new ArrayList<GetFdpProcessCollectList>();
        //费用验证
        if (result.getActualPriceCompleteNum() == null) {
            result.setActualPriceCompleteNum("0");
            result.setActualPriceUnit("人民币");
        }
        if (result.getActualPriceUncompleteNum() == null) {
            result.setActualPriceUncompleteNum("0");
            result.setActualPriceUnit("人民币");
        }
        if (result.getEstimatedPriceCompleteNum() == null) {
            result.setEstimatedPriceCompleteNum("0");
            result.setEstimatedPriceUnit("人民币");
        }
        if (result.getEstimatedPriceUncompleteNum() == null) {
            result.setEstimatedPriceUncompleteNum("0");
            result.setEstimatedPriceUnit("人民币");
        }
        list.add(result);
        return list;
    }

    public List<GetDemandCollectList> getDemandcollectByObjectByDown (ParamFdpProcessDetailed param,int userId){
        param.setUserId(userId);
        List<GetDemandCollectList> demandCollectLists = new ArrayList<GetDemandCollectList>();
        Integer number;
        List<GetDemandCollectList> result = new ArrayList<GetDemandCollectList>();
        param.setStatus(null);
        demandCollectLists = processMapper.selectDemandList(param);
        for (GetDemandCollectList item : demandCollectLists) {
            item.setDemandTotal(item.getDemandTotal());
            result.add(item);
            param.setStatus("COMPLETE");
            demandCollectLists = processMapper.selectDemandList(param);
            for (GetDemandCollectList comlist : demandCollectLists) {
                if(item.getCompanyId()==comlist.getCompanyId()){
                    number = comlist.getDemandTotal();
                    item.setDemCompleteNum(number);
                }

            }
            param.setStatus("UNCOMPLETE");
            demandCollectLists = processMapper.selectDemandList(param);
            for (GetDemandCollectList uncomlist : demandCollectLists) {
                if(item.getCompanyId() == uncomlist.getCompanyId()){
                    number = uncomlist.getDemandTotal();
                    item.setDemUncompleteNum(number);
                }

            }

        }
        return result;
    }

    public List<GetBelongPersonCollerctList> getProcessDealPersoncollectByObjectByDown(ParamFdpProcessDetailed param, int userId) {
        param.setUserId(userId);
        List<GetBelongPersonCollerctList> dealPersonCollectLists = new ArrayList<GetBelongPersonCollerctList>();
        Integer number;
        List<GetBelongPersonCollerctList> result = new ArrayList<GetBelongPersonCollerctList>();
        param.setStatus(null);
        dealPersonCollectLists = processMapper.selectDealPersonList(param);
        for (GetBelongPersonCollerctList item : dealPersonCollectLists) {
            item.setBelongPersonTotal(item.getBelongPersonTotal());
            result.add(item);
            param.setStatus("COMPLETE");
            dealPersonCollectLists = processMapper.selectDealPersonList(param);
            for (GetBelongPersonCollerctList comlist : dealPersonCollectLists) {
                if(item.getBelongPersonId()==comlist.getBelongPersonId()){
                    number = comlist.getBelongPersonTotal();
                    item.setBelCompleteNum(number);
                }

            }
            param.setStatus("UNCOMPLETE");
            dealPersonCollectLists = processMapper.selectDealPersonList(param);
            for (GetBelongPersonCollerctList uncomlist : dealPersonCollectLists) {
                if(item.getBelongPersonId() == uncomlist.getBelongPersonId()){
                    number = uncomlist.getBelongPersonTotal();
                    item.setBelUncompleteNum(number);
                }

            }

        }
        return result;
    }

    public Result getFaultDispatchOrderRequestList(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        List<FdpFaultdispatchRequestList> result = new ArrayList<>();
        List<FdpFaultdispatchRequestList> list = requestList(getFaultDispatchRequestList,user.getId(),user.getCompanyId());
        //查询派工清单
        if(list.size()>0) {
            if (getFaultDispatchRequestList.getPageNum() != null && getFaultDispatchRequestList.getPageSize() != null) {
                PageHelper.startPage(getFaultDispatchRequestList.getPageNum(), getFaultDispatchRequestList.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<FdpFaultdispatchRequestList> pageInfo = new PageInfo<>(list);
                return ResultUtil.success(pageInfo);
            }else{
                return ResultUtil.success(list);
            }
        }else{
            return ResultUtil.success(result);
        }

    }

    private List<FdpFaultdispatchRequestList> requestList(GetFaultDispatchRequestList getFaultDispatchRequestList, int userId,int companyId) {

        SysCompanyUsers users = sysCompanyUsersMapper.selectByPrimaryKey(userId);
        getFaultDispatchRequestList.setUserCompanyId(companyId);
        getFaultDispatchRequestList.setVendorId(users.getActObjectId());
        List<FdpFaultdispatchRequestList> list = orderMapper.selectOrderList(getFaultDispatchRequestList);
        return list;
//        if(users.getActType().equals("VENDOR")){
//            getFaultDispatchRequestList.setVendorId(users.getActObjectId());
//            List<FdpFaultdispatchRequestList> list = orderMapper.selectOrderList(getFaultDispatchRequestList);
//            for (FdpFaultdispatchRequestList ll: list
//            ) {
//                JSONObject jsStr = JSONObject.fromObject(ll.getDispatchModelRequest());
//                JSONArray device = JSONArray.fromObject(ll.getDeviceRequest());
//                String dev ="";
//                String devdev="";
//                String devAll ="";
//
//                for(int i = 0;i<device.size();i++){
//                    jsStr = device.getJSONObject(i);
//                    for(int j =0 ;j<jsStr.size();j++){
//                        devdev = jsStr.getString(String.valueOf(j));
//                        if(devdev==null||devdev.equals("")){
//                            continue;
//                        }
//                        if(j==jsStr.size()-1){
//                            dev = devdev;
//                            devAll=devAll+dev;
//                        }else {
//                            dev = devdev + ",";
//                            devAll = devAll + dev;
//                        }
//                    }
//
//                    int indx = devAll.lastIndexOf(",");
//                    if(indx!=-1){
//                        devAll = devAll.substring(0,indx)+devAll.substring(indx+1,devAll.length());
//                    }
//                    devAll =devAll+";";
//                    int indAll = devAll.lastIndexOf(";");
//                    if(indAll!=-1){
//                        devAll = devAll.substring(0,indAll)+devAll.substring(indAll+1,devAll.length());
//                    }
//                }
//
//                ll.setDeviceRequest(devAll);
//                if(ll.isFlagFinish()==true){
//                    ll.setFinishTime(ll.getLastDealTime());
//
//                }
//
//            }
//
//            return list;
//
//        }
//        if(users.getActType().equals("DEMAND")){
//            getFaultDispatchRequestList.setVendorId(users.getActObjectId());
//            List<FdpFaultdispatchRequestList> list = orderMapper.selectOrderListDemand(getFaultDispatchRequestList);
//            return list;
//        }
//        else {  return new ArrayList<FdpFaultdispatchRequestList>();}
    }

    public Result exportFaultOrderRequestList(GetFaultDispatchRequestList getFaultDispatchRequestList) {

        SysCompanyUsers user = CurrentUtil.getCurrent();

        List<FdpFaultdispatchRequestList> list = requestList(getFaultDispatchRequestList,user.getId(),user.getCompanyId());

        // 下载
        long fileName = System.currentTimeMillis();
        OutputStream out;
        try {
            File filePath = new File(filepath);
            if (!filePath.exists()) {
                filePath.mkdir();
            }
            String sCurrPath = filepath + ExcelUtil.FILE_SEPARATOR + "REQUESTLIST_" + fileName + ".xls";
            out = new FileOutputStream(sCurrPath);
        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("找不到默认保存文件的目录，请联系管理员!");
        }

        ExcelUtil.exportRequestList(list, out);
        File uploadFile = new File(filepath + ExcelUtil.FILE_SEPARATOR + "REQUESTLIST_" + fileName + ".xls");
        try {
            return fileService.upload(fileName + ".xls", new FileInputStream(uploadFile), "excel");
        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("文件没有找到，请联系管理员！");
        }

    }

    public Result getFaultOrderRequestCount(GetFaultDispatchRequestList getFaultDispatchRequestList) {

        SysCompanyUsers user = CurrentUtil.getCurrent();
        List<FaultOrderRequestCount> list = new ArrayList<>();
        List<FaultOrderRequestCount> requestList = requestCountList(getFaultDispatchRequestList,user.getId());

        if(requestList.size()>0) {
            if(getFaultDispatchRequestList.getPageSize()!=null&&getFaultDispatchRequestList.getPageNum()!=null) {
                PageHelper.startPage(getFaultDispatchRequestList.getPageNum(), getFaultDispatchRequestList.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<FaultOrderRequestCount> pageInfo = new PageInfo<>(requestList);
                return ResultUtil.success(pageInfo);
            }else{
                return ResultUtil.success(requestList);
            }
        }else{
            return ResultUtil.success(list);
        }
    }

    /**
     * （方法）查询派工汇总
     * @param getFaultDispatchRequestList
     * @param userId
     * @return
     * @Author Liukan
     * @Date 2018/06/24
     */
    private List<FaultOrderRequestCount> requestCountList(GetFaultDispatchRequestList getFaultDispatchRequestList, int userId) {

        SysCompanyUsers users = sysCompanyUsersMapper.selectByPrimaryKey(userId);

        getFaultDispatchRequestList.setVendorId(users.getActObjectId());
        getFaultDispatchRequestList.setActType(users.getActType());
        int count = orderMapper.selectRequestCount(getFaultDispatchRequestList);
        List<FaultOrderRequestCount> requestList = new ArrayList<FaultOrderRequestCount>();
        FaultOrderRequestCount requestCount = new FaultOrderRequestCount();
        requestCount.setRequestCountAll(count);
        getFaultDispatchRequestList.setDispatchStatus("COMPLETE");
        BigDecimal actual = BigDecimal.ZERO;
        BigDecimal estimate = BigDecimal.ZERO;
        BigDecimal actualUN = BigDecimal.ZERO;
        BigDecimal estimateUN = BigDecimal.ZERO;
        String actualUnit ="人民币";
        String estimateUnit ="人民币";
        FaultOrderRequestCount request = orderMapper.selectRequestCompletePrice(getFaultDispatchRequestList);

        if (request == null) {
            actual = BigDecimal.ZERO;
            estimate = BigDecimal.ZERO;
            actualUnit ="人民币";
            estimateUnit ="人民币";

        } else {
            if (request.getActualPriceComplete() == null) {
                actual = BigDecimal.ZERO;
            }else{
                actual = request.getActualPriceComplete();
            }
            if (request.getActualPriceUnit() == null) {
                actualUnit = "人民币";
            }else{
                actualUnit = request.getActualPriceUnit();
            }
            if (request.getEstimatedPriceComplete() == null) {
                estimate = BigDecimal.ZERO;
            }else {
                estimate =request.getEstimatedPriceComplete();
            }
            if (request.getEstimatedPriceUnit() == null) {
                estimateUnit ="人民币";
            }else {
                estimateUnit =request.getEstimatedPriceUnit();
            }
        }

        requestCount.setActualPriceUnit(actualUnit);
        requestCount.setEstimatedPriceUnit(estimateUnit);
        requestCount.setActualPriceComplete(actual);
        requestCount.setEstimatedPriceComplete(estimate);
        int countComplete = orderMapper.selectRequestCount(getFaultDispatchRequestList);
        requestCount.setRequestCountComplete(countComplete);

        getFaultDispatchRequestList.setDispatchStatus("UNCOMPLETE");
        int countUnComplete = orderMapper.selectRequestCount(getFaultDispatchRequestList);
        requestCount.setReqeustCountUncomplete(countUnComplete);
        FaultOrderRequestCount requestUn = orderMapper.selectRequestCompletePrice(getFaultDispatchRequestList);
        if (requestUn == null) {
            actualUN = BigDecimal.ZERO;
            estimateUN = BigDecimal.ZERO;

        }else {
            if (requestUn.getActualPriceComplete() == null) {
                actualUN = BigDecimal.ZERO;
            }else {
                actualUN =requestUn.getActualPriceComplete();
            }
            if (requestUn.getEstimatedPriceComplete() == null) {
                estimateUN = BigDecimal.ZERO;
            }else
            {
                estimateUN =requestUn.getEstimatedPriceComplete();
            }
        }
        requestCount.setActualPriceUncomplete(actualUN);
        requestCount.setEstimatedPriceUncomplete(estimateUN);

        getFaultDispatchRequestList.setDispatchStatus("CANCEL");
        int countRequestCancel = orderMapper.selectRequestCount(getFaultDispatchRequestList);
        requestCount.setRequestCountCancel(countRequestCancel);

        requestList.add(requestCount);
        return requestList;
    }

    public Result getFaultOrderRequestCountByDemand(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        List<FaultOrderRequestCountByCompany> list = new ArrayList<>();
        List<FaultOrderRequestCountByCompany> listCompany =requestListCompany(getFaultDispatchRequestList,user.getId());
        if(listCompany.size()>0) {
            if (getFaultDispatchRequestList.getPageNum() != null && getFaultDispatchRequestList.getPageSize() != null) {
                PageHelper.startPage(getFaultDispatchRequestList.getPageNum(), getFaultDispatchRequestList.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<FaultOrderRequestCountByCompany> pageInfo = new PageInfo<>(listCompany);
                return ResultUtil.success(pageInfo);
            } else {
                return ResultUtil.success(listCompany);
            }
        }else{
            return ResultUtil.success(list);
        }
    }

    /**
     /**
     * （方法）派工雇主统计
     * @param getFaultDispatchRequestList
     * @param userId
     * @return
     */
    private List<FaultOrderRequestCountByCompany> requestListCompany(GetFaultDispatchRequestList getFaultDispatchRequestList, int userId) {
        SysCompanyUsers users = sysCompanyUsersMapper.selectByPrimaryKey(userId);

        getFaultDispatchRequestList.setVendorId(users.getActObjectId());
        getFaultDispatchRequestList.setActType(users.getActType());
        List<FaultOrderRequestCount> listAll = orderMapper.selectRequestByCompanyAll(getFaultDispatchRequestList);

        List<FaultOrderRequestCount> listComplete = orderMapper.selectRequestByCompanyComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCount> listUnComplete = orderMapper.selectRequestByCompanyUnComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listCompany = new ArrayList<FaultOrderRequestCountByCompany>();


        for (FaultOrderRequestCount cc: listAll
        ) {
            FaultOrderRequestCountByCompany list = new FaultOrderRequestCountByCompany();
            list.setCompanyName(cc.getCompanyName());
            list.setAllCount(cc.getRequestCountAll());
            for ( FaultOrderRequestCount complete: listComplete
            ) {
                if(cc.getCompanyName().equals(complete.getCompanyName())){
                    list.setCompleteCount(complete.getRequestCountComplete());

                }
            }
            for(FaultOrderRequestCount unComplete :listUnComplete){

                if(cc.getCompanyName().equals(unComplete.getCompanyName())){
                    list.setUnCompleteCount(unComplete.getReqeustCountUncomplete());
                }
            }
            listCompany.add(list);
        }

        return listCompany;

    }

    public Result requestCountByCreatePerson(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        List<FaultOrderRequestCountByCompany> list = new ArrayList<>();
        List<FaultOrderRequestCountByCompany> listCreatePerson = requestCountByPerson(getFaultDispatchRequestList,user.getId());
        if(listCreatePerson.size()>0) {
            if (getFaultDispatchRequestList.getPageNum() != null && getFaultDispatchRequestList.getPageSize() != null) {
                PageHelper.startPage(getFaultDispatchRequestList.getPageNum(), getFaultDispatchRequestList.getPageSize());
                // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
                PageInfo<FaultOrderRequestCountByCompany> pageInfo = new PageInfo<>(listCreatePerson);
                return ResultUtil.success(pageInfo);
            } else {
                return ResultUtil.success(listCreatePerson);
            }
        }else{
            return ResultUtil.success(list);
        }
    }

    /**
     * (方法)查询派工工单创建人统计
     * @param getFaultDispatchRequestList
     * @param userId
     * @return
     * @Author Liukan
     * @Date 2018/06/19
     */
    private List<FaultOrderRequestCountByCompany> requestCountByPerson(GetFaultDispatchRequestList getFaultDispatchRequestList, int userId) {

        SysCompanyUsers users = sysCompanyUsersMapper.selectByPrimaryKey(userId);

        getFaultDispatchRequestList.setVendorId(users.getActObjectId());
        getFaultDispatchRequestList.setActType(users.getActType());
        List<FaultOrderRequestCount> listAll = orderMapper.selectRequestByCreatePersonyAll(getFaultDispatchRequestList);

        List<FaultOrderRequestCount> listComplete = orderMapper.selectRequestByCreatePersonyComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCount> listUnComplete = orderMapper.selectRequestByCreatePersonyUnComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listCreatePerson = new ArrayList<FaultOrderRequestCountByCompany>();


        for (FaultOrderRequestCount cc: listAll
        ) {
            FaultOrderRequestCountByCompany list = new FaultOrderRequestCountByCompany();
            list.setCompanyName(cc.getUsername());
            list.setAllCount(cc.getRequestCountAll());
            for ( FaultOrderRequestCount complete: listComplete
            ) {
                if(cc.getUsername().equals(complete.getUsername())){
                    list.setCompleteCount(complete.getRequestCountComplete());

                }
            }
            for(FaultOrderRequestCount unComplete :listUnComplete){

                if(cc.getUsername().equals(unComplete.getUsername())){
                    list.setUnCompleteCount(unComplete.getReqeustCountUncomplete());
                }
            }
            listCreatePerson.add(list);
        }

        return listCreatePerson;
    }

    public Result exportFaultOrderRequestCount(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        SysCompanyUsers user = CurrentUtil.getCurrent();
        List<FaultOrderRequestCount> requestCountList = requestCountList(getFaultDispatchRequestList,user.getId());
        List<FaultOrderRequestCountByCompany> requestListCompany =requestListCompany(getFaultDispatchRequestList,user.getId());
        List<FaultOrderRequestCountByCompany> requestCountByPerson = requestCountByPerson(getFaultDispatchRequestList,user.getId());
        // 下载
        long fileName = System.currentTimeMillis();
        OutputStream out;
        try {
            File filePath = new File(filepath);
            if (!filePath.exists()) {
                filePath.mkdir();
            }
            String sCurrPath = filepath + ExcelUtil.FILE_SEPARATOR + "REQUESTCOUNTLIST_" + fileName + ".xls";
            out = new FileOutputStream(sCurrPath);
        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("找不到默认保存文件的目录，请联系管理员!");
        }

        ExcelUtil.exportRequestCountList(requestCountList,requestListCompany, requestCountByPerson,out);
        File uploadFile = new File(filepath + ExcelUtil.FILE_SEPARATOR + "REQUESTCOUNTLIST_" + fileName + ".xls");
        try {
            return fileService.upload(fileName + ".xls", new FileInputStream(uploadFile), "excel");
        } catch (FileNotFoundException e) {
            return ResultUtil.validateError("文件没有找到，请联系管理员！");
        }
    }









    public Result getFaultDispatchOrderList(GetFdpFaultdispatchOrderList getFdpFaultdispatchOrderList) {
        if (getFdpFaultdispatchOrderList.getPageNum() != null && getFdpFaultdispatchOrderList.getPageSize() != null) {
            PageHelper.startPage(getFdpFaultdispatchOrderList.getPageNum(), getFdpFaultdispatchOrderList.getPageSize());
            Page<FdpFaultDispatchOrderList> persons = fdpFaultDispatchOrderMapper.selectDispatchOrderList(getFdpFaultdispatchOrderList);
            // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
            com.github.pagehelper.PageInfo<FdpFaultDispatchOrderList> pageInfo = new com.github.pagehelper.PageInfo<>(persons);
            return ResultUtil.success(pageInfo);
        } else {
            List<FdpFaultDispatchOrderList> persons = fdpFaultDispatchOrderMapper.selectDispatchOrderList(getFdpFaultdispatchOrderList);
            return ResultUtil.success(persons);
        }
    }

    public Result exportFaultOrderList(GetFdpFaultdispatchOrderList getFdpFaultdispatchOrderList) {
        List<FdpFaultDispatchOrderList> list = fdpFaultDispatchOrderMapper.selectDispatchOrderList(getFdpFaultdispatchOrderList);

        // 下载
        long fileName = System.currentTimeMillis();
        OutputStream out;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdir();
            }
            String sCurrPath = filepath + ExcelUtil.FILE_SEPARATOR + "ORDERLIST" + fileName + ".xls";
            out = new FileOutputStream(sCurrPath);
        } catch (FileNotFoundException e) {
            return ResultUtil.errorBusinessMsg("找不到默认保存文件的目录，请联系管理员!");
        }

        ExcelUtil.exportDispatchOrderList(list, out);
        File uploadFile = new File(filepath + ExcelUtil.FILE_SEPARATOR + "ORDERLIST" + fileName + ".xls");
        try {
            return fileService.upload(fileName + ".xls", new FileInputStream(uploadFile), "excel");
        } catch (FileNotFoundException e) {
            return ResultUtil.errorBusinessMsg("文件没有找到，请联系管理员！");
        }
    }

    public Result exportFaultOrderCount(GetFaultDispatchRequestList getFaultDispatchRequestList) {


        List<FaultOrderCount> orderList = faultOrderCount(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listCompany = faultOrderCountByDemand(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listLastDealPerson = faultOrderCountByDealPerson(getFaultDispatchRequestList);

        // 下载
        long fileName = System.currentTimeMillis();
        OutputStream out;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdir();
            }
            String sCurrPath = filepath + ExcelUtil.FILE_SEPARATOR + "ORDERCOUNTLIST_" + fileName + ".xls";
            out = new FileOutputStream(sCurrPath);
        } catch (FileNotFoundException e) {
            return ResultUtil.errorBusinessMsg("找不到默认保存文件的目录，请联系管理员!");
        }

        ExcelUtil.exportOrderCountList(orderList, listCompany, listLastDealPerson, out);
        File uploadFile = new File(filepath + ExcelUtil.FILE_SEPARATOR + "ORDERCOUNTLIST_" + fileName + ".xls");
        try {
            return fileService.upload(fileName + ".xls", new FileInputStream(uploadFile), "excel");
        } catch (FileNotFoundException e) {
            return ResultUtil.errorBusinessMsg("文件没有找到，请联系管理员！");
        }

    }

    /**
     * (方法)查询采购工单统计
     *
     * @param getFaultDispatchRequestList
     * @return
     * @Author Liukan
     * @Date 2018/06/24
     */
    private List<FaultOrderCount> faultOrderCount(GetFaultDispatchRequestList getFaultDispatchRequestList) {

        int count = fdpFaultDispatchOrderMapper.selectOrderCount(getFaultDispatchRequestList);
        List<FaultOrderCount> orderList = new ArrayList<FaultOrderCount>();
        FaultOrderCount orderCount = new FaultOrderCount();
        orderCount.setOrderCountAll(count);
        getFaultDispatchRequestList.setDispatchStatus("COMPLETE");
        BigDecimal actual = BigDecimal.ZERO;
        BigDecimal estimate = BigDecimal.ZERO;
        BigDecimal actualUN = BigDecimal.ZERO;
        BigDecimal estimateUN = BigDecimal.ZERO;
        BigDecimal actualCost = BigDecimal.ZERO;
        BigDecimal actualCostUn = BigDecimal.ZERO;
        BigDecimal estimateCost = BigDecimal.ZERO;
        BigDecimal estimateCostUn = BigDecimal.ZERO;

        FaultOrderCount order = fdpFaultDispatchOrderMapper.selectOrderCompletePrice(getFaultDispatchRequestList);

        if (order == null) {
            actual = BigDecimal.ZERO;
            estimate = BigDecimal.ZERO;
            actualCost = BigDecimal.ZERO;
            estimateCost = BigDecimal.ZERO;
            orderCount.setActualMonetaryUnit("");
            orderCount.setActualPriceUnit("");
            orderCount.setEstimatedMonetaryUnit("");
            orderCount.setEstimatedPriceUnit("");

        } else {
            if (order.getSumEstimatedPrice() == null) {
                estimate = BigDecimal.ZERO;
            } else {
                estimate = order.getSumEstimatedPrice();
            }
            if (order.getSumActualPrice() == null) {
                actual = BigDecimal.ZERO;
            } else {
                actual = order.getSumActualPrice();
            }
            if (order.getSumActualCost() == null) {
                actualCost = BigDecimal.ZERO;
            } else {
                actualCost = order.getSumActualCost();

            }
            if (order.getSumEstimatedCost() == null) {
                estimateCost = BigDecimal.ZERO;
            } else {
                estimateCost = order.getSumEstimatedCost();
            }
            orderCount.setActualMonetaryUnit(order.getActualMonetaryUnit());
            orderCount.setActualPriceUnit(order.getActualPriceUnit());
            orderCount.setEstimatedMonetaryUnit(order.getEstimatedMonetaryUnit());
            orderCount.setEstimatedPriceUnit(order.getEstimatedPriceUnit());
        }

        orderCount.setSumActualCost(actualCost);
        orderCount.setSumEstimatedCost(estimateCost);
        orderCount.setSumActualPrice(actual);
        orderCount.setSumEstimatedPrice(estimate);
        int countComplete = fdpFaultDispatchOrderMapper.selectOrderCount(getFaultDispatchRequestList);
        orderCount.setOrderCountComplete(countComplete);

        getFaultDispatchRequestList.setDispatchStatus("UNCOMPLETE");
        int countUnComplete = fdpFaultDispatchOrderMapper.selectOrderCount(getFaultDispatchRequestList);
        orderCount.setOrderCountUnComplete(countUnComplete);
        FaultOrderCount orderUn = fdpFaultDispatchOrderMapper.selectOrderCompletePrice(getFaultDispatchRequestList);
        if (orderUn == null) {
            actualUN = BigDecimal.ZERO;
            estimateUN = BigDecimal.ZERO;
            actualCostUn = BigDecimal.ZERO;
            estimateCostUn = BigDecimal.ZERO;
        } else {
            if (orderUn.getSumActualPrice() == null) {
                actualUN = BigDecimal.ZERO;
            } else {
                actualUN = orderUn.getSumActualPrice();
            }
            if (orderUn.getSumEstimatedPrice() == null) {
                estimateUN = BigDecimal.ZERO;
            } else {
                estimateUN = orderUn.getSumEstimatedPrice();
            }
            if (orderUn.getSumEstimatedCost() == null) {
                estimateCostUn = BigDecimal.ZERO;
            } else {
                estimateCostUn = orderUn.getSumEstimatedCost();
            }
            if (orderUn.getSumActualCost() == null) {
                actualCostUn = BigDecimal.ZERO;
            } else {
                actualCostUn = orderUn.getSumActualCost();
            }
        }
        orderCount.setSumUnActualCost(actualCostUn);
        orderCount.setSumUnEstimatedCost(estimateCostUn);
        orderCount.setSumUnActualPrice(actualUN);
        orderCount.setSumUnEstimatedPrice(estimateUN);

        getFaultDispatchRequestList.setDispatchStatus("CANCEL");
        int countOrderCancel = fdpFaultDispatchOrderMapper.selectOrderCount(getFaultDispatchRequestList);
        orderCount.setOrderCountCancel(countOrderCancel);

        orderList.add(orderCount);
        return orderList;
    }

    private List<FaultOrderRequestCountByCompany> faultOrderCountByDemand(GetFaultDispatchRequestList getFaultDispatchRequestList) {

        List<FaultOrderCount> listAll = fdpFaultDispatchOrderMapper.selectOrderByCompanyAll(getFaultDispatchRequestList);

        List<FaultOrderCount> listComplete = fdpFaultDispatchOrderMapper.selectOrderByCompanyComplete(getFaultDispatchRequestList);

        List<FaultOrderCount> listUnComplete = fdpFaultDispatchOrderMapper.selectOrderByCompanyUnComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listCompany = new ArrayList<FaultOrderRequestCountByCompany>();


        for (FaultOrderCount cc : listAll) {
            FaultOrderRequestCountByCompany list = new FaultOrderRequestCountByCompany();
            list.setCompanyName(cc.getCompanyName());
            list.setAllCount(cc.getOrderCountAll());
            for (FaultOrderCount complete : listComplete) {
                if (cc.getCompanyName().equals(complete.getCompanyName())) {
                    list.setCompleteCount(complete.getOrderCountComplete());

                }
            }
            for (FaultOrderCount unComplete : listUnComplete) {

                if (cc.getCompanyName().equals(unComplete.getCompanyName())) {
                    list.setUnCompleteCount(unComplete.getOrderCountUnComplete());
                }
            }
            listCompany.add(list);
        }

        return listCompany;

    }

    private List<FaultOrderRequestCountByCompany> faultOrderCountByDealPerson(GetFaultDispatchRequestList getFaultDispatchRequestList) {


        List<FaultOrderCount> listAll = fdpFaultDispatchOrderMapper.selectOrderByDealPersonAll(getFaultDispatchRequestList);

        List<FaultOrderCount> listComplete = fdpFaultDispatchOrderMapper.selectOrderByCreatePersonComplete(getFaultDispatchRequestList);

        List<FaultOrderCount> listUnComplete = fdpFaultDispatchOrderMapper.selectOrderByCreatePersonUnComplete(getFaultDispatchRequestList);

        List<FaultOrderRequestCountByCompany> listLastDealPerson = new ArrayList<FaultOrderRequestCountByCompany>();


        for (FaultOrderCount cc : listAll
        ) {
            FaultOrderRequestCountByCompany list = new FaultOrderRequestCountByCompany();
            list.setCompanyName(cc.getCompanyName());
            list.setAllCount(cc.getOrderCountAll());
            for (FaultOrderCount complete : listComplete
            ) {
                if (cc.getCompanyName().equals(complete.getCompanyName())) {
                    list.setCompleteCount(complete.getOrderCountComplete());

                }
            }
            for (FaultOrderCount unComplete : listUnComplete) {

                if (cc.getCompanyName().equals(unComplete.getCompanyName())) {
                    list.setUnCompleteCount(unComplete.getOrderCountUnComplete());
                }
            }
            listLastDealPerson.add(list);
        }

        return listLastDealPerson;

    }

    /**
     * 查询采购工单统计
     *
     * @param getFaultDispatchRequestList
     * @return
     */
    public Result getFaultOrderCount(GetFaultDispatchRequestList getFaultDispatchRequestList) {

        List<FaultOrderCount> orderList = faultOrderCount(getFaultDispatchRequestList);

        return ResultUtil.success(orderList);

    }

    public Result getFaultOrderCountByDemand(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        List<FaultOrderRequestCountByCompany> listCompany = faultOrderCountByDemand(getFaultDispatchRequestList);
        return ResultUtil.success(listCompany);
    }

    public Result getFaultOrderCountByDealPerson(GetFaultDispatchRequestList getFaultDispatchRequestList) {
        List<FaultOrderRequestCountByCompany> listLastDealPerson = faultOrderCountByDealPerson(getFaultDispatchRequestList);
        return ResultUtil.success(listLastDealPerson);
    }

}