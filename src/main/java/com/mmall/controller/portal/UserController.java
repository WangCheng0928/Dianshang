package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping(value = "login.do",method = RequestMethod.GET)
    @ResponseBody //作用：在返回时自动通过springmvc的json插件将返回值序列化成json
    public ServerReponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        //service--mybatis--dao
        ServerReponse<User> reponse=iUserService.login(username,password);
        if(reponse.isSuccess()){
//            session.setAttribute(Const.CURRENT_USER,reponse.getData());
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(reponse.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return reponse;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<String> loginout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
        RedisShardedPoolUtil.del(loginToken);
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
    public ServerReponse<User> getUserInfo(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
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
    public ServerReponse<String> resetPassword(HttpServletRequest httpServletRequest,String passwordOld,String passwordNew){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorMessage("用户未登录");
        }
       return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<User> update_information(HttpServletRequest httpServletRequest, User user){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User currUser = JsonUtil.string2Obj(userJsonstr, User.class);
        if(currUser==null){
            return ServerReponse.createByErrorMessage("用户未登录");
        }
        user.setId(currUser.getId());
        user.setUsername(currUser.getUsername());
        ServerReponse<User> reponse=iUserService.update_information(user);
        if(reponse.isSuccess()){
            reponse.getData().setUsername(currUser.getUsername());
            RedisShardedPoolUtil.setEx(loginToken, JsonUtil.obj2String(reponse.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return reponse;
    }

    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerReponse<User> get_information(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonstr, User.class);
        if(currentUser==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录，status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }


}
