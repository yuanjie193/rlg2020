package com.itdr.controller;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.service.ProductService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("portal/product/")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 获取商品直接子类
     * @param pid
     * @return
     */
    @RequestMapping("baseCategory.do")
    public ServerResponse<Category> baseCategory(Integer pid){
        return productService.baseCategory(pid);
    }

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    public ServerResponse<Product> detail(Integer productId){
        return productService.detail(productId);
    }

    /**
     * 获取商品列表
     * @param keyWord
     * @param pageName
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse<Product> list(String keyWord,
                        @RequestParam(value = "pageName",required = false,defaultValue = "1") Integer pageName,//页码
                        @RequestParam(value = "pageSize",required = false,defaultValue = "5")Integer pageSize,//每页数目
                        @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){     //排序规则
        return productService.list(keyWord,pageName,pageSize,orderBy);
    }
    @RequestMapping("show_product.do")
    public ServerResponse<Product> showProduct(String type){     //排序规则
        return productService.showProduct(type);
    }
}
