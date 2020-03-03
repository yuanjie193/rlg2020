package com.itdr.controller;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.pojo.Users;
import com.itdr.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@ResponseBody
@RequestMapping("portal/cart/")
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 查看购物车列表
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session){
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.list(user);
    }

    /**
     * 购物车添加商品
     * @param productID
     * @param count
     * @param session
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse add(Integer productID,
                              @RequestParam(value = "count", required = false, defaultValue = "1") Integer count,
                              @RequestParam(value = "type", required = false, defaultValue = "0")Integer type,
                              HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.add(productID,count,type,user);
    }
    /**
     * 删除购物车一种商品或购物车选中商品
     * @param productIDs
     * @param session
     * @return
     */
    /*@RequestMapping("delete_product.do")
    public ServerResponse deleteProduct(String productIDs,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.deleteProduct(productIDs,user) ;
    }*/
    @RequestMapping("delete_product.do")
    public ServerResponse deleteProduct(Integer productID,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.deleteProduct(productID,user) ;
    }

    /**
     * 更新商品数目
     * @param productID
     * @param count
     * @param session
     * @return
     */
    @RequestMapping("update_product.do")
    public ServerResponse updateProduct(Integer productID,Integer count,
                                        @RequestParam(value = "type", required = false, defaultValue = "0")Integer type
                                        ,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.updateProduct(productID,count,type,user) ;
    }
    /**
     * 获取购物车中选中商品总数
     * @param
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    public ServerResponse getCartProductCount(Integer productID,Integer count,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.getCartProductCount(user) ;
    }

    /**
     * 获取购物车中全部商品总数
     * @param user
     * @return
     */
    @RequestMapping("get_cart_product_count_all.do")
    public ServerResponse getCartProductCountAll(Integer productID,Integer count,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.getCartProductCountAll(user) ;
    }

    /**
     *  选中商品或取消选中商品
     * @param productID
     * @param session
     * @return
     */
    @RequestMapping("select.do")
    public ServerResponse select(Integer productID,HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.select(productID,user) ;
    }

    /**
     * 购物车商品全选
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
    public ServerResponse selectAll(HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.selectAll(user) ;
    }
    /**
     * 购物车商品取消全选
     * @param session
     * @return
     */
    @RequestMapping("un_select_all.do")
    public ServerResponse unSelectAll(HttpSession session){
        //用户是否登录
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return cartService.unSelectAll(user) ;
    }

    @RequestMapping("over.do")
    public ServerResponse over(HttpSession session){
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return  cartService.over(user);
    }
}
