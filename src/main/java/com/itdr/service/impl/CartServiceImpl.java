package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.mapper.CartMapper;
import com.itdr.mapper.ProductMapper;
import com.itdr.pojo.Cart;
import com.itdr.pojo.Product;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.CartProductVO;
import com.itdr.pojo.vo.CartVO;
import com.itdr.service.CartService;
import com.itdr.utils.BigDecimalUtil;
import com.itdr.utils.ObjectToVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    //获得cartVO
    protected   CartVO getCartVO(List<Cart> list){
        //获取购物车中对应的商品
        List<CartProductVO> products = new ArrayList<CartProductVO>();
        boolean bol = true;
        BigDecimal bigDecimal=new BigDecimal(0);
        for (Cart cart : list) {
            //根据购物车中的商品ID去商品表中查询商品详细信息
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            //第一次封装
            if (product != null) {
                CartProductVO cartProductVO = ObjectToVOUtil.cartAndProductToVO(cart, product);
                products.add(cartProductVO);
                //计算商品总价 如果商品没有选中，不参与计算
                if(cart.getChecked()== 1 ){
                    bigDecimal= BigDecimalUtil.add(bigDecimal.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
            }
            //判断购物车是否全选
            if (cart.getChecked() == 0) {
                bol = false;
            }
        }
        CartVO cartVO = ObjectToVOUtil.toCartVO(products,bol,bigDecimal);
        return cartVO;
    }
    //获得用户购物车列表
    private ServerResponse getCarList(Users users){
        //查询登录用户的购物车信息
        List<Cart> list = cartMapper.selectByUsersID(users.getId());
        //用户购物车中是否有数据
        if (list.size() == 0) {
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.EMPTY_CART.getCode(),
                    Const.CartCheckedEnum.EMPTY_CART.getDesc());
        }
        return ServerResponse.successRS(list);
    }

    /**
     * 查询购物车商品信息
     * @param users
     * @return
     */
    @Override
    public ServerResponse list(Users users) {
        ServerResponse<List<Cart>> carList = getCarList(users);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
        }

    /**
     * 增加购物车商品
     * @param productID
     * @param count
     * @param user
     * @return
     */
    @Override
    public ServerResponse add(Integer productID, Integer count,Integer type, Users user) {
        //参数合法判断
        if(productID == null || productID<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(count == null || count<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        //像购物车中加入数据
        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setProductId(productID);
        cart.setQuantity(count);
        Product product = productMapper.selectByPrimaryKey(productID);
        // 商品是否下架
        if(product == null || product.getStatus() != 1){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.UNEXIST_P.getCode(),
                    Const.CartCheckedEnum.UNEXIST_P.getDesc());
        }
        ///添加商品库存是否超出
        if(count >product.getStock()){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.BEYOND_STOCK.getCode(),
                    Const.CartCheckedEnum.BEYOND_STOCK.getDesc());
        }
        //购物车添加商品或更新商品数据
        //查询购物车是否有此商品
        Cart c = cartMapper.selectByUsersIDAndProductID(user.getId(),productID);
        if(c == null){
            int insert = cartMapper.insert(cart);
            if(insert<= 0){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.ADD_PRODUCT_FAIL.getDesc());
            }
        }else {
            //根据类型来决定要执行得行为,如果等于0是增加，1是减少，2直接更新进行直接加减
            if(type == Const.CartCheckedEnum.CART_TYPE.getCode()){
                if(count + c.getQuantity() > product.getStock()){
                    return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.ADD_PRODUCT_FAIL.getDesc());
                }
                c.setQuantity(count+c.getQuantity());
            }else if(type == 1){
                if(c.getQuantity()-count<=0){
                    return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.REDUCE_PRODUCT_FAIL.getDesc());
                }
                c.setQuantity(c.getQuantity()-count);
            }else if(type ==2){
                c.setQuantity(count);
            }
            int i = cartMapper.updateByPrimaryKey(c);
            if(i<=0){
                //更新失败
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
            }
        }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
    }

    /**
     * 删除购物车商品,对应ID商品或者全选商品
     * @param productID
     * @param user
     * @return
     */
    @Override
    public ServerResponse deleteProduct(Integer productID, Users user) {
        //如果传进来ID值即删除对应ID商品，未传值就删除所有选中商品
        //参数合法性判断
        if(productID != null && productID <=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(productID != null){
            Cart cart = cartMapper.selectByUsersIDAndProductID(user.getId(), productID);
            if(cart == null){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.PRODUCT_NULL.getDesc());
            }
        }else {
            List<Cart> carts = cartMapper.selectByUsersID(user.getId());
            if(carts.size() == 0 ){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.PRODUCT_NULL.getDesc());
            }
        }
        //删除商品数据
        //根据商品ID和对应的商品数目删除购物车商品
        int i= 0;
        if(productID != null){
           i = cartMapper.deleteByPrimaryKey(productID,user.getId());
        }else {
            i =  cartMapper.deleteByChecked(user.getId());
        }
        if(i<=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.DELETE_FAYL.getDesc());
        }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
    }
    /**
     * 直接更新商品数量
     * @param productID
     * @param count
     * @param user
     * @return
     */
    /*@Override
    public ServerResponse updateProduct(Integer productID, Integer count, Users user) {
        //参数合法判断
        if(productID == null || productID<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(count == null || count<=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        Product product = productMapper.selectByPrimaryKey(productID);
        // 商品是否下架
        if(product == null || product.getStatus() != 1){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.UNEXIST_P.getCode(),
                    Const.CartCheckedEnum.UNEXIST_P.getDesc());
        }
        ///添加商品库存是否超出
        if(count >product.getStock()){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.BEYOND_STOCK.getCode(),
                    Const.CartCheckedEnum.BEYOND_STOCK.getDesc());
        }
        //查询用户购物车有没有此商品
        Cart cart = cartMapper.selectByUsersIDAndProductID(user.getId(), productID);
        if(cart == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.PRODUCT_NULL.getDesc());
        }
        //更新商品数目
        cart.setQuantity(count);
        int i = cartMapper.updateByPrimaryKey(cart);
        if(i<=0){
            //更新失败
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
        }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
    }*/
    @Override
    public ServerResponse updateProduct(Integer productID, Integer count,Integer type, Users user) {
        //参数合法判断
        if(productID == null || productID<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        if(count == null ){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        Product product = productMapper.selectByPrimaryKey(productID);
        // 商品是否下架
        if(product == null || product.getStatus() != 1){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.UNEXIST_P.getCode(),
                    Const.CartCheckedEnum.UNEXIST_P.getDesc());
        }
        //查询用户购物车有没有此商品
        Cart cart = cartMapper.selectByUsersIDAndProductID(user.getId(), productID);
        if(cart == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.PRODUCT_NULL.getDesc());
        }
        /*//添加传入的值
        count=count+cart.getQuantity();
        //更新商品数目
        cart.setQuantity(count);*/

        //根据类型来决定要执行得行为,如果等于0，进行直接加减
        //根据类型来决定要执行得行为,如果等于0是增加，1是减少，2直接更新进行直接加减
        if(type == Const.CartCheckedEnum.CART_TYPE.getCode()){
            if(count + cart.getQuantity() > product.getStock()){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.ADD_PRODUCT_FAIL.getDesc());
            }
            cart.setQuantity(count+cart.getQuantity());
        }else if(type == 1){
            if(cart.getQuantity()-count<=0){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.REDUCE_PRODUCT_FAIL.getDesc());
            }
            cart.setQuantity(cart.getQuantity()-count);
        }else if(type ==2){
            if(count>product.getStock()){
                return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.BEYOND_STOCK.getDesc());
            }
            cart.setQuantity(count);
        }
        int i = cartMapper.updateByPrimaryKey(cart);
        if(i<=0){
            //更新失败
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
        }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
    }

    /**
     * 获取购物车中选中得商品总数
     * @param user
     * @return
     */
    @Override
    public ServerResponse getCartProductCount(Users user) {
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        List<Cart> carts= carList.getData();
        Integer count = 0;
        for (Cart cart : carts) {
            if(cart.getChecked() == 1){
                count = count +cart.getQuantity();
            }
        }
        return ServerResponse.successRS(count);
    }

    /**
     * 获取购物车中全部商品总数
     * @param user
     * @return
     */
    @Override
    public ServerResponse getCartProductCountAll(Users user) {
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        List<Cart> carts= carList.getData();
        Integer count = 0;
        for (Cart cart : carts) {
                count = count +cart.getQuantity();
        }
        return ServerResponse.successRS(count);
    }



    /**
     * 选中/取消选中某个商品
     * @param user
     * @return
     */
    @Override
    public ServerResponse select(Integer productID,Users user) {
        //参数合法判断
        if(productID == null || productID<0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UNLAWFULNESS_PARAM);
        }
        //查询用户购物车有没有此商品
        Cart cart = cartMapper.selectByUsersIDAndProductID(user.getId(), productID);
        if(cart == null){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.PRODUCT_NULL.getDesc());
        }
        //更新商品状态为选中
        int i =0;
        if( i == 0 && cart.getChecked() == 0){
            cart.setChecked(1);
            i =  cartMapper.updateCheckByProductID(cart);
        }
        if(i == 0 && cart.getChecked() == 1){
            cart.setChecked(0);
            i =  cartMapper.updateCheckByProductID(cart);
        }
        if(i<=0){
            //更新失败
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
        }

        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList.getData());
        return ServerResponse.successRS(cartVO);
    }

    /**
     * 商品全选
     * @param user
     * @return
     */
    @Override
    public ServerResponse selectAll(Users user) {
        //获取购物车中商品数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        List<Cart> carts= carList.getData();
         for (Cart cart : carts) {
                if( cart.getChecked() == 0){
                    cart.setChecked(1);
                    int i =  cartMapper.updateCheckByProductID(cart);
                    if(i<=0){
                        //更新失败
                        return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
                    }
                }
         }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList2 = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList2.getData());
        return ServerResponse.successRS(cartVO);
    }

    @Override
    public ServerResponse unSelectAll(Users user) {
        //获取购物车中商品数据
        ServerResponse<List<Cart>> carList = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        List<Cart> carts= carList.getData();
        for (Cart cart : carts) {
            if( cart.getChecked() == 1){
                cart.setChecked(0);
                int i =  cartMapper.updateCheckByProductID(cart);
                if(i<=0){
                    //更新失败
                    return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
                }
            }
        }
        //获取购物车中商品数据
        //返回封装好的CartVO数据
        ServerResponse<List<Cart>> carList2 = getCarList(user);
        if(!carList.isSuccess()){
            return carList;
        }
        CartVO cartVO = getCartVO(carList2.getData());
        return ServerResponse.successRS(cartVO);
    }
    @Override
    public ServerResponse over(Users user) {
        //判断当前用户购物车中是否有数据
        List<Cart> carts = cartMapper.selectByUsersID(user.getId());
        if(carts.isEmpty()){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.EMPTY_CART.getCode(),
                    Const.CartCheckedEnum.EMPTY_CART.getDesc());
        }
        //判断购物车是否有选中数据
        boolean bol = false;
        for (Cart cart : carts) {
            if(cart.getChecked() == 1){
                bol=true;
            }
        }
        if(!bol){
            return ServerResponse.defeatedRS(Const.CartCheckedEnum.NO_SELECT_PRODUCT.getCode(),
                    Const.CartCheckedEnum.NO_SELECT_PRODUCT.getDesc());
        }
        return ServerResponse.successRS(true);
    }

}
