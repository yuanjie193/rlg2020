package com.itdr.service.impl;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.config.TokenCache;
import com.itdr.mapper.UsersMapper;
import com.itdr.pojo.Users;
import com.itdr.service.UserService;
import com.itdr.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse login(String username, String password) {
//        参数非空判断
        if(StringUtils.isEmpty(username)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_USERNAME.getCode(),
                    Const.UserEnum.EMPTY_USERNAME.getDesc());
        }
        if(StringUtils.isEmpty(password)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PASSWORD.getCode(),
                    Const.UserEnum.EMPTY_PASSWORD.getDesc());
        }
        //MD5加密
        String MD5password = MD5Util.getMD5Code(password);
        //查询用户
        Users u = usersMapper.selectByUsernameAndPassword(username,MD5password);
        if(u == null){
            //返回用户登录失败信息
            return ServerResponse.defeatedRS(Const.UserEnum.FAIL_LOGIN.getCode(),
                    Const.UserEnum.FAIL_LOGIN.getDesc());
        }
        //成功返回用户数据
        return ServerResponse.successRS(u);
    }
    /**
     * 用户注册
     * @param u
     * @return
     */
    @Override
    public ServerResponse register(Users u) {
        //参数非空判断
        if(StringUtils.isEmpty(u.getUsername())){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_USERNAME.getCode(),
                    Const.UserEnum.EMPTY_USERNAME.getDesc());
        }
        if(StringUtils.isEmpty(u.getPassword())){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PASSWORD.getCode(),
                    Const.UserEnum.EMPTY_PASSWORD.getDesc());
        }
        if(StringUtils.isEmpty(u.getQuestion())){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_QUESTION.getCode(),
                    Const.UserEnum.EMPTY_QUESTION.getDesc());
        }
        if(StringUtils.isEmpty(u.getAnswer())){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_ANSWER.getCode(),
                    Const.UserEnum.EMPTY_ANSWER.getDesc());
        }
        //判断用户是否存在
        int username = usersMapper.selectByUsernameOrEmail(u.getUsername(), "username");
        //判断邮箱是否存在
        int email = usersMapper.selectByUsernameOrEmail(u.getEmail(), "email");
        if(username>0 || email >0){
            return ServerResponse.defeatedRS(Const.UserEnum.EXIT_USERNAME_OR_EMAIL.getCode(),
                    Const.UserEnum.EXIT_USERNAME_OR_EMAIL.getDesc());
        }
        //MD5加密
        u.setPassword(MD5Util.getMD5Code(u.getPassword()));
        //注册用户信息
        int insert = usersMapper.insert(u);
        if(insert <=0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UserEnum.FAIL_REGISTER.getDesc());
        }
        return ServerResponse.successRS(Const.UserEnum.SUCCESS_USER.getDesc());
    }

