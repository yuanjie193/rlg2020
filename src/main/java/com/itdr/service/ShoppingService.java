package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.Users;

public interface ShoppingService {
    ServerResponse add(Shopping shopping, Users user);

    ServerResponse selectAddress(Users user);

    ServerResponse updateAddress(Shopping shopping,Users user);

    ServerResponse deleteAddress(Integer shoppingID, Users user);
}
