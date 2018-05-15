package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;

import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerReponse productSave(HttpServletRequest httpServletRequest, Product product){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，有权限进行新增商品的操作
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerReponse.createByErrorMessage("不是管理员，没有增加商品的权限");
        }

    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerReponse setSaleStatus(HttpServletRequest httpServletRequest, Integer productId,Integer status){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，有权限进行商品状态
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerReponse.createByErrorMessage("不是管理员，没有增加商品的权限");
        }

    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerReponse getDetail(HttpServletRequest httpServletRequest, Integer productId){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，可以获取商品详情
            return iProductService.manageProductDetail(productId);

        }else {
            return ServerReponse.createByErrorMessage("不是管理员，没有增加商品的权限");
        }

    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerReponse getList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，具备操作权限
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerReponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerReponse productSerach(HttpServletRequest httpServletRequest,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，具备操作权限
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServerReponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerReponse upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return ServerReponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，可以上传文件
            //上传完之后这个upload的路径会上传到webapp/WEB-INF下，与index.jsp属于同级
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap= Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerReponse.createBySuccess(fileMap);
        }else {
            return ServerReponse.createByErrorMessage("不是管理员没有上传文件的权限");
        }
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        String userJsonstr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonstr, User.class);
        if(user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
//            返回值类型
//            {
//                "success": true/false,
//                    "msg": "error message", # optional
//                "file_path": "[real file path]"
//            }
            //富文本中对于返回值有自己的要求，我们使用的是simditor所以按照simditor的要求进行返回
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","没有操作权限");
            return resultMap;
        }
    }
}
