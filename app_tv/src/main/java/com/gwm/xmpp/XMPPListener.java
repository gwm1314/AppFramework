package com.gwm.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Packet;

/**
 * XMPP监听器，所有的监听接口都已封装到该类中
 * @author asus1
 *	该类的所有方法均在主线程中运行
 */
public class XMPPListener {
	/**
	 * 每当您与服务器通讯时都会回调该方法，可用于检验数据包和处理服务器返回的数据，处理好友请求等
	 * @param packet 与服务器通讯的数据包
	 */
	public void interceptPacket(Packet packet){
		System.out.println(packet.toXML());
	}
	/**
	 * 当连接服务器成功后会回调该方法
	 */
	public void onConnSuccess() {
		
	}
	/**
	 * 当连接服务器失败后会回调该方法
	 */
	public void onConnError() {
		
	}
	/**
	 * XMPP登陆成功，用户以上线
	 * @param username 用户名
	 * @param password 密码
	 */
	public void onLoginSuccess(String username, String password) {
		
	}
	/**
	 * XMPP登陆失败
	 * @param username 用户名
	 * @param password 密码
	 */
	public void onLoginError(String username, String password) {
		
	}
	/**
	 * 用于处理聊天时，来回发送的数据包，常用于聊天时接收别人发过来的消息
	 * @param chat
	 * @param msg
	 * @param message 
	 */
	public void processMessage(Chat chat,
			org.jivesoftware.smack.packet.Message msg, ChatMessage message) {
		
	}
	/**
	 * 注册成功
	 * @param username
	 * @param password
	 */
	public void regSuccess(String username, String password) {
		
	}
	/**
	 * 注册失败
	 * @param username
	 * @param password
	 */
	public void regError(String username, String password) {
		
	}
}
