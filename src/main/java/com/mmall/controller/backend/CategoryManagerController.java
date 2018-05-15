package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerReponse addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加我们处理分类的逻辑
            return  iCategoryService.addCategory(categoryName,parentId);

        }else{
            return ServerReponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerReponse setCategoryName(HttpServletRequest httpServletRequest,Integer categoryId,String categoryName){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }else{
            if(iUserService.checkAdminRole(user).isSuccess()){
                //是管理员，可以修改类型名称
                return  iCategoryService.updateCategoryName(categoryId,categoryName);
            }else {
                return ServerReponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
    }
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerReponse getChildrenParallelCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }else{
            if(iUserService.checkAdminRole(user).isSuccess()){
                //查询子节点的category信息，并且不递归，保持平级
                return iCategoryService.getChildrenParallelCategory(categoryId);
            }else{
                return ServerReponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }

    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerReponse getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }else {
            if(iUserService.checkAdminRole(user).isSuccess()){
                //是管理员，查询当前节点的id和递归子节点的id
                return iCategoryService.selectCategoryAndChildrenById(categoryId);
            }else {
                return ServerReponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
    }
}
