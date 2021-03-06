package com.itdr.config;

public class Const {

        public static final int DEFAULT_SUCCESS=200;
        public static final int DEFAULT_FAIL=100;
        public static final String UNLAWFULNESS_PARAM ="非法的参数";


        //用户枚举类
        public enum UserEnum {
            //状态信息
            ERROR_PASSWORD(1, "密码错误"),
            EMPTY_USERNAME(2, "用户名不能为空"),
            EMPTY_PASSWORD(3, "密码不能为空"),
            EMPTY_QUESTION(4, "问题不能为空"),
            EMPTY_ANSWER(5, "答案不能为空"),
            INEXISTENCE_USER(6, "用户名不存在"),
            EXIST_USER(7, "用户名已存在"),
            EXIST_EMAIL(8, "邮箱已注册"),
            EMPTY_PARAM(9, "注册信息不能为空"),
            EMPTY_PARAM2(10, "更新信息不能为空"),
            SUCCESS_USER(11, "用户注册成功"),
            SUCCESS_MSG(12, "校验成功"),
            NO_LOGIN(13, "用户未登录"),
            NO_QUESTION(14, "该用户未设置找回密码问题"),
            ERROR_ANSWER(15,"问题答案错误"),
            LOSE_EFFICACY(16, "token已经失效"),
            UNLAWFULNESS_TOKEN(17, "非法的token"),
            DEFEACTED_PASSWORDNEW(18, "修改密码操作失败"),
            SUCCESS_PASSWORDNEW(19, "修改密码操作成功"),
            ERROR_PASSWORDOLD(20, "旧密码输入错误"),
            SUCCESS_USERMSG(21, "更新个人信息成功"),
            FORCE_EXIT(22, "用户未登录，无法获取当前用户信息"),
            LOGOUT(23, "退出成功"),
            FAIL_LOGIN(24,"登录失败"),
            FAIL_REGISTER(25,"用户注册失败"),
            EMPTY_TYPE(26,"类型不能为空"),
            EXIT_USERNAME_OR_EMAIL(27,"用户名或邮箱已存在");

            private int code;
            private String desc;

            private UserEnum(int code, String desc) {
                this.code = code;
                this.desc = desc;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    //商品枚举类
    public enum ProductEnum {

        PRODUCT_ONLINE(1, "在售"),
        PRODUCT_OFFLINE(2, "下架"),
        PRODUCT_DELETE(3, "删除"),
        ERROR_PAMAR(4, "参数错误"),
        NO_PRODUCT(5, "该商品已下架"),
        NULL_PRODUCI(6,"暂无商品数据");
        private int code;
        private String desc;

        private ProductEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //购物车枚举类
    public enum CartCheckedEnum {
        EMPTY_PARAM(100, "参数不能为空"),
        BZ_XQ(101, "商品详情页更新数量"),
        BZ_GWC(102, "购物车页面更新数量"),
        PRODUCT_CHECKED(1, "已勾选"),
        PRODUCT_UNCHECKED(0, "未勾选"),
        NO_SESSION(Const.DEFAULT_FAIL, "用户未登录,请登录"),
        EMPTY_CART(Const.DEFAULT_FAIL, "购物车暂时还没有商品喔~"),
        FALSE_UPDATE(Const.DEFAULT_FAIL, "更新数据失败"),
        UNEXIST_P(Const.DEFAULT_FAIL, "商品不存在"),
        ADD_PRODUCT_FAIL(103,"添加商品失败"),
        REDUCE_PRODUCT_FAIL(104,"减少商品失败"),
        BEYOND_STOCK(104,"商品库存不足"),
        PRODUCT_NULL(105,"暂无此商品"),
        DELETE_FAYL(106,"删除失败"),
        CART_TYPE(0,"增加"),
        NO_SELECT_PRODUCT(107,"没有选中得商品");

        private int code;
        private String desc;

        private CartCheckedEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //收货地址枚举类
    public enum ShoppingEnum {
        EMPTY_PARAM(100, "参数不能为空"),
        FAIL_ADD(101,"地址添加失败"),
        EMPTY_RECEIVER_NAME(102,"收货人姓名不能为空！"),
        EMPTY_RECEIVER_PHONE(103,"收货人电话不能为空"),
        EMPTY_INFORMATION(104,"信息不能为空"),
        NO_ADDRESS(105,"暂未添加收货地址"),
        NOT_SHOP_ADDRESS(106,"更新失败"),
        NO_DEFAULT_ADDRESS(107,"暂无默认收货地址");

        private int code;
        private String desc;

        private ShoppingEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum aliPayEnum {
        ORDER_CANCELED(0, "已取消"),
        ORDER_UN_PAY(10, "未付款"),
        ORDER_PAYED(20, "已付款"),
        ORDER_SEND(40, "已发货"),
        ORDER_SUCCESS(50, "交易成功"),
        ORDER_CLOSED(60, "交易关闭"),
        ORDER_UN_EXIT(70,"订单不存在"),
        ORDER_UN_MATCH(70,"订单不匹配"),
        FAILED_ORDER_USER(3,"订单与用户不匹配"),
        FAIDED_ORDER(4,"下单失败"),
        VERIFY_SIGNATURE_FALSE(5,"验签失败");
        private int code;
        private String desc;

        private aliPayEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        }

    public enum OrderStatusEnum {
        ORDER_CANCELED(0, "已取消"),
        ORDER_UN_PAY(10, "未付款"),
        ORDER_PAYED(20, "已付款"),
        ORDER_SEND(40, "已发货"),
        ORDER_SUCCESS(50, "交易成功"),
        ORDER_CLOSED(60, "交易关闭"),
        FALSE_CREAT(70, "创建订单失败"),
        FALSE_CREAT_ORDER_ITEM(80,"创建订单详情失败"),
        NULL_ORDER_ITEM(90,"暂无商品信息"),
        NULL_ORDER(91,"订单不存在"),
        ERROR_CHANG(92,"操作有误");
        private int code;
        private String desc;

        private OrderStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    }
