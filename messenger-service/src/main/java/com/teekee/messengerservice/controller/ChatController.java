package com.teekee.messengerservice.controller;

import com.teekee.commoncomponets.utils.Result;
import com.teekee.commoncomponets.utils.ResultUtil;
import com.teekee.messengerservice.entity.message.Chat;
import com.teekee.messengerservice.service.socket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @description: web聊天
 * @author: zhangshuai
 * @create: 2018-11-02 09:26
 */
@Api(value = "ChatController", tags = "chat-controller")
@RestController
@RequestMapping("/chat")
@CrossOrigin
public class ChatController {
    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ApiOperation(
            value = "推送数据服务",
            notes = "推送数据服务",
            consumes = "application/json",
            responseReference = "com.teekee.commoncomponets.utils.Result"
    )
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/push",method=RequestMethod.POST)
    public Result push(@RequestBody Chat chatRequest) {
        try {
            WebSocketServer.sendInfo(chatRequest.getMessage(),chatRequest.getCid());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(100,chatRequest.getCid()+"#"+e.getMessage());
        }
        return ResultUtil.success();
    }
}