//   当前用户更新信息
    @Override
    public ServerResponse update_Information(Users user, String email, String phone, String question, String answer) {
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        Users u = new Users();
        u.setEmail(email);
        u.setPhone(phone);
        u.setQuestion(question);
        u.setAnswer(answer);
        u.setId(user.getId());
        int i =usersMapper.updateByPrimaryKeySelective(u);
        if(i<=0){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PARAM2.getCode(),
                    Const.UserEnum.EMPTY_PARAM2.getDesc());
        }
        //更新成功后更新用户session，获取用户的新信息
        Users users = usersMapper.selectByUsername(user.getUsername());
        return ServerResponse.successRS(users);
    }

    /**
     * 判断注册信息是否有效
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse checkValid(String str, String type) {
        //        参数非空判断,注册信息不能为空
        if(StringUtils.isEmpty(str)){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,
                    Const.UserEnum.EMPTY_PARAM.getDesc());
        }
        //        参数非空判断,类型不能为空
        if(StringUtils.isEmpty(type)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_TYPE.getCode(),
                    Const.UserEnum.EMPTY_TYPE.getDesc());
        }
        //查找用户名或邮箱是否存在
        int i = usersMapper.selectByUsernameOrEmail(str,type);
        if(i>0){
            return ServerResponse.defeatedRS(Const.DEFAULT_FAIL,Const.UserEnum.EXIT_USERNAME_OR_EMAIL.getDesc());
        }
        return ServerResponse.successRS(Const.UserEnum.SUCCESS_MSG.getDesc());
    }

    /**
     * 获取密保问题
     * @param username
     * @return
     */
    @Override
    public ServerResponse forgetGetQuestion(String username) {
        //参数非空判断
        if(StringUtils.isEmpty(username)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_USERNAME.getCode(),
                    Const.UserEnum.EMPTY_USERNAME.getDesc());
        }
        Users u  = usersMapper.selectByUsername(username);
        if(u == null){
            return ServerResponse.defeatedRS(Const.UserEnum.INEXISTENCE_USER.getCode(),
                    Const.UserEnum.INEXISTENCE_USER.getDesc());
        }
        String question = u.getQuestion();
        if(StringUtils.isEmpty(question)){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_QUESTION.getCode(),
                    Const.UserEnum.NO_QUESTION.getDesc());
        }
        return ServerResponse.successRS(Const.DEFAULT_SUCCESS,question);
    }

    /**
     * 验证用户答案是否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */

    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {
        //参数非空判断
        if(StringUtils.isEmpty(username)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_USERNAME.getCode(),
                    Const.UserEnum.EMPTY_USERNAME.getDesc());
        }
        if(StringUtils.isEmpty(question)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_QUESTION.getCode(),
                    Const.UserEnum.EMPTY_QUESTION.getDesc());
        }
        if(StringUtils.isEmpty(answer)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_ANSWER.getCode(),
                    Const.UserEnum.EMPTY_ANSWER.getDesc());
        }
        //判断答案是否正确
        int i = usersMapper.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        if(i<=0){
            return ServerResponse.defeatedRS(Const.UserEnum.ERROR_ANSWER.getCode(),
                    Const.UserEnum.ERROR_ANSWER.getDesc() );
        }
        //返回随机令牌，使用工具类UUID产生随机字符串
        String forgetToken = UUID.randomUUID().toString();
        //把令牌放入缓存中，这里使用的是Google的guava缓存，后期会使用redis替代
        TokenCache.set("token_" + username, forgetToken);
        return ServerResponse.successRS(Const.DEFAULT_SUCCESS,forgetToken);
    }

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */

    @Override
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //参数非空判断
        if(StringUtils.isEmpty(username)){
            System.out.println(StringUtils.isEmpty(username));
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_USERNAME.getCode(),
                    Const.UserEnum.EMPTY_USERNAME.getDesc());
        }
        //参数非空判断
        if(StringUtils.isEmpty(passwordNew)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PASSWORD.getCode(),
                    Const.UserEnum.EMPTY_PASSWORD.getDesc());
        }
        //参数非空判断
        if(StringUtils.isEmpty(forgetToken)){
            return ServerResponse.defeatedRS(Const.UserEnum.UNLAWFULNESS_TOKEN.getCode(),
                    Const.UserEnum.UNLAWFULNESS_TOKEN.getDesc());
        }
        //判断token是否正确
        String s = TokenCache.get("token_" + username);
        if(s==null || s.equals("")){
            return ServerResponse.defeatedRS(Const.UserEnum.LOSE_EFFICACY.getCode(),
                    Const.UserEnum.LOSE_EFFICACY.getDesc());
        }
        if(!s.equals(forgetToken)){
            return ServerResponse.defeatedRS(Const.UserEnum.UNLAWFULNESS_TOKEN.getCode(),
                    Const.UserEnum.UNLAWFULNESS_TOKEN.getDesc());
        }
        //MD5加密
        String MD5password = MD5Util.getMD5Code(passwordNew);
        //重置密码
        int i =  usersMapper.updateByUsernameAndPasswordNew(username,MD5password);
        if(i<=0){
            return ServerResponse.defeatedRS(Const.UserEnum.DEFEACTED_PASSWORDNEW.getCode(),
                    Const.UserEnum.DEFEACTED_PASSWORDNEW.getDesc());
        }
        return ServerResponse.successRS(Const.UserEnum.SUCCESS_PASSWORDNEW.getDesc());
    }

    /**
     * 登录状态下的重置密码
     * @param user
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Override
    public ServerResponse resetPassword(Users user, String passwordOld, String passwordNew) {
        //参数非空判断
        if(StringUtils.isEmpty(passwordOld)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PASSWORD.getCode(),
                    Const.UserEnum.EMPTY_PASSWORD.getDesc());
        }
        //参数非空判断
        if(StringUtils.isEmpty(passwordNew)){
            return ServerResponse.defeatedRS(Const.UserEnum.EMPTY_PASSWORD.getCode(),
                    Const.UserEnum.EMPTY_PASSWORD.getDesc());
        }
        //MD5加密
        String md5passwordOld = MD5Util.getMD5Code(passwordOld);
        String md5passwordNew = MD5Util.getMD5Code(passwordNew);
        //更新密码
        int i = usersMapper.updateByUsernameAndPasswordOldAndPasswordNew(user.getUsername(),md5passwordOld,md5passwordNew);
        if(i<=0){
            return ServerResponse.defeatedRS(Const.UserEnum.DEFEACTED_PASSWORDNEW.getCode(),
                    Const.UserEnum.DEFEACTED_PASSWORDNEW.getDesc());
        }
        return ServerResponse.successRS(Const.UserEnum.SUCCESS_PASSWORDNEW.getDesc());
    }
}
