package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody //作用：在返回时自动通过springmvc的json插件将返回值序列化成json
    public ServerReponse<User> login(String username, String password, HttpSession session){
        //service--mybatis--dao
        ServerReponse<User> reponse=iUserService.login(username,password);
        if(reponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,reponse.getData());
        }
        return reponse;
    }

    @RequestMapping(value = "loginout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> loginout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerReponse.createBySuccess("退出成功");
    }


    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> register(User user){

        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> checkValid(String str,String type){

        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<User> getUserInfo(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerReponse.createBySuccess(user);
        }
        return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorMessage("用户未登录");
        }
       return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<User> update_information(HttpSession session,User user){
        User currUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currUser==null){
            return ServerReponse.createByErrorMessage("用户未登录");
        }
        user.setId(currUser.getId());
        user.setUsername(currUser.getUsername());
        ServerReponse<User> reponse=iUserService.update_information(user);
        if(reponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,reponse.getData());
        }
        return reponse;
    }

    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<User> get_information(HttpSession session){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录，status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }


}
