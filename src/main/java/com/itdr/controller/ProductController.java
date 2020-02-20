package com.itdr.controller;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("portal/product/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("baseCategory.do")
    public ServerResponse<Category> baseCategory(Integer pid){
        return productService.baseCategory(pid);
    }
    @RequestMapping("detail.do")
    public ServerResponse<Product> detail(Integer productId){
        return productService.detail(productId);
    }

}
