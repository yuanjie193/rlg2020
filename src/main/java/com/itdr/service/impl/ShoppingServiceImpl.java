package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.mapper.ShoppingMapper;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.Users;
import com.itdr.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    ShoppingMapper shoppingMapper;

    @Override
    public ServerResponse add(Shopping shopping, Users user) {
        if(shopping == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        shopping.setUserId(user.getId());
        int insert = shoppingMapper.insert(shopping);
        if(insert<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.FAIL_ADD.getDesc());
        }
        //查找用户地址
        List list = shoppingMapper.selectByUserID(user.getId());
        return ServerResponse.successRS(list);
    }
}
