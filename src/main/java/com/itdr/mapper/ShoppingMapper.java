package com.itdr.mapper;

import com.itdr.pojo.Shopping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shopping record);

    int insertSelective(Shopping record);

    Shopping selectByPrimaryKey( @Param("id") Integer id,@Param("uid") Integer uid);

    int updateByPrimaryKeySelective(Shopping record);

    int updateByPrimaryKey(Shopping record);

    List selectByUserID(@Param("userID")Integer userID);

    int deleteByPrimaryKeyAndUserID(@Param("shoppingID") Integer shoppingID, @Param("uid")Integer uid);

    Shopping selectByShoppingID(Integer shoppingID);
}