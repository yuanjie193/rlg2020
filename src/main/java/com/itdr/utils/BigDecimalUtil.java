package com.itdr.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {
    /**
     * 加法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal add(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.add(bigDecimal2);
    }
    /**
     * 减法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal sub(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.subtract(bigDecimal2);
    }
    /**
     * 乘法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal mul(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.multiply(bigDecimal2);
    }
    /**
     * 除法运算,取小数两位，四舍五入
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal div(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.divide(bigDecimal2,2,BigDecimal.ROUND_HALF_UP);
    }
}
