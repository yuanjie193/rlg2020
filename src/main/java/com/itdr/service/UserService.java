package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface UserService {
    ServerResponse login(String username, String password);

    ServerResponse register(Users u);

    ServerResponse update_Information(Users user, String email, String phone, String question, String answer);

    ServerResponse checkValid(String str, String type);

    ServerResponse forgetGetQuestion(String username);

    ServerResponse forgetCheckAnswer(String username, String question, String answer);

    ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse resetPassword(Users user, String passwordOld, String passwordNew);
}
