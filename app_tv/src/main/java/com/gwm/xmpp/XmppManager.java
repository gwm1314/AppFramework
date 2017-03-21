package com.gwm.xmpp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.os.Message;

import com.gwm.base.BaseRunnable;
import com.gwm.util.MyLogger;

/**
 * XMPP连接管理器
 * @author gwm
 */
public class XmppManager implements com.gwm.android.Handler.HandlerListener{
	private MyLogger Log;
	private String XmppHost; // 主机名IP
	private int XmppPort; // 端口号
	private String serviceName; // 服务器名称
	private String username; // 用户名
	private String password; // 密码
	private static XmppManager manager = new XmppManager();
	private ExecutorService executor; // 线程执行器，用于高并发访问
	private XMPPConnection connection; // XMPP连接对象
	private List<Runnable> runs; // 线程池
	private boolean running; // 判断当前是否有线程处于运行状态
	private XMPPListener listener; // XMPP监听器总闸
	private static final int XMPP_SERVICE_INTERCEPTION = 100; // 拦截服务器所有的请求所产生的数据包
	private static final int XMPP_CONNECTION_SUCCESS = 200; // 服务器连接成功
	private static final int XMPP_CONNECTION_ERROR = 404; // 服务器连接失败
	private static final int XMPP_LOGIN_SUCCESS = 500; // 用户登录成功
	public static final int XMPP_LOGIN_ERROR = 300; // 用户登录失败
	private static final int XMPP_REGISTER_SUCCESS = 302; //用户注册成功
	public static final int XMPP_REGISTER_ERROR = 505; //用户注册失败

	public static XmppManager getInstance() {
		return manager;
	}

	private XmppManager() {
		Log = MyLogger.kLog();
		executor = Executors.newSingleThreadExecutor();
		runs = new ArrayList<Runnable>();
	}

	public void connection() {
		addTask(new ConnectionTask(this));
	}
	public void register(){
		connection();
		addTask(new RegisterTask(this));
	}
	public void login() {
		connection();
		addTask(new LoginTask(this));
	}
	
	public boolean isConnection() {
		return connection != null && connection.isConnected();
	}

	public void setListener(XMPPListener listener) {
		this.listener = listener;
	}

	public boolean isAuthenticated() {
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

	public String getXmppHost() {
		return XmppHost;
	}

	public void setXmppHost(String xmppHost) {
		XmppHost = xmppHost;
	}

	public int getXmppPort() {
		return XmppPort;
	}

	public void setXmppPort(int xmppPort) {
		XmppPort = xmppPort;
	}

	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 设置XMPP服务器的名称
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public final class ConnectionTask extends BaseRunnable{
        public ConnectionTask(com.gwm.android.Handler.HandlerListener listener){
            super(listener);
        }
		@Override
		public void run() {
			if (!isConnection()) {
				if (getXmppHost() != null && getXmppPort() != 0) {
					ConnectionConfiguration config = new ConnectionConfiguration(
							getXmppHost(), getXmppPort());
					config.setSecurityMode(SecurityMode.enabled);
					config.setSASLAuthenticationEnabled(false);
					config.setCompressionEnabled(false);
					XMPPConnection connection = new XMPPConnection(config);
					manager.setConnection(connection);
					connection.addPacketInterceptor(new PacketInterceptor() {
						@Override
						public void interceptPacket(Packet packet) {
                            sendHandlerMessage(XMPP_SERVICE_INTERCEPTION, 0, 0, packet);
						}
					}, null);
					try {
						connection.connect();
						Log.i("XMPP 连接成功，host=" + XmppHost + ",port="
								+ XmppPort);
                        sendHandlerMessage(XMPP_CONNECTION_SUCCESS);
					} catch (XMPPException e) {
						e.printStackTrace();
						Log.i("XMPP 连接失败");
                        sendHandlerMessage(XMPP_CONNECTION_ERROR);
					}
				}
			}
			runTask();
		}
	}

	public final class LoginTask extends BaseRunnable {
        public LoginTask(com.gwm.android.Handler.HandlerListener listener){
            super(listener);
        }
		@Override
		public void run() {
			if (!isAuthenticated()) {
				Log.d("username=" + username);
				Log.d("password=" + password);
				try {
					getConnection().login(getUsername(), getPassword(),
							"android");
					Log.d("Loggedn in successfully");
                    sendHandlerMessage(XMPP_LOGIN_SUCCESS);

				} catch (XMPPException e) {
					Log.e("LoginTask.run()... xmpp error");
					Log.e("Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
                    sendHandlerMessage(XMPP_LOGIN_ERROR);
				}
			} else {
				Log.i("Logged in already");
			}
			runTask();
		}
	}
	
	public final class RegisterTask extends BaseRunnable{
        public RegisterTask(com.gwm.android.Handler.HandlerListener listener){
            super(listener);
        }
		@Override
		public void run() {
			try {
				Registration reg = new Registration();
				reg.setType(IQ.Type.SET);
				reg.addAttribute("username", getUsername());
				reg.addAttribute("password", getPassword());
				connection.sendPacket(reg);
                sendHandlerMessage(XMPP_REGISTER_SUCCESS);
			} catch (Exception e) {
				e.printStackTrace();
                sendHandlerMessage(XMPP_REGISTER_ERROR);
			}
            runTask();
		}
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	private synchronized void addTask(Runnable runnable) {
		if (runs.isEmpty() && !running) {
			if (!executor.isTerminated() && !executor.isShutdown()
					&& runnable != null) {
				running = true;
				executor.submit(runnable);
			}
		} else {
			runs.add(runnable);
		}
	}

	private synchronized void runTask() {
		running = false;
		if (!runs.isEmpty()) {
			Runnable runnable = (Runnable) runs.get(0);
			runs.remove(0);
			running = true;
			executor.submit(runnable);
		}
	}
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case XMPP_SERVICE_INTERCEPTION:
                if (listener != null) {
                    listener.interceptPacket((Packet) msg.obj);
                }
                break;
            case XMPP_CONNECTION_SUCCESS:
                if (listener != null) {
                    listener.onConnSuccess();
                }
                break;
            case XMPP_CONNECTION_ERROR:
                if (listener != null) {
                    listener.onConnError();
                }
                break;
            case XMPP_LOGIN_SUCCESS:
                if (listener != null) {
                    listener.onLoginSuccess(manager.username, manager.password);
                }
                break;
            case XMPP_LOGIN_ERROR:
                if (listener != null) {
                    listener.onLoginError(manager.username, manager.password);
                }
                break;
            case XMPP_REGISTER_SUCCESS:
                if(listener != null){
                    listener.regSuccess(manager.username, manager.password);
                }
                break;
            case XMPP_REGISTER_ERROR:
                if(listener != null){
                    listener.regError(manager.username, manager.password);
                }
                break;
        }
    }
}
