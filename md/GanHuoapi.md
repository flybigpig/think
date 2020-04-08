# GanHuoapi

# 干货集中营API v2文档



## 首页banner轮播

`https://gank.io/api/v2/banners` 请求方式: `GET`
注:返回首页banner轮播的数据

------

## 分类 API

`https://gank.io/api/v2/categories/` 请求方式: `GET`
注:获取所有分类具体子分类[types]数据

- category_type 可接受参数 Article | GanHuo | Girl
- Article: 专题分类、 GanHuo: 干货分类 、 Girl:妹子图



示例:

- 获取干货所有子分类
  `https://gank.io/api/v2/categories/GanHuo`
- 获取文章所有子分类
  `https://gank.io/api/v2/categories/Article`
- 获取妹纸所有子分类
  `https://gank.io/api/v2/categories/Girl`
  妹子图类型只有一项: 且为Girl



------

## 分类数据 API

`https://gank.io/api/v2/data/category//type//page//count/`
请求方式: `GET`
注:

- category 可接受参数 All(所有分类) | Article | GanHuo | Girl
- type 可接受参数 All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
- count: [10, 50]
- page: >=1



示例:

- 获取妹子列表
  `https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10`
- 获取Android干货列表
  `https://gank.io/api/v2/data/category/GanHuo/type/Android/page/1/count/10`



------

## 随机 API

`https://gank.io/api/v2/random/category//type//count/`
请求方式: `GET`
注:

- category 可接受参数 Article | GanHuo | Girl
- type 可接受参数 Android | iOS | Flutter | Girl，即分类API返回的类型数据
- count: [1, 50]

示例:

- 获取干货分类下Android子分类的10个随机文章列表
  `https://gank.io/api/v2/random/category/GanHuo/type/Android/count/10`





------

## 文章详情 API

`https://gank.io/api/v2/post/`
请求方式: `GET`
注:

- post_id 可接受参数 文章id[分类数据API返回的_id字段]

示例:

- 获取`5e777432b8ea09cade05263f`的详情数据
  `https://gank.io/api/v2/post/5e777432b8ea09cade05263f`





------

## 本周最热 API

`https://gank.io/api/v2/hot//category//count/`
请求方式: `GET`
注:

- hot_type 可接受参数 views（浏览数） | likes（点赞数） | comments（评论数）❌
- category 可接受参数 Article | GanHuo | Girl
- count: [1, 20]



------

## 文章评论获取 API

`https://gank.io/api/v2/post/comments/`
请求方式: `GET`
注:

- post_id 可接受参数 文章Id



------

## 搜索 API

`https://gank.io/api/v2/search//category//type//page//count/`
请求方式: `GET`
注:

- search 可接受参数 要搜索的内容
- category 可接受参数 All[所有分类] | Article | GanHuo
- type 可接受参数 Android | iOS | Flutter ...，即分类API返回的类型数据
- count: [10, 50]
- page: >=1

示例:

- 搜索干货-Android分类下的Android关键字 `https://gank.io/api/v2/search/android/category/GanHuo/type/Android/page/1/count/10`
- 搜索文章-Android分类下的Android关键字 `https://gank.io/api/v2/search/android/category/Article/type/Android/page/1/count/10`
- 搜索全部分类下的Android关键字 `https://gank.io/api/v2/search/android/category/All/type/All/page/1/count/10`



