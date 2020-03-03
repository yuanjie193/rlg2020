package com.itdr.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderMsgVO {
    private List<OrderItemVO> orderItemVOList;
    // 图片服务器地址
    private String imageHost;
    private BigDecimal productTotalPrice;
}
