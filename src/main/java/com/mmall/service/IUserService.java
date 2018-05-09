package com.mmall.service;

import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;

public interface IUserService {
    ServerReponse<User> login(String username, String password);
    ServerReponse<String> register(User user);
    ServerReponse<String> checkValid(String str,String type);
    ServerReponse selectQuestion(String username);
    ServerReponse<String> checkAnswer(String username,String question,String answer);
    ServerReponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);
    ServerReponse<String> resetPassword(String passwordOld,String passwordNew,User user);
    ServerReponse<User> update_information(User user);
    ServerReponse<User> getInformation(Integer userId);
    ServerReponse checkAdminRole(User user);
}
