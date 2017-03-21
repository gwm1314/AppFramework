package com.gwm.websocket;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import android.text.TextUtils;
import com.gwm.util.FileUtils;
import com.gwm.util.MyLogger;

/**
 * WebSocket推送处理类
 * @author asus1
 *
 */
public class WebSocketUtil extends WebSocketClient{
	private WebSocketListener listener;
	public void setListener(WebSocketListener listener) {
		this.listener = listener;
	}
	/**
	 * 创建WebSocket连接处理类
	 * @param serverUri
	 * @param draft   
	 * @throws Exception
	 */
	public WebSocketUtil(URI serverUri, Draft draft) throws Exception {
		super(serverUri, draft);
		connectBlocking();
	}
	/**
	 * 创建WebSocket连接处理类(推荐使用)
	 * @param serverURI  例："ws://localhost:8080/myweb/android.do"
	 * @throws Exception  服务器连接失败会抛出该异常  很有可能 是服务器的websocket版本与该框架版本不一致，或者您写的uri地址有误，请认真核对
	 */
	public WebSocketUtil(URI serverURI) throws Exception {
		this(serverURI,new Draft_17());
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("web socket已连接");
	}
	//接收服务器端推送的消息
	@Override
	public void onMessage(String message){
        MyLogger.kLog().i("服务器推送消息: " + message);
		if(listener != null){
			listener.onMessage(message);
		}
	}
	@Override
	public void onMessage(ByteBuffer bytes) {
		super.onMessage(bytes);
	}
	@Override
	public void onFragment(Framedata framedata) {
		MyLogger.kLog().i("received framedata: " + new String(framedata.getPayloadData().array()));
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
        MyLogger.kLog().i("web socket连接已关闭");
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}
	/**
	 * 发送消息到服务器
	 * @param message  消息内容
	 */
	public void sendMessage(String message){
		if(!TextUtils.isEmpty(message)){
			send(message);
		}else{
			throw new NullPointerException("发送的数据为空，message="+message);
		}
	}
	/**
	 * 利用WebSocket上传文件  发送文件到服务器(需测试)
	 * @param file
	 * @throws java.io.FileNotFoundException
	 */
	public void sendFile(File file) throws FileNotFoundException{
		if(file != null && !file.exists()){
			byte[] byteArray = FileUtils.getFileToByteArray(file);
			send(byteArray);
		}else{
			throw new FileNotFoundException("该文件不存在");
		}
	}
	/**
	 * 接受服务器端推送数据的回调接口
	 */
	public interface WebSocketListener{
		void onMessage(String message);
	}
}
