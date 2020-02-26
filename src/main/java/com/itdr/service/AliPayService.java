package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface AliPayService {
    ServerResponse pay(Users user, Long orderNo);
}
