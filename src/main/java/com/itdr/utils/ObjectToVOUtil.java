package com.itdr.utils;

import com.itdr.pojo.Cart;
import com.itdr.pojo.Product;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.CartProductVO;
import com.itdr.pojo.vo.CartVO;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.pojo.vo.UsersVO;

import java.math.BigDecimal;
import java.util.List;

public class ObjectToVOUtil {
    /**
     * 用户类封装
     * @param u
     * @return
     */
    public static UsersVO usersToUsersVO(Users u){
        UsersVO uv = new UsersVO();
        uv.setId(u.getId());
        uv.setUsername(u.getUsername());
        uv.setEmail(u.getEmail());
        uv.setPhone(u.getPhone());
        uv.setCreateTime(u.getCreateTime());
        uv.setUpdateTime(u.getUpdateTime());
        return uv;
    }

    /**
     * 商品类封装
     * @param product
     * @return
     */
    public static ProductVO productToVO(Product product){
        ProductVO pv = new ProductVO();
        pv.setBanner(product.getBanner());
        pv.setCategoryId(product.getCategoryId());
        pv.setCreateTime(product.getCreateTime());
        pv.setDetail(product.getDetail());
        pv.setHot(product.getHot());
        pv.setMainImage(product.getMainImage());
        pv.setId(product.getId());
        pv.setName(product.getName());
        pv.setNew(product.getNew());
        pv.setUpdateTime(product.getUpdateTime());
        pv.setSubtitle(product.getSubtitle());
        pv.setStock(product.getStock());
        pv.setStatus(product.getStatus());
        pv.setSubImages(product.getSubImages());
        pv.setPrice(product.getPrice());
        pv.setImageHost(PropertiesUtil.getValue("ImageHost"));
        return pv;
    }

    /**
     * 封装购物车和商品数据
     * @param c
     * @param p
     * @return
     */
    public static CartProductVO cartAndProductToVO(Cart c,Product p ){
        CartProductVO cv = new CartProductVO();
        cv.setId(c.getId());
        cv.setUserId(c.getUserId());
        cv.setProductId(c.getProductId());
        cv.setQuantity(c.getQuantity());
        cv.setProductName(p.getName());
        cv.setProductSubtitle(p.getSubtitle());
        cv.setProductStock(p.getStock());//库存
        cv.setProductMainImage(p.getMainImage());//商品图片
        cv.setProductStatus(p.getStatus());//商品状态
        cv.setProductPrice(p.getPrice());
        //商品是否选中
        cv.setProductChecked(c.getChecked());
        //一条购物信息总价
        cv.setProductTotalPrice(BigDecimalUtil.mul(c.getQuantity(),p.getPrice().doubleValue()));
        //商品是否超过库存，默认未超过，如果超过返回失败
        String limitQuantity ="LIMIT_NUM_SUCCESS";
        if(c.getQuantity()>p.getStock()){
          limitQuantity = "LIMIT_NUM_FAILURE";
        }
        cv.setLimitQuantity(limitQuantity);
        return cv;
    }
    public static CartVO toCartVO(List<CartProductVO> list, Boolean bol, BigDecimal b){
       CartVO cv = new CartVO();
       cv.setCartProductVOList(list);
       //是否全选
        cv.setAllCheck(bol);
        //商品总价格
        cv.setCartTotalPrice(b);
       return cv;
    }
}
