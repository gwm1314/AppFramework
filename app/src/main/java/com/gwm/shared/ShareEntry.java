package com.gwm.shared;

/**
 * Created by John on 2016/3/25.
 */
public class ShareEntry {
    public String shareTitle; // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    public String shareTitleUrl;  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    public String shareContent;   // text是分享文本，所有平台都需要这个字段
    public String shareImagePath;  // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    public String shareUrl;  // url仅在微信（包括好友和朋友圈）中使用
    public String shareComment;  // comment是我对这条分享的评论，仅在人人网和QQ空间使用
    public String shareSite;  // site是分享此内容的网站名称，仅在QQ空间使用
    public String shareSiteUrl;  // siteUrl是分享此内容的网站地址，仅在QQ空间使用
}
