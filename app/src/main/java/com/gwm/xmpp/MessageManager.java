package com.gwm.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * XMPP消息管理者，用于发送消息和接收消息
 * @author asus1
 */
public class MessageManager {
	private static MessageManager manager = new MessageManager();
	private Message msg;
	private XMPPConnection connection;
	private XMPPListener listener;
	
	public void setListener(XMPPListener listener) {
		this.listener = listener;
	}
	public static MessageManager getInstance(){
		return manager;
	}
	private MessageManager(){
		connection = XmppManager.getInstance().getConnection();
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean arg1){
				chat.addMessageListener(new MyMessageListener());
			}
		});
		msg = new Message();
		msg.setFrom(XmppManager.getInstance().getUsername()+"@"+XmppManager.getInstance().getServiceName()+"/android");
	}
	/**
	 * 创建私聊聊天器
	 * @param to 和你聊天的对方是谁
	 */
	public void createChat(String to){
		msg.setTo(to+"@"+XmppManager.getInstance().getServiceName());
		msg.setType(Message.Type.chat);
	}
	/**
	 * 创建群聊聊天器
	 * @param to 和你聊天的对方是谁
	 */
	public void createGroupChat(String to){
		msg.setTo(to+"@"+XmppManager.getInstance().getServiceName());
		msg.setType(Message.Type.groupchat);
	}

	/**
	 * 发送一条聊天信息
	 * @param message 聊天信息
	 */
	public void sendMessage(ChatMessage message){
		msg.setBody(message.toJson());
		connection.sendPacket(msg);
	}
	private final class MyMessageListener implements MessageListener{
		@Override
		public void processMessage(Chat chat,
				org.jivesoftware.smack.packet.Message msg) {
			System.out.println(msg.toXML());
			ChatMessage message = ChatMessage.fromJson(msg.getBody()); 
			if(listener != null)
				listener.processMessage(chat,msg,message);
		}
	}
}
