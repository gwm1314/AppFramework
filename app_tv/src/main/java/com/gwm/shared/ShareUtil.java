package com.gwm.shared;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by John on 2016/3/25.
 */
public class ShareUtil {
    /**
     * 启动分享窗口
     *
     * @param context
     * @param entry   分享的内容
     */
    public void showShare(Context context, ShareEntry entry) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle(entry.shareTitle);
        oks.setTitleUrl(entry.shareTitleUrl);
        oks.setText(entry.shareContent);
        oks.setImagePath(entry.shareImagePath);//确保SDcard下面存在此张图片
        oks.setUrl(entry.shareUrl);
        oks.setComment(entry.shareComment);
        oks.setSite(entry.shareSite);
        oks.setSiteUrl(entry.shareSiteUrl);
        // 启动分享GUI
        oks.show(context);
    }

    //----------------------------------------------------------第三方登录-------------------------------------------------
    /**
     * 第三方平台登录

     获取用户资料的最主要用途是实现第三方平台登录的功能。一般来说，如果您的应用已经拥有自己的登录/注册功能了，但是您还希望提供用户一种利用已有的微博等第三方平台的账号快速登录到您的系统，那么您可以选择简单的“授权-登录”，或者“获取用户资料-注册-登录”。
     第一种方法会让您的系统一直依赖第三方平台，其操作方式如下：

     1、点击您应用的“登录”按钮
     2、调用authorize引导用户授权
     4、成功使用getDb().getUserId()来获取此用户在此平台上的id
     5、如果id不为空，就视为用户已经登录
     而第二种方法您的应用需要有自己的账号系统。操作如下：

     1、点击您应用的“登录”按钮
     2、通过用户指定的平台，使用getDb().getUserId()来得到用户在此平台上的id
     3、如果id不为空，则提交给您的登录接口，否则调用showUser请求用户的资料
     4、服务器接收到id以后判断用户是否已经注册，若已注册，认为登录成功，否则引导客户端进入注册流程
     5、客户端进入注册流程以后，将从showUser得到的资料填写到注册页面，用户完善资料以后，将其id和资料一并提交给您应用的服务器
     6、如果注册成功，引导用户进入客户端应用
     获取资料前ShareSDK会自行判断平台是否已经授权，若未授权，会自行执行授权操作。
     更多第三方平台登录的实现技巧，可以参考ShareSDK BBS中的相关页面。
     */

    /**
     * 第三方登录授权
     *
     * @param name       授权方(可取的值：SinaWeibo.NAME、QQ.NAME)
     * @param paListener 授权监听器
     */
    public void platfrom(String name, PlatformActionListener paListener) {
        Platform weibo = ShareSDK.getPlatform(name);
        weibo.setPlatformActionListener(paListener);
        weibo.authorize();
        //移除授权
        //weibo.removeAccount(true);
    }

    /**
     * 第三方登录SSO授权
     * 这里需要注意的是新浪微博客户端授权是需要用户在开发者平台（网址：http://open.weibo.com）申请的应用用过了新浪的审核。
     * 而且要通过keystore进行签名打包测试。注意打包所用的keystore上的md5签名、项目的包名要与新浪开发者平台上填写的签名与包名一致。
     */
    public void SSOplatfrom(String name, PlatformActionListener paListener) {
        Platform weibo = ShareSDK.getPlatform(name);
        weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
        weibo.setPlatformActionListener(paListener); // 设置分享事件回调
        weibo.authorize();
    }

    /**
     * 获取授权信息
     *
     * @param context
     * @param name    平台名称(可取的值：SinaWeibo.NAME、QQ.NAME等)
     * @return
     */
    public Platform getPlatfromInfo(Context context, String name) {
        Platform qzone = ShareSDK.getPlatform(context, name);
        String accessToken = qzone.getDb().getToken(); // 获取授权token
        String openId = qzone.getDb().getUserId(); // 获取用户在此平台的ID
        String nickname = qzone.getDb().get("nickname"); // 获取用户昵称
        // 接下来执行您要的操作
        return qzone;
    }

    /**
     * 删除授权信息
     *
     * @param context
     * @param name       平台名称(可取的值：SinaWeibo.NAME、QQ.NAME等)
     * @param paListener
     */
    public void removePlatfrom(Context context, String name, PlatformActionListener paListener) {
        Platform qzone = ShareSDK.getPlatform(context, name);
        if (qzone.isValid()) {
            qzone.removeAccount();
        }
        qzone.setPlatformActionListener(paListener);
        qzone.authorize();
        //isValid和removeAccount不开启线程，会直接返回。
    }

    /**
     * 获取授权用户的资料
     *
     * @param context
     * @param name       平台名称
     * @param paListener
     */
    public void getUserPlatfromInfo(Context context, String name, PlatformActionListener paListener) {
        Platform weibo = ShareSDK.getPlatform(context, name);
        weibo.setPlatformActionListener(paListener);
        weibo.showUser(null);//执行登录，登录后在回调里面获取用户资料
        //weibo.showUser(“3189087725”);//获取账号为“3189087725”的资料  如果为null，则表示获取授权账户自己的资料。
    }

    //--------------------------------------------------关注--------------------------------------------

    /**
     * 关注好友
     *
     * @param context
     * @param name       平台名称(目前只实现了两个平台：新浪微博、腾讯微博)
     * @param account    好友的账户
     * @param paListener
     */
    public void focusOn(Context context, String name, String account, PlatformActionListener paListener) {
        Platform weibo = ShareSDK.getPlatform(context, name);
        //添加平台事件监听
        weibo.setPlatformActionListener(paListener);
        //关注好友
        weibo.followFriend(account);
    }

    /**
     * 获取关注列表(其中Facebook获取的是好友列表)
     * @param weibo ShareSDK对应的微博类的实例对象(SinaWeibo、TencentWeibo)
     * @param context
     * @param paListener
     *
     * Share SDK当前仅为有限的平台提供获取关注列表的功能，这些平台包括：新浪微博、腾讯微博
     */
    public void getFocusOnList(Platform weibo,Context context,final PlatformActionListener paListener) {
        weibo.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform weibo, int action, Throwable t) {
                t.printStackTrace();
                if (action == Platform.ACTION_GETTING_FRIEND_LIST) {
                    // 在这里处理获取关注列表失败的代码
                    paListener.onError(weibo,action,t);
                }
            }

            public void onComplete(Platform weibo, int action,
                                   HashMap<String, Object> res) {
                if (action == Platform.ACTION_GETTING_FRIEND_LIST) {
                    // 在这里处理获取关注列表成功的代码
                    paListener.onComplete(weibo,action,res);
                }
            }

            public void onCancel(Platform weibo, int action) {
                if (action == Platform.ACTION_GETTING_FRIEND_LIST) {
                    // 在这里处理取消获取关注列表的代码
                    paListener.onCancel(weibo,action);
                }
            }
        });
        weibo.listFriend(50, 0, null); // 获取授权账号的列表则传递null
    };
}
