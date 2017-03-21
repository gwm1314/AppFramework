package demo.appframework.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gwm.base.BaseActivity;
import com.gwm.xmpp.ChatMessage;
import com.gwm.xmpp.XMPPListener;
import com.gwm.xmpp.XmppManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Packet;

/**
 * XMPP推送demo
 */
public class XMPPActivity extends BaseActivity {
    XmppManager manager1 = XmppManager.getInstance(); //获取XMPP管理器实例
    @Override
    public void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {

        manager1.setListener(listener); //设置XMPP回调监听器
        manager1.setXmppHost(""); //设置服务器的IP地址
        manager1.setXmppPort(9091); //设置服务器的端口号
        manager1.connection(); //发送连接请求
    }
    private XMPPListener listener = new XMPPListener(){
        @Override
        public void interceptPacket(Packet packet) {
            //监听与服务器之间的交互，无论是服务器推送还是客户端请求都会回调该方法
            super.interceptPacket(packet);
        }
        public void onConnSuccess(){
            //连接服务器成功时回调
            manager1.setUsername(""); //设置登录用户名
            manager1.setPassword(""); //设置密码
            manager1.setServiceName(""); //设置服务器名称
            manager1.login(); //发送登录请求
        }
        public void onLoginSuccess(String username, String password){
            //登录服务器成功时回调

        }
        public void onLoginError(String username, String password){
            //登录失败时回调
        }
        public void processMessage(Chat chat,
                                   org.jivesoftware.smack.packet.Message msg, ChatMessage message){
            //处理服务器端推送的聊天消息
        }
        public void regSuccess(String username, String password){
            //注册成功
        }
        public void regError(String username, String password) {
            //注册失败
        }
    };
}
