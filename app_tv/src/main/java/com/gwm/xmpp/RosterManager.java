package com.gwm.xmpp;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * XMPP好友管理器
 * @author asus1
 */
public class RosterManager {
	private Roster roster; //花名册对象
	private XmppManager manager;
	private static RosterManager roster_manager = new RosterManager();
	public static RosterManager getInstance(){
		return roster_manager;
	}
	private RosterManager(){
		manager = XmppManager.getInstance();
		XMPPConnection conn = manager.getConnection();
		if(conn != null)
			roster = conn.getRoster();
	}
	/**
	 * 创建组
	 * @param groupName 组名
	 */
	public void createGroup(String groupName){
		roster.createGroup(groupName);
	}
	/**
	 * 添加好友
	 * @param user 用户名
	 * @param name 昵称
	 * @param group 添加到那个分组中
	 * 		注意：在添加好友之前需要设置服务器名称，调用XmppManager类的setServiceName()设置服务器名称
	 * 		manager.setServiceName("");
	 */
	public void addFriend(String user,String name,String group){
		try {
			roster.createEntry(user+"@"+manager.getServiceName(), name, new String[]{group});
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有的好友
	 * @return
	 */
	public List<RosterEntry> getFriend(){
		return  new ArrayList<RosterEntry>(roster.getEntries());
	}
	/**
	 * 获取所有的组信息
	 * @return
	 */
	public List<RosterGroup> getGroups(){
		return new ArrayList<RosterGroup>(roster.getGroups());
	}
	/**
	 * 获取某个组下的所有好友信息
	 * @param group
	 * @return
	 */
	public List<RosterEntry> getFriendByGroup(String group){
		return new ArrayList<RosterEntry>(roster.getGroup(group).getEntries());
	}
}
