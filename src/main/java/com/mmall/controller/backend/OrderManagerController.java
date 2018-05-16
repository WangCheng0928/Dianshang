package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/manage/order")
public class OrderManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse<PageInfo> orderList(HttpServletRequest httpServletRequest,
                                             @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                             @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员具备操作权限
            return iOrderService.manageOrderList(pageNum,pageSize);
        }else {
            return ServerReponse.createBySuccessMsg("不是管理员,无权限操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerReponse<OrderVo> orderDetail(HttpServletRequest httpServletRequest, Long orderNo){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员具备操作权限
            return iOrderService.manageDetail(orderNo);
        }else {
            return ServerReponse.createBySuccessMsg("不是管理员,无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerReponse<PageInfo> orderSearch(HttpServletRequest httpServletRequest, Long orderNo,
                                              @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员具备操作权限
            return iOrderService.manageSerachOrder(orderNo,pageNum,pageSize);
        }else {
            return ServerReponse.createBySuccessMsg("不是管理员,无权限操作");
        }
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerReponse<String> orderSendGoods(HttpServletRequest httpServletRequest, Long orderNo){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员具备操作权限
            return iOrderService.manageSendGoods(orderNo);
        }else {
            return ServerReponse.createBySuccessMsg("不是管理员,无权限操作");
        }
    }

}
