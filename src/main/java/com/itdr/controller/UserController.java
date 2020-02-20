package com.itdr.controller;

import com.itdr.common.ServerResponse;
import com.itdr.config.Const;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.UsersVO;
import com.itdr.service.UserService;
import com.itdr.utils.ObjectToVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("portal/user/")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("login.do")
    public ServerResponse<Users> login(String username, String password, HttpSession session){
          ServerResponse sr =  userService.login(username,password);
          //在session中保存用户数据
        if(sr.isSuccess()){
            //登录成功保存session对象
            session.setAttribute("user",sr.getData());
        }
        return sr;
    }
    /**
     * 用户注册
     * @param u
     * @return
     */
    @RequestMapping("register.do")
     public ServerResponse register(Users u){
         return  userService.register(u);
     }

    /**
     * 用户退出登录
     * @param session
     * @return
     */
    @RequestMapping("logout.do")
     public ServerResponse logout(HttpSession session){
        session.removeAttribute("user");
        return ServerResponse.successRS(Const.UserEnum.LOGOUT.getDesc());
     }
    /**
     * 获取当前用户登录详细信息
     */
    @RequestMapping("get_information.do")
    public  ServerResponse getInformation(HttpSession session){
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        return ServerResponse.successRS(user);
    }

    /**
     * 获取当前用户登录信息
     * @param session
     * @return
     */
    @RequestMapping("get_user_info.do")
    public  ServerResponse getUserInfo(HttpSession session){
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        UsersVO usersVO = ObjectToVOUtil.usersToUsersVO(user);
        return ServerResponse.successRS(usersVO);
    }

    /**
     * 当前用户更新信息,更新用户session
     * @param session
     * @param email
     * @param phone
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping("update_information.do")
    public ServerResponse updateInformation(HttpSession session,String email,String phone,String question,String answer) {
        Users user =(Users)session.getAttribute("user");
        ServerResponse s = userService.update_Information(user,email,phone,question,answer);
        if(s.isSuccess()){
            session.removeAttribute("user");
            session.setAttribute("user",s.getData());
            return ServerResponse.successRS(Const.UserEnum.SUCCESS_USERMSG.getDesc());
        }
        return s;
    }
    /**
     * 检查邮箱或用户名是否重复
     * @param str
     * @param type
     * @return
     */
    @RequestMapping("check_valid.do")
    public ServerResponse checkValid(String str,String type ){
        return  userService.checkValid(str,type);
    }

    /**
     * 忘记密码，获取密保问题
     * @param username
     * @return
     */
    @RequestMapping("forget_get_question.do")
    public ServerResponse forgetGetQuestion(String username ){
        return  userService.forgetGetQuestion(username);
    }

    /**
     * 提交问题答案
     * @param username
     * @return
     */
    @RequestMapping("forget_check_answer.do")
    public ServerResponse forgetCheckAnswer(String username,String question,String answer){
        return  userService.forgetCheckAnswer(username,question,answer);
    }

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @param session
     * @return
     */
    @RequestMapping("forget_reset_password.do")
    public ServerResponse forgetResetPassword(String username,String passwordNew,String forgetToken,HttpSession session){
        ServerResponse serverResponse = userService.forgetResetPassword(username, passwordNew, forgetToken);
        if(serverResponse.isSuccess()){
            session.removeAttribute("user");
        }
        return serverResponse;
    }

    /**
     * 登录状态下更新密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping("reset_password.do")
    public  ServerResponse resetPassword(String passwordOld,String passwordNew,HttpSession session){
        Users user =(Users)session.getAttribute("user");
        if(user == null){
            return ServerResponse.defeatedRS(Const.UserEnum.NO_LOGIN.getCode(),
                    Const.UserEnum.NO_LOGIN.getDesc());
        }
        ServerResponse serverResponse = userService.resetPassword(user, passwordOld, passwordNew);
        //修改成功后删除session重新登录
        if(serverResponse.isSuccess()){
            session.removeAttribute("user");
        }
        return serverResponse;
    }

}
