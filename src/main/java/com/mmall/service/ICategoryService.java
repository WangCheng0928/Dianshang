package com.mmall.service;

import com.mmall.common.ServerReponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerReponse addCategory(String categoryName, Integer parentId);
    ServerReponse updateCategoryName(Integer categoryId,String categoryName);
    ServerReponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerReponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
