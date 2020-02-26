package com.itdr.pojo.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
    private List<CartProductVO> cartProductVOList;

    private Boolean allCheck;

    private BigDecimal cartTotalPrice;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public Boolean getAllCheck() {
        return allCheck;
    }

    public void setAllCheck(Boolean allCheck) {
        this.allCheck = allCheck;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
