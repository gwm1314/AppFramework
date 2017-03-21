   该套框架包含以下常用特性
        1."再按一次退出系统"提示：  不得重写onKeyDown()方法，直接在Activity调用addToFirst()即可。
        2.自动加载布局，在Activity/Fragment/Page中重写onInitAttribute()，例：

            public void onInitAttribute(BaseAttribute attr){
                attr.mSetView = true;
            }

            MainActivity--activity_main  AutoZspActivity--activity_auto_zsp  采用驼峰式命名法则
        3.无需为View设置监听器：只需要声明一个int[]类型的ids变量，框架会自动为该数组内的所有控件设置上监听事件
        4.Thread+Handler的操作封装，demo见ThreadHandler.java类
        5.优化了BaseAdapter.java  减轻了代码的负担，demo可以参照MyAdapter.java类
        6.全套含有MVC设计思想，参考demo:MVCActivity.java类
        7.对图片的优化处理 全套嵌入的AFinal框架中图片处理类：FinalBitmap.java
        8.webservice 使用方法参照WebServiceUtils.java的注释
        9.websocket 使用方法参照WebSocketUtil.java的注释
        10.xmpp/mqtt推送  demo可以参照XMPPActivity.java  mqtt推送使用详见PushService.java类的注释
        11.二维码 参考QrUtil.java类
        12.常用的自定义控件
            (包括消息数字BadgeView 横向的ListView  图案密码LocusPassWordView 自定义进度条ProgressBarFactory
            可双向拖动的seekbar 百分比布局 带有删除线的TextView ViewPagerIntdicator 字母排序控件AssortView 滚轮控件wheelView)
            https://github.com/koral--/android-gif-drawable gif动画
        13.三大支付平台的支持 支付宝、微信、银联
        14.常用工具类见util包
        15.网络访问已经全部集成到MVC设计模式中
        16.JSON数据解析，已经全部集成到MVC设计模式中
        17.视图层框架搭建Activity-->Fragment-->自定义视图组件BasePage
        18.集成ShareSDK开发。