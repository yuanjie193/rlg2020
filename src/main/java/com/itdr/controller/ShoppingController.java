package com.itdr.controller;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.Users;
import com.itdr.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/portal/shopping/")
public class ShoppingController {
    @Autowired
    ShoppingService shoppingService;

    /**
     * 增加收货地址
     * @param shopping
     * @param session
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse addAddress(Shopping shopping, HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return shoppingService.add(shopping,user);
    }

    /**
     * 查找收获地址
     * @param session
     * @return
     */
    @RequestMapping("select_address.do")
    public ServerResponse selectAddress(HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return shoppingService.selectAddress(user);
    }

    /**
     * 查找单个收货地址
     * @param sid
     * @param session
     * @return
     */
    @RequestMapping("select_one_address.do")
    public ServerResponse selectOneAddress(Integer sid,HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return shoppingService.selectOneAddress(user,sid);
    }
    /**
     * 查找默认收货地址
     * @param sid
     * @param session
     * @return
     */
    @RequestMapping("select_default_address.do")
    public ServerResponse selectDefaultAddress(HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return shoppingService.selectDefaultAddress(user);
    }
    /**
     * 更新收货地址
     * @param shopping
     * @param session
     * @return
     */
    @RequestMapping("update_address.do")
    public ServerResponse updateAddress(Shopping shopping,HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return shoppingService.updateAddress(shopping,user);
    }

    @RequestMapping("delete_address.do")
    public ServerResponse deleteAddress(Integer shoppingID,HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }

        return shoppingService.deleteAddress(shoppingID,user);
    }
}
