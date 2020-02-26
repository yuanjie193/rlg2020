package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.service.AliPayService;
import org.springframework.stereotype.Service;

@Service
public class AliPayServiceImpl implements AliPayService {
    @Override
    public ServerResponse pay(Users user, Long orderNo) {
        return null;
    }
}
