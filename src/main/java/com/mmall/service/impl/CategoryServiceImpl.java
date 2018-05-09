package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerReponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
    public ServerReponse addCategory(String categoryName,Integer parentId){
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServerReponse.createByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的
        int rowCount=categoryMapper.insert(category);
        if(rowCount>0){
            return ServerReponse.createBySuccessMsg("添加品类成功");
        }
        return ServerReponse.createByErrorMessage("添加品类失败");

    }

    public ServerReponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServerReponse.createByErrorMessage("更新品类参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerReponse.createBySuccessMsg("更新商品成功");
        }else{
            return ServerReponse.createByErrorMessage("更新商品失败");
        }
    }

    public ServerReponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categories=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categories)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerReponse.createBySuccess(categories);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerReponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet= Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList= Lists.newArrayList();
        if(categoryId!=null){
            for (Category category:categorySet){
                categoryIdList.add(category.getId());
            }
        }
        return ServerReponse.createBySuccess(categoryIdList);
    }
    //递归算法就是自己调自己，算出子节点
    private Set<Category> findChildCategory(Set<Category> categories,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categories.add(category);
        }
        //查找子节点，递归算法一定有一个退出的条件
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
//        这里需要注意的是，这里是mybatis返回的集合，mybatis对于返回集合的处理是
//        如果没有查到的话,不会做一个返回null，所以categoryList不会为null，不需要做空判断
//        但如果是其他不可预知的方法就需要做一个空判断，否则就会出现空指针异常
        for (Category category1:categoryList){
            findChildCategory(categories,category1.getId());
        }
        return categories;
    }

}
