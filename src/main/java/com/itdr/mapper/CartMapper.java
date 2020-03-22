package com.itdr.mapper;

import com.itdr.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(@Param("productID") Integer id,@Param("userID")Integer userID);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectByUsersID(Integer userId);

    Cart selectByUsersIDAndProductID(@Param("userID") Integer userID, @Param("productID")Integer productID);


    int updateCheckByProductID(Cart cart);

    int deleteByChecked(Integer id);

    List<Cart> selectByUsersIDAndChecked(@Param("uid") Integer uid);
}