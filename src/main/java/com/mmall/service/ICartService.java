package com.mmall.service;

import com.mmall.common.ServerReponse;
import com.mmall.vo.CartVo;

public interface ICartService {
    ServerReponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerReponse<CartVo> update(Integer userId,Integer productId,Integer count);
    ServerReponse<CartVo> deleteProduct(Integer userId,String productIds);
    ServerReponse<CartVo> list(Integer userId);
    ServerReponse<CartVo> selectOrUnselect(Integer userId,Integer checked,Integer productId);
    ServerReponse<Integer> getCartProductCount(Integer userId);
}
