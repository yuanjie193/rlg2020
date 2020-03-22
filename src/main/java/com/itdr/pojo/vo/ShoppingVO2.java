package com.itdr.pojo.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShoppingVO2 {
//    	每条地址的唯一标识
    Integer id ;
//    	收货人姓名
    String name ;
//    	收货人手机号
    String tel ;
//    	收货地址
    String address ;
//    是否为默认地址
    Boolean isDefault;

}
