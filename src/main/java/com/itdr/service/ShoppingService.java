package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.Users;

public interface ShoppingService {
    ServerResponse add(Shopping shopping, Users user);
}
