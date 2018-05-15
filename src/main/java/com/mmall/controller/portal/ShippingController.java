package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerReponse add(HttpServletRequest httpServletRequest, Shipping shipping){//使用springmvc对象绑定方式
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.add(user.getId(),shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerReponse del(HttpServletRequest httpServletRequest,Integer shippingId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerReponse update(HttpServletRequest httpServletRequest, Shipping shipping){//使用springmvc对象绑定方式
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.update(user.getId(),shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerReponse select(HttpServletRequest httpServletRequest,Integer shippingId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse list(HttpServletRequest httpServletRequest,
                              @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }

}
