package com.itdr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public  class ProductServiceImpl implements ProductService {
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

    @Override
    public ServerResponse<Product> list(String keyWord, Integer pageName, Integer pageSize, String orderBy) {
        //非空判断
        if(StringUtils.isEmpty(keyWord)){
            return ServerResponse.defeatedRS(Const.ProductEnum.ERROR_PAMAR.getCode(),
                    Const.ProductEnum.ERROR_PAMAR.getDesc());
        }
        //排序参数处理
        String[] split = new String[2];
        if(!StringUtils.isEmpty(orderBy)){
             split = orderBy.split("_");
        }

        //模糊查询
        String word = "%"+keyWord+"%";
        //开启分页
        PageHelper.startPage(pageName,pageSize,split[0]+" "+split[1]);
        List<Product> li =  productMapper.selectByName(word);
        PageInfo pageInfo = new PageInfo(li);

       //封装Vo
        List<ProductVO> liNew = new ArrayList<ProductVO>();
        for (Product product : li) {
            ProductVO productVO = ObjectToVOUtil.ProductToVO(product);
            liNew.add(productVO);
        }

        pageInfo.setList(liNew);
        //返回成功数据
        return ServerResponse.successRS(pageInfo);
    }
}
