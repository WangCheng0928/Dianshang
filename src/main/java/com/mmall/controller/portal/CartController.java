package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse list(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerReponse add(HttpSession session,Integer productId,Integer count){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.add(user.getId(),productId,count);
    }
    @RequestMapping("update.do")
    @ResponseBody
    public ServerReponse update(HttpSession session,Integer productId,Integer count){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerReponse deleteProduct(HttpSession session,String productIds){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }
    //全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerReponse<CartVo> selectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,null);
    }
    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerReponse<CartVo> unSelectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UN_CHECKED,null);
    }
    //单独选
    @RequestMapping("select.do")
    @ResponseBody
    public ServerReponse<CartVo> select(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,productId);
    }
    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerReponse<CartVo> unSelect(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UN_CHECKED,productId);
    }
    //查询当前用户的购物车里面的产品数量，如果一个产品有10个，就返回10
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerReponse<Integer> getCartProductCount(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());

    }

}
