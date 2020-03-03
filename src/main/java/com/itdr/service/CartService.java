package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface CartService {
    ServerResponse list(Users user);

    ServerResponse add(Integer productID, Integer count, Integer type,Users user);

    ServerResponse deleteProduct(Integer productID, Users user);

    ServerResponse updateProduct(Integer productId, Integer count,Integer type, Users user);

    ServerResponse getCartProductCount(Users user);

    ServerResponse select(Integer productID,Users user);

    ServerResponse selectAll(Users user);

    ServerResponse unSelectAll(Users user);

    ServerResponse getCartProductCountAll(Users user);

    ServerResponse over(Users user);
}
