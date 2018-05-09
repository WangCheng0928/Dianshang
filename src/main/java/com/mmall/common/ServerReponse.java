package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候，如果是null的对象，key也会消失
public class ServerReponse<T> implements Serializable {
    private int status;
    private String msg;
    //为了做成一个通用的，采用泛型T,最大的好处在返回时可以指定泛型里面的内容
    //，也可以不指定泛型里面的强制类型
    private T data;


    //这几个不同类型的私有构造器是为了封装public方法时调用起来比较优雅
    //比较简明和通用
    private ServerReponse(int status) {
        this.status = status;
    }
    private ServerReponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
    private ServerReponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerReponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    @JsonIgnore
//    使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status== ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerReponse<T> createBySuccess(){
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerReponse<T> createBySuccessMsg(String msg){
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerReponse<T> createBySuccess(T data){
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerReponse<T> createBySuccess(String msg, T data){
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServerReponse<T> createByError(){
        return new ServerReponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerReponse<T> createByErrorMessage(String errorMessage){
        return new ServerReponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServerReponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerReponse<T>(errorCode,errorMessage);
    }

}
