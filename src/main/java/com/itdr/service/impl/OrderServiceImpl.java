package com.itdr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.mapper.*;
import com.itdr.pojo.*;
import com.itdr.pojo.vo.*;
import com.itdr.service.CartService;
import com.itdr.service.OrderService;
import com.itdr.utils.BigDecimalUtil;
import com.itdr.utils.ObjectToVOUtil;
import com.itdr.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ShoppingMapper shoppingMapper;
    //生成订单号
    private Long getNumber(){
        long round = Math.round(Math.random() * 100);
        long l = System.currentTimeMillis()+round;
        return l;
    }
    /**
     * 封装订单信息
     * @param shippingId
     * @param o
     * @param orderItemVOList
     * @param shoppingVO
     * @return
     */
    private OrderVO getOrderVO(Integer shippingId, Order o, List<OrderItemVO> orderItemVOList, ShoppingVO shoppingVO){
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(o.getOrderNo());
        orderVO.setShippingId(shippingId);
        orderVO.setPayment(o.getPayment());
        orderVO.setPaymentType(o.getPaymentType());
        orderVO.setPostage(o.getPostage());
        orderVO.setStatus(o.getStatus());
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setShoppingVO(shoppingVO);
        orderVO.setImageHost(PropertiesUtil.getValue("ImageHost"));
        orderVO.setPaymentTime(o.getPaymentTime());
        orderVO.setSendTime(o.getSendTime());
        orderVO.setEndTime(o.getEndTime());
        orderVO.setCloseTime(o.getCloseTime());
        return orderVO;
    }
    @Override
    public ServerResponse creat(Users user, Integer shoppingID) {
        //参数判断
        if(shoppingID == null || shoppingID < 0){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.EMPTY_CART.getCode(),
                    Const.CartCheckedEnum.EMPTY_CART.getDesc());
        }
        //判断当前用户购物车中是否有数据
        ServerResponse over = cartService.over(user);
        if(!over.isSuccess()){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.NO_SELECT_PRODUCT.getCode(),
                    Const.CartCheckedEnum.NO_SELECT_PRODUCT.getDesc());
        }

        //订单编号生成规则
        //根据用户名获取购物车信息
        List<Cart> carts = cartMapper.selectByUsersID(user.getId());
        CartVO cartVO = ((CartServiceImpl) cartService).getCartVO(carts);
        //创建一个订单
        Long number = getNumber();
        Order o = new Order();
        o.setUserId(user.getId());
        o.setOrderNo(number);
        o.setShippingId(shoppingID);
        o.setPayment(cartVO.getCartTotalPrice());
        o.setPaymentType(1);
        o.setPostage(0);
        o.setStatus(10);
        //把创建得订单对象存储到数据库中
        int i = orderMapper.insert(o);
        if(i<=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.FALSE_CREAT.getDesc());
        }
        //创建订单详情对象
        List<OrderItemVO> itemVOList =new ArrayList<OrderItemVO>();
        for (Cart cart : carts) {
            OrderItem orderItem = new OrderItem();
            if(cart.getChecked() == 1){
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product.getStatus() == 1 && cart.getQuantity()<=product.getStock()) {
                    orderItem.setUserId(user.getId());
                    orderItem.setOrderNo(o.getOrderNo());
                    orderItem.setProductId(cart.getProductId());
                    orderItem.setProductName(product.getName());
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setCurrentUnitPrice(product.getPrice());
                    orderItem.setQuantity(cart.getQuantity());
                    orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue()));
                }
                //把创建的订单详情对象存储到数据库中
                int i2 = orderItemMapper.insert(orderItem);
                if(i2<=0){
                    return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.FALSE_CREAT_ORDER_ITEM.getDesc());
                }
                OrderItemVO orderItemVO = ObjectToVOUtil.orderItemToOrderItemVO(orderItem);
                itemVOList.add(orderItemVO);
            }
        }
        //清空购物车选中数据
        cartMapper.deleteByChecked(user.getId());
        //返回成功数据
        Shopping shopping = shoppingMapper.selectByShoppingID(shoppingID);
        if(shopping == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
        }
        ShoppingVO shoppingVO = ObjectToVOUtil.shippingToShippingVO(shopping);
        OrderVO orderVO = getOrderVO(shoppingID,o,itemVOList, shoppingVO);
        return ServerResponse.successRS(orderVO);
    }

    /**
     * 获取用户订单商品详情
     * @param user
     * @param orderNo
     * @return
     */

    @Override
    public ServerResponse getOrderCartProduct(Users user, Long orderNo) {
        if(orderNo != null){
            //根据订单号和用户Id查询订单商品信息
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNoAndUserID(orderNo, user.getId());
            if(orderItems.isEmpty()){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
            }
            List<OrderItemVO> orderItemVOList = new ArrayList<OrderItemVO>();
            for (OrderItem orderItem : orderItems) {
                OrderItemVO orderItemVO = ObjectToVOUtil.orderItemToOrderItemVO(orderItem);
                orderItemVOList.add(orderItemVO);
            }
            //查询支付总价
            Order order = orderMapper.selectByOrderNo(orderNo);
            OrderMsgVO orderMsgVO = ObjectToVOUtil.getOrderMsgVO(orderItemVOList,order.getPayment());
            return ServerResponse.successRS(orderMsgVO);
        }else {
            //没有订单编号时
            //判断当前用户购物车中是否有数据
            ServerResponse over = cartService.over(user);
            if(!over.isSuccess()){
                return ServerResponse.defeatedRS(Const.CartCheckedEnum.NO_SELECT_PRODUCT.getCode(),
                        Const.CartCheckedEnum.NO_SELECT_PRODUCT.getDesc());
            }
            //根据用户名获取购物车信息
            List<Cart> carts = cartMapper.selectByUsersID(user.getId());
            BigDecimal payment=new BigDecimal(0);
           /* for (Cart cart : carts) {
                if(cart.getChecked() == 1){
                    Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                    if(product.getStatus() == 1 && cart.getQuantity()<=product.getStock()) {
                      BigDecimal pay =  BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue());
                      payment=BigDecimalUtil.add(payment.doubleValue(),pay.doubleValue());
                    }
                }
            }*/
            //创建订单详情对象
            List<OrderItemVO> itemVOList =new ArrayList<OrderItemVO>();
            for (Cart cart : carts) {
                OrderItem orderItem = new OrderItem();
                if(cart.getChecked() == 1){
                    Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                    if(product.getStatus() == 1 && cart.getQuantity()<=product.getStock()) {
                        orderItem.setUserId(user.getId());
                        orderItem.setOrderNo(null);
                        orderItem.setProductId(cart.getProductId());
                        orderItem.setProductName(product.getName());
                        orderItem.setProductImage(product.getMainImage());
                        orderItem.setCurrentUnitPrice(product.getPrice());
                        orderItem.setQuantity(cart.getQuantity());
                        BigDecimal pay =  BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue());
                        orderItem.setTotalPrice(pay);
                        payment=BigDecimalUtil.add(payment.doubleValue(),pay.doubleValue());
                    }
                    OrderItemVO orderItemVO = ObjectToVOUtil.orderItemToOrderItemVO(orderItem);
                    itemVOList.add(orderItemVO);
                }
            }
            OrderMsgVO orderMsgVO = ObjectToVOUtil.getOrderMsgVO(itemVOList, payment);
            return ServerResponse.successRS(orderMsgVO);
        }
    }

    @Override
    public ServerResponse getOrderList(Users user, Integer pageSize, Integer pageNum) {
        //查询order
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserID(user.getId());
        if(orderList.isEmpty()){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
        }
        //根据订单号和用户Id查询订单商品信息
        List<OrderVO> orderVOList = new ArrayList<OrderVO>();
        for (Order order : orderList) {
            //根据订单号和用户Id查询订单商品信息
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNoAndUserID(order.getOrderNo(), user.getId());
            if(orderItems.isEmpty()){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
            }
            List<OrderItemVO> orderItemVOList = new ArrayList<OrderItemVO>();
            for (OrderItem orderItem : orderItems) {
                OrderItemVO orderItemVO = ObjectToVOUtil.orderItemToOrderItemVO(orderItem);
                orderItemVOList.add(orderItemVO);
            }
            System.out.println(order.getShippingId());
            Shopping shopping = shoppingMapper.selectByShoppingID(order.getShippingId());
            System.out.println(shopping);
            if(shopping == null){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
            }
            ShoppingVO shoppingVO = ObjectToVOUtil.shippingToShippingVO(shopping);
            OrderVO orderVO = getOrderVO(order.getShippingId(), order, orderItemVOList, shoppingVO);
            orderVOList.add(orderVO);
        }
        PageInfo pageInfo = new PageInfo(orderVOList);
        return ServerResponse.successRS(pageInfo);
    }

    @Override
    public ServerResponse getStatusNumber(Users user) {
        List<Order> orderList = orderMapper.selectByUserID(user.getId());
        if(orderList == null){
            return ServerResponse.defeatedRS(Const.OrderStatusEnum.NULL_ORDER_ITEM.getCode(),
                    Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
        }
        Integer qx = 0;
        Integer dfk = 0;
        Integer dfh = 0;
        Integer dsh = 0;
        for (Order order : orderList) {
            if(order.getStatus() == 0){
                qx=qx+1;
            }
            if(order.getStatus() == 10){
                dfk+=1;
            }
            if(order.getStatus() == 20){
                dfh = dfh+1;
            }
            if(order.getStatus() == 40){
                dsh = dsh +1;
            }
        }
        StatusVO statusVO = ObjectToVOUtil.getStatusVO(qx, dfk, dfh, dsh);
        return ServerResponse.successRS(statusVO);
    }

    @Override
    public ServerResponse getCancelOrder(Users user, Integer type) {
        if(type == null || type < 0){
            return ServerResponse.defeatedRS(Const.UNLAWFULNESS_PARAM);
        }
        List<Order> orderList = orderMapper.selectByUserIDAndStatus(user.getId(),type);
        if(orderList.isEmpty()){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
        }
        //根据订单号和用户Id查询订单商品信息
        List<OrderVO> orderVOList = new ArrayList<OrderVO>();
        for (Order order : orderList) {
            //根据订单号和用户Id查询订单商品信息
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNoAndUserID(order.getOrderNo(), user.getId());
            if(orderItems.isEmpty()){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.OrderStatusEnum.NULL_ORDER_ITEM.getDesc());
            }
            List<OrderItemVO> orderItemVOList = new ArrayList<OrderItemVO>();
            for (OrderItem orderItem : orderItems) {
                OrderItemVO orderItemVO = ObjectToVOUtil.orderItemToOrderItemVO(orderItem);
                orderItemVOList.add(orderItemVO);
            }
            System.out.println(order.getShippingId());
            Shopping shopping = shoppingMapper.selectByShoppingID(order.getShippingId());
            System.out.println(shopping);
            if(shopping == null){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.ShoppingEnum.NO_ADDRESS.getDesc());
            }
            ShoppingVO shoppingVO = ObjectToVOUtil.shippingToShippingVO(shopping);
            OrderVO orderVO = getOrderVO(order.getShippingId(), order, orderItemVOList, shoppingVO);
            orderVOList.add(orderVO);
        }
        return ServerResponse.successRS(orderVOList);
    }

    /**
     * 取消订单
     * @param user
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse toCancelOrder(Users user, Long orderNo) {
        if(orderNo == null || orderNo <0){
            return ServerResponse.defeatedRS(Const.UNLAWFULNESS_PARAM);
        }
        Order order = orderMapper.selectByOrderNoAndUserID(user.getId(), orderNo);
        if(order == null){
            return ServerResponse.defeatedRS(Const.OrderStatusEnum.NULL_ORDER.getCode(),
                    Const.OrderStatusEnum.NULL_ORDER.getDesc());
        }
        if(order.getStatus() != 10){
            return ServerResponse.defeatedRS(Const.OrderStatusEnum.ERROR_CHANG.getCode(),
                    Const.OrderStatusEnum.ERROR_CHANG.getDesc());
        }
        int i = orderMapper.updateByOrderNo(orderNo);
        return ServerResponse.successRS(Const.OrderStatusEnum.ORDER_CANCELED.getDesc());
    }
}
