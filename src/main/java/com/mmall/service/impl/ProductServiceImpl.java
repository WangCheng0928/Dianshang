package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerReponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;
    public ServerReponse saveOrUpdateProduct(Product product){
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray=product.getSubImages().split(",");
                if(subImageArray.length>0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if(product.getId()!=null){
                //这里要注意如果是更新产品，一定要传入产品id，否则会新增一条产品信息
                int rowConut=productMapper.updateByPrimaryKey(product);
                if(rowConut>0){
                    return ServerReponse.createBySuccessMsg("更新产品成功");
                }else {
                    return ServerReponse.createByErrorMessage("更新产品失败");
                }
            }else {
                int rowCount=productMapper.insert(product);
                if(rowCount>0){
                    return ServerReponse.createBySuccessMsg("新增产品成功");
                }else{
                    return ServerReponse.createByErrorMessage("新增产品失败");
                }

            }
        }
        return ServerReponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    public ServerReponse<String> setSaleStatus(Integer productId,Integer status){
        if(productId==null || status==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount=productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServerReponse.createBySuccessMsg("修改产品销售状态成功");
        }else {
            return ServerReponse.createByErrorMessage("修改产品销售状态失败");
        }
    }
    public ServerReponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerReponse.createByErrorMessage("产品已下架或删除");
        }
        //VO对象--value object
        //pojo--bo（business object）--vo（view object）
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServerReponse.createBySuccess(productDetailVo);
    }
    private  ProductDetailVo  assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategotyId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost,从配置文件中获取，配置和代码分离
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp.server.http.prefix"));
        //parentCategoryId
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null){
            //默认根节点
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        //createTime,从mybatis获取出来的是毫秒数不易于阅读，需要对createtime和updatetime进行一个转化
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        //updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServerReponse<PageInfo> getProductList(int pageNum,int pageSize){
        //1.startPage---start
        //2.填充自己的sql查询逻辑
        //3.pageHelper--收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectList();
        List<ProductListVo> productListVoList= Lists.newArrayList();
        for(Product product:productList){
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerReponse.createBySuccess(pageInfo);

    }
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp.server.http.prefix"));
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        return  productListVo;
    }

    public ServerReponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for(Product product:productList){
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerReponse.createBySuccess(pageInfo);
    }

    public ServerReponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId==null){
            return ServerReponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerReponse.createByErrorMessage("产品已下架或删除");
        }
        if(product.getStatus()!= Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerReponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServerReponse.createBySuccess(productDetailVo);
    }

   public ServerReponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
       if(StringUtils.isBlank(keyword)&& categoryId==null){
           return ServerReponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
       }
       List<Integer> categoryIdList=new ArrayList<Integer>();
       if(categoryId!=null){
           Category category=categoryMapper.selectByPrimaryKey(categoryId);
           if(category==null&& StringUtils.isBlank(keyword)){
               //没有该分类，并且还没有关键字，这时候返回一个空的结果集，不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVoList);
                return ServerReponse.createBySuccess(pageInfo);

           }
           categoryIdList=iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
       }
       if(StringUtils.isNotBlank(keyword)){
           keyword=new StringBuilder().append("%").append(keyword).append("%").toString();

       }
       PageHelper.startPage(pageNum,pageSize);
       //排序处理
       if(StringUtils.isNotBlank(orderBy)){
           if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
               String[] orderByArray=orderBy.split("_");
               //PageHelper里面oederBy需要传入参数的格式为PageHelper.orderBy("price desc");
               PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
           }
       }
        List<Product> productList=productMapper.selectByNameAndCategoryId(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for(Product product:productList){
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productList);
       pageInfo.setList(productListVoList);
        return ServerReponse.createBySuccess(pageInfo);
   }
}
