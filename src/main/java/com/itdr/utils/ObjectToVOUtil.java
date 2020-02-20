package com.itdr.utils;

import com.itdr.pojo.Product;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.pojo.vo.UsersVO;

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
    public static ProductVO ProductToVO(Product product){
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
}
