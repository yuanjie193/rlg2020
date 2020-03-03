package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

import java.util.Map;

public interface AliPayService {
    ServerResponse pay(Users user, Long orderNo);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Users user, Long orderNo);
}
