package com.itdr.mapper;

import com.itdr.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNoAndUserID(@Param("uid") Integer uid,@Param("orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserID(Integer uid);

    List<Order> selectByUserIDAndStatus(@Param("uid") Integer uid, @Param("type") Integer type);

    int updateByOrderNo(@Param("orderNo") Long orderNo);

}