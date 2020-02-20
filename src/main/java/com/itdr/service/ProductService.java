package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;

public interface ProductService {
    ServerResponse<Category> baseCategory(Integer pid);

    ServerResponse<Product> detail(Integer productId);
}
