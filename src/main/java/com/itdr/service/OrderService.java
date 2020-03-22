package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface OrderService {
    ServerResponse creat(Users user, Integer shoppingID);

    ServerResponse getOrderCartProduct(Users user, Long orderNo);

    ServerResponse getOrderList(Users user, Integer pageSize, Integer pageNum);

    ServerResponse getStatusNumber(Users user);

    ServerResponse getCancelOrder(Users user, Integer type);

    ServerResponse toCancelOrder(Users user, Long orderNo);
}
