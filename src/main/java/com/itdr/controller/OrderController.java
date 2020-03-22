package com.itdr.controller;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.pojo.Users;
import com.itdr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/portal/order/")
public class OrderController {
    @Autowired
    OrderService orderService;
    /**
     * 创建订单
     * @param session
     * @param shoppingID
     * @return
     */
    @RequestMapping("creat.do")
    public ServerResponse creat(HttpSession session,Integer shoppingID){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  orderService.creat(user,shoppingID);
    }

    /**
     * 获取用户订单商品详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session,
                                @RequestParam(value = "orderNo" ,required = false) Long orderNo){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        System.out.println(orderNo);
        return  orderService.getOrderCartProduct(user,orderNo);
    }

    @RequestMapping("get_order_list.do")
    public ServerResponse getOrderList(HttpSession session,
                                       @RequestParam(value = "pageSize" ,required = false,defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "pageNum" ,required = false,defaultValue = "1") Integer pageNum){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  orderService.getOrderList(user,pageSize,pageNum);
    }

    /**
     * 获取对应的订单状态数目
     * @param session
     * @return
     */
    @RequestMapping("get_status_number.do")
    public ServerResponse getStatusNumber(HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  orderService.getStatusNumber(user);
    }
    /**
     * 获取对应订单
     * @param session
     * @return
     */
    @RequestMapping("get_cancel_order.do")
    public ServerResponse getCancelOrder(HttpSession session,Integer type){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  orderService.getCancelOrder(user,type);
    }
    /**
     * 取消订单
     * @param session
     * @return
     */
    @RequestMapping("cancel_order.do")
    public ServerResponse toCancelOrder(HttpSession session,Long orderNo ){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  orderService.toCancelOrder(user,orderNo);
    }
}
