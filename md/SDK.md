# SDK

+ proguard-rules.pro
+ consumer_proguard (运行时keep class)

![image-20200306142053162](C:\Users\czdxn\AppData\Roaming\Typora\typora-user-images\image-20200306142053162.png)



+ 打包aar release   对外keep类
+ 混淆合并：
  + aar 混淆  proguard-rules.pro---keep-
  + no-deal-with
+ flurry 需要初始化
  + try-catch
+ 打点  LogUtils 日志记录问题
  + 初始化
  + 填充 
  + 点击
  + 请求
  + 报错
+ ![image-20200306144709190](C:\Users\czdxn\AppData\Roaming\Typora\typora-user-images\image-20200306144709190.png)
+ 方法数超限
  + mulidex

- 注意事项：
  - 编写sdk
  - 录入广告
  - 请求
- 框架

框架 -->appId-->保活-->内存-->等



附件 sdk.txt

--------------------------------------------

保活：

1、JobService

2、一像素

3、账户

4、workManager（成都）

5、一些服务

6、隐藏图标（成都）

广告：

https://developers.google.com/admob/android/native/options?hl=zh-cn

https://developers.facebook.com/docs/audience-network/guides/ad-formats/native/android#delay

https://developers.mopub.com/publishers/android/banner/

外部：

1、监听亮屏，暗屏、内存抖动（上次和这次内存值比较）、wifi速度、电量

做法：起一个轮询有一个时间间隔，间隔到了就出广告

外部场景有一些限制：广告间隔、点击次数、展示次数、每一个场景的限制等为了防止不能一直出

比如wifi的速度值、内存的限制值