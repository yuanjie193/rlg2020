package com.itdr.utils;

import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.itdr.config.pay.BizContent;
import com.itdr.config.pay.Configs;
import com.itdr.config.pay.PGoodsDetail;
import com.itdr.pojo.*;
import com.itdr.pojo.vo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObjectToVOUtil {
    /**
     * 用户类封装
     * @param u
     * @return
     */
    public static UsersVO usersToUsersVO(Users u){
        UsersVO uv = new UsersVO();
        uv.setId(u.getId());
        uv.setUsername(u.getUsername());
        uv.setEmail(u.getEmail());
        uv.setPhone(u.getPhone());
        uv.setCreateTime(u.getCreateTime());
        uv.setUpdateTime(u.getUpdateTime());
        return uv;
    }

    /**
     * 商品类封装
     * @param product
     * @return
     */
    public static ProductVO productToVO(Product product){
        ProductVO pv = new ProductVO();
        pv.setBanner(product.getBanner());
        pv.setCategoryId(product.getCategoryId());
        pv.setCreateTime(product.getCreateTime());
        pv.setDetail(product.getDetail());
        pv.setHot(product.getHot());
        pv.setMainImage(product.getMainImage());
        pv.setId(product.getId());
        pv.setName(product.getName());
        pv.setNew(product.getNew());
        pv.setUpdateTime(product.getUpdateTime());
        pv.setSubtitle(product.getSubtitle());
        pv.setStock(product.getStock());
        pv.setStatus(product.getStatus());
        pv.setSubImages(product.getSubImages());
        pv.setPrice(product.getPrice());
        pv.setImageHost(PropertiesUtil.getValue("ImageHost"));
        return pv;
    }

    /**
     * 封装购物车和商品数据
     * @param c
     * @param p
     * @return
     */
    public static CartProductVO cartAndProductToVO(Cart c,Product p ){
        CartProductVO cv = new CartProductVO();
        cv.setId(c.getId());
        cv.setUserId(c.getUserId());
        cv.setProductId(c.getProductId());
        cv.setQuantity(c.getQuantity());
        cv.setProductName(p.getName());
        cv.setProductSubtitle(p.getSubtitle());
        cv.setProductStock(p.getStock());//库存
        cv.setProductMainImage(p.getMainImage());//商品图片
        cv.setProductStatus(p.getStatus());//商品状态
        cv.setProductPrice(p.getPrice());
        //商品是否选中
        cv.setProductChecked(c.getChecked());
        //一条购物信息总价
        cv.setProductTotalPrice(BigDecimalUtil.mul(c.getQuantity(),p.getPrice().doubleValue()));
        //商品是否超过库存，默认未超过，如果超过返回失败
        String limitQuantity ="LIMIT_NUM_SUCCESS";
        if(c.getQuantity()>p.getStock()){
          limitQuantity = "LIMIT_NUM_FAILURE";
        }
        cv.setLimitQuantity(limitQuantity);
        return cv;
    }
    public static CartVO toCartVO(List<CartProductVO> list, Boolean bol, BigDecimal b){
       CartVO cv = new CartVO();
       cv.setCartProductVOList(list);
       //是否全选
        cv.setAllCheck(bol);
        //商品总价格
        cv.setCartTotalPrice(b);
       return cv;
    }
    /**
     * 获取一个BizContent对象
     * @param order
     * @param orderItems
     * @return
     */
    public static BizContent getBizContent(Order order, List<OrderItem> orderItems){
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "睿乐GO在线平台"+order.getPayment();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getPayment());

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+orderItems.size()+"件共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "001";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem orderItem : orderItems) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = getNewPay(orderItem);
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        BizContent biz = new BizContent();
        biz.setSubject(subject);
        biz.setTotal_amount(totalAmount);
        biz.setOut_trade_no(outTradeNo);
        biz.setUndiscountable_amount(undiscountableAmount);
        biz.setSeller_id(sellerId);
        biz.setBody(body);
        biz.setOperator_id(operatorId);
        biz.setStore_id(storeId);
        biz.setExtend_params(extendParams);
        biz.setTimeout_express(timeoutExpress);
        //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        biz.setNotify_url(Configs.getNotifyUrl_test());
        biz.setGoods_detail(goodsDetailList);

        return biz;
    }
    /**
     * 商品详情和支付宝商品类转换
     * @param orderItem
     * @return
     */
    public static PGoodsDetail getNewPay(OrderItem orderItem){
        PGoodsDetail info = new PGoodsDetail();
        info.setGoods_id(orderItem.getProductId().toString());
        info.setGoods_name(orderItem.getProductName());
        info.setPrice(orderItem.getCurrentUnitPrice().toString());
        info.setQuantity(orderItem.getQuantity().longValue());
        return info;
    }

    /**
     * 封装商品详情
     * @param orderItem
     * @return
     */
    public static OrderItemVO orderItemToOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        return orderItemVO;
    }

    /**
     * 封装地址详情
     * @param shopping
     * @return
     */
    public static ShoppingVO shippingToShippingVO(Shopping shopping){
        ShoppingVO shoppingVO = new ShoppingVO();
        shoppingVO.setReceiverName(shopping.getReceiverName());
        shoppingVO.setReceiverPhone(shopping.getReceiverPhone());
        shoppingVO.setReceiverMobile(shopping.getReceiverMobile());
        shoppingVO.setReceiverCity(shopping.getReceiverCity());
        shoppingVO.setReceiverDistrict(shopping.getReceiverDistrict());
        shoppingVO.setReceiverProvince(shopping.getReceiverProvince());
        shoppingVO.setReceiverAddress(shopping.getReceiverAddress());
        shoppingVO.setReceiverZip(shopping.getReceiverZip());
        return shoppingVO;
    }
    public static OrderMsgVO getOrderMsgVO(List<OrderItemVO> orderItemVOList,BigDecimal productTotalPrice){
        OrderMsgVO orderMsgVO = new OrderMsgVO();
        orderMsgVO.setOrderItemVOList(orderItemVOList);
        orderMsgVO.setImageHost(PropertiesUtil.getValue("ImageHost"));
        orderMsgVO.setProductTotalPrice(productTotalPrice);
        return orderMsgVO;
    }
}
