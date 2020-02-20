package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.mapper.CategoryMapper;
import com.itdr.mapper.ProductMapper;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.service.ProductService;
import com.itdr.utils.ObjectToVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private  ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse<Category> baseCategory(Integer pid) {
        //判断数据合法性
        if(pid == null || pid < 0 ){
            return ServerResponse.defeatedRS(Const.ProductEnum.ERROR_PAMAR.getCode(),
                    Const.ProductEnum.ERROR_PAMAR.getDesc());
        }
        List<Category> li = categoryMapper.selectByParentID(pid);
        return ServerResponse.successRS(li);
    }

    @Override
    public ServerResponse<Product> detail(Integer productId) {
        //判断数据合法性
        if(productId == null || productId < 0 ){
            return ServerResponse.defeatedRS(Const.ProductEnum.ERROR_PAMAR.getCode(),
                    Const.ProductEnum.ERROR_PAMAR.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);

        if(product == null || product.getStatus() != 1){
            return ServerResponse.defeatedRS(Const.ProductEnum.NO_PRODUCT.getCode(),
                    Const.ProductEnum.NO_PRODUCT.getDesc());
        }
        //封装VO
        ProductVO productVO = ObjectToVOUtil.ProductToVO(product);
        //返回成功数据
        return ServerResponse.successRS(productVO);
    }
}
