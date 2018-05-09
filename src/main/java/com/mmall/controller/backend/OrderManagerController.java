package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/order")
public class OrderManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse<PageInfo> orderList(HttpSession session,
                                             @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                             @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerReponse<OrderVo> orderDetail(HttpSession session, Long orderNo){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerReponse<PageInfo> orderSearch(HttpSession session, Long orderNo,
                                              @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerReponse<String> orderSendGoods(HttpSession session, Long orderNo){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
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
