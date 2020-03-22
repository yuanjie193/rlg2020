package com.itdr.mapper;

import com.itdr.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKey(Product record);

    Product selectByPrimaryKey(Integer productId);

    List<Product> selectByName(String word);

    List<Product> selectByType(@Param("type") String type);
}