package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerReponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {
    ServerReponse createOrder(Integer userId,Integer shippingId);
    ServerReponse<String> cancel(Integer userId,Long orderNo);
    ServerReponse getOrderCartProduct(Integer userId);
    ServerReponse<OrderVo> detail(Integer userId, Long orderNo);
    ServerReponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
    ServerReponse<PageInfo> manageOrderList(int pageNum,int pageSize);
    ServerReponse<OrderVo> manageDetail(Long orderNo);
    ServerReponse<PageInfo> manageSerachOrder(Long orderNo,int pageNum,int pageSize);
    ServerReponse<String> manageSendGoods(Long orderNo);

    ServerReponse pay(Long orderNo, Integer userId, String path);
    ServerReponse alipayCallback(Map<String,String> params);
    ServerReponse queryOrderPayStatus(Integer userId,Long orderNo);
}
