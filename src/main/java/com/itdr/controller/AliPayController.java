package com.itdr.controller;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.pojo.Users;
import com.itdr.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/portal/ali/")
public class AliPayController {
    @Autowired
    AliPayService aliPayService;
    @RequestMapping("pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return aliPayService.pay(user,orderNo);
    }
}
