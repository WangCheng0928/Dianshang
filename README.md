Java从零到企业级电商项目实战(包括第一期和第二期)
项目采用SSM框架，二期新增tomcat集群、redis集群。通过nginx负载均衡。
另外我也用springboot实现了这个项目的第一期：https://github.com/WangCheng0928/dianshangspringboot
第一期实现商城的整体流程：
	用户注册登录--->浏览商品加入购物车--->清空购物车--->下单选择送货地址--->支付--->订单完成
	其中包括许多具体功能的实现
第二期针对单点问题，对项目进行了改进，虽然集群都只有2台，但这里主要测试集群的可用性。

第一期主要模块及功能：
	后台（或管理员）：
		产品（product）
		品类（category）
		用户（user）
		统计（statistic）
		订单（order）
	门户（或普通用户）
		用户（user）
		产品（product）
		订单（order）
		购物车（cart）
		收货地址（shipping）
		支付（pay）
具体接口详情：https://github.com/WangCheng0928/DSInterfaceWiki/wiki

第二期主要针对第一期作了相应的改进，二期主要工作如下：

1、maven环境隔离
2、tomcat、redis集群
3、redis集群 + cookie 解决单点登录
4、redis + springsession集群解决单点登录
5、springMVC全局异常处理
6、springMVC拦截器配置
7、spring schedule 实现定时关单
8、spring schedule + redis分布式锁构建分布式任务调度
9、spring schedule + redisson分布式锁构建分布式任务调度

项目已部署在阿里云服务器上
后台接口地址：http://www.wchenglearning.xyz/
