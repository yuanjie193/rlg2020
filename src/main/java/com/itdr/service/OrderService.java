package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface OrderService {
    ServerResponse creat(Users user, Integer shoppingID);

    ServerResponse getOrderCartProduct(Users user, Long orderNo);

    ServerResponse getOrderList(Users user, Integer pageSize, Integer pageNum);
}
