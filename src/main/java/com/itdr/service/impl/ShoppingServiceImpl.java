package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.mapper.ShoppingMapper;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.ShoppingVO2;
import com.itdr.service.ShoppingService;
import com.itdr.utils.ObjectToVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    ShoppingMapper shoppingMapper;
    private ServerResponse getNotNull(Shopping shopping){
        if(shopping == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(StringUtils.isEmpty(shopping.getReceiverName())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        if(StringUtils.isEmpty(shopping.getReceiverPhone()) && StringUtils.isEmpty(shopping.getReceiverMobile())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_RECEIVER_PHONE.getDesc());
        }
        if(StringUtils.isEmpty(shopping.getReceiverProvince())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        if(StringUtils.isEmpty(shopping.getReceiverCity())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        if(StringUtils.isEmpty(shopping.getReceiverDistrict())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        if(StringUtils.isEmpty(shopping.getReceiverAddress())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        return ServerResponse.successRS();
    }

    @Override
    public ServerResponse add(Shopping shopping, Users user) {
        ServerResponse notNull = getNotNull(shopping);
        if(!notNull.isSuccess()){
            return notNull;
        }
        // 把当前用户id赋值给地址的用户ID
        shopping.setUserId(user.getId());
        if(shopping.getReceiverPhone().equals("true")){
         //把当前用户的所有地址设为非默认0
           int i =  shoppingMapper.updateByUserID(user.getId());
           System.out.println(i);
        }
        int insert = shoppingMapper.insert(shopping);
        if(insert<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.FAIL_ADD.getDesc());
        }
        //查找用户地址
        List list = shoppingMapper.selectByUserID(user.getId());
        if(list.isEmpty()){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
        }
        return ServerResponse.successRS(list);
    }

    /**
     * 查找用户地址
     * @param user
     * @return
     */
    @Override
    public ServerResponse selectAddress(Users user) {
        //查找用户地址
        List<Shopping> list = shoppingMapper.selectByUserID(user.getId());
        if(list.isEmpty()){
            return ServerResponse.defeatedRS(Const.ShoppingEnum.NO_ADDRESS.getCode(),Const.ShoppingEnum.NO_ADDRESS.getDesc());
        }
        List<ShoppingVO2> vo2List = new ArrayList<ShoppingVO2>();
        for (Shopping shopping : list) {
            ShoppingVO2 shoppingVO2 = ObjectToVOUtil.shippingToShippingVO2(shopping);
            vo2List.add(shoppingVO2);
        }
        return ServerResponse.successRS(vo2List);
    }

    /**
     * 根据地址id查找地址
     * @param user
     * @param sid
     * @return
     */
    @Override
    public ServerResponse selectOneAddress(Users user, Integer sid) {
        if(sid == null || sid <=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        Shopping s  = shoppingMapper.selectByPrimaryKey(sid,user.getId());
        if(s == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NOT_SHOP_ADDRESS.getDesc());
        }
        return ServerResponse.successRS(s);
    }

    /**
     * 查找默认收货地址
     * @param user
     * @return
     */

    @Override
    public ServerResponse selectDefaultAddress(Users user) {
        Shopping shopping = shoppingMapper.selectByDefaultAddress(user.getId());
        if(shopping != null){
            ShoppingVO2 shoppingVO2 = ObjectToVOUtil.shippingToShippingVO2(shopping);
            return ServerResponse.successRS(shoppingVO2);
        }
        return ServerResponse.successRS(Const.ShoppingEnum.NO_DEFAULT_ADDRESS.getCode(),Const.ShoppingEnum.NO_DEFAULT_ADDRESS.getDesc());
    }

    @Override
    public ServerResponse updateAddress(Shopping shopping,Users user) {
        if(shopping == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(StringUtils.isEmpty(shopping.getId())){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
        //查看用户是否有此收获地址
        Shopping s  = shoppingMapper.selectByPrimaryKey(shopping.getId(),user.getId());
        if(s == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NOT_SHOP_ADDRESS.getDesc());
        }
        if(shopping.getReceiverPhone() != null && shopping.getReceiverPhone().equals("true")) {
            //把当前用户的所有地址设为非默认false
            int i = shoppingMapper.updateByUserID(user.getId());
            System.out.println(i);
        }
        int update = shoppingMapper.updateByPrimaryKeySelective(shopping);
        if(update<=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NOT_SHOP_ADDRESS.getDesc());
        }
        //查找用户地址
        List list = shoppingMapper.selectByUserID(user.getId());
        if(list.isEmpty()){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
        }
        return ServerResponse.successRS(list);
    }

    @Override
    public ServerResponse deleteAddress(Integer shoppingID, Users user) {
        if(StringUtils.isEmpty(shoppingID)){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.EMPTY_INFORMATION.getDesc());
        }
       int i =  shoppingMapper.deleteByPrimaryKeyAndUserID(shoppingID,user.getId());
        if(i<=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NOT_SHOP_ADDRESS.getDesc());
        }
        //查找用户地址
        List list = shoppingMapper.selectByUserID(user.getId());
        if(list.isEmpty()){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
        }
        return ServerResponse.successRS(list);
    }
}
