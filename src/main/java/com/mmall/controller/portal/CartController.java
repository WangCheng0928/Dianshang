package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse list(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerReponse add(HttpServletRequest httpServletRequest,Integer productId,Integer count){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.add(user.getId(),productId,count);
    }
    @RequestMapping("update.do")
    @ResponseBody
    public ServerReponse update(HttpServletRequest httpServletRequest,Integer productId,Integer count){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerReponse deleteProduct(HttpServletRequest httpServletRequest,String productIds){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }
    //全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerReponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,null);
    }
    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerReponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UN_CHECKED,null);
    }
    //单独选
    @RequestMapping("select.do")
    @ResponseBody
    public ServerReponse<CartVo> select(HttpServletRequest httpServletRequest,Integer productId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,productId);
    }
    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerReponse<CartVo> unSelect(HttpServletRequest httpServletRequest,Integer productId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UN_CHECKED,productId);
    }
    //查询当前用户的购物车里面的产品数量，如果一个产品有10个，就返回10
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerReponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());

    }

}
