package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerReponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;



@Service("iUserService")
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerReponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if(resultCount==0){
            return ServerReponse.createByErrorMessage("用户名不存在");
        }
        //to do密码登录MD5
        String md5Password=MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username,md5Password);
        if(user==null){
            return ServerReponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);//将密码置空
        return ServerReponse.createBySuccess("登录成功",user);
    }

    public ServerReponse<String> register(User user){
//        int resultCount=userMapper.checkUsername(user.getUsername());
//        if(resultCount>0){
//            return ServerReponse.createByErrorMessage("用户名已存在");
//        }
        ServerReponse validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
//        resultCount=userMapper.checkEmail(user.getEmail());
//        if(resultCount>0){
//            return ServerReponse.createByErrorMessage("邮箱已存在");
//        }
        user.setRole(Const.Role.ROLE_CUSTOME);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerReponse.createByErrorMessage("注册失败");
        }
        return ServerReponse.createBySuccessMsg("注册成功");
    }

    public ServerReponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServerReponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int  resultCount=userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerReponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else{
            return ServerReponse.createByErrorMessage("参数错误");
        }
        return ServerReponse.createBySuccessMsg("校验成功");
    }

    public ServerReponse selectQuestion(String username){
        ServerReponse validReponse=this.checkValid(username,Const.USERNAME);
        if(validReponse.isSuccess()){
            //用户不存在
            return ServerReponse.createByErrorMessage("用户不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerReponse.createBySuccess(question);
        }
        return ServerReponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerReponse<String> checkAnswer(String username,String question,String answer){
        int resultCount=userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken= UUID.randomUUID().toString();
            RedisShardedPoolUtil.setEx(Const.TOKEN_PREFIX+username,forgetToken, 60 * 60 * 12);
            return ServerReponse.createBySuccess(forgetToken);
        }
        return ServerReponse.createByErrorMessage("问题答案错误");
    }
    public ServerReponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerReponse.createByErrorMessage("参数错误，token需要传递");

        }
        ServerReponse validReponse=this.checkValid(username,Const.USERNAME);
        if(validReponse.isSuccess()){
            return ServerReponse.createByErrorMessage("用户不存在");
        }
        String token = RedisShardedPoolUtil.get(Const.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerReponse.createByErrorMessage("token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password= MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount>0){
                return  ServerReponse.createBySuccessMsg("修改密码成功");
            }
        }else {
            return ServerReponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerReponse.createByErrorMessage("修改密码失败");
    }

    public ServerReponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //为了防止横向越权，要校验一下这个用户的旧密码，一定要是这个用户，因为我们会查询一个count(1),如果不指定id，那么结果就是true，count>0.
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount==0){
            return ServerReponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerReponse.createBySuccessMsg("密码修改成功");
        }
        return ServerReponse.createByErrorMessage("密码修改失败");
    }


    public ServerReponse<User> update_information(User user){
        //username是不能被更新的
        //email也要进行一个校验，校验新的email是不是已经存在，并且存在的email如果相同的话，不能是我们当前的这个用户
        int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerReponse.createByErrorMessage("email已经存在，请更换email在尝试更新");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerReponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerReponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerReponse<User> getInformation(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerReponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerReponse.createBySuccess(user);
    }

    //bankend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerReponse checkAdminRole(User user){
        if(user!=null && user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerReponse.createBySuccess();
        }
        return ServerReponse.createByError();
    }
}
