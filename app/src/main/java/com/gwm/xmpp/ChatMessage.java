package com.gwm.xmpp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

/**
 * 用与封装聊天信息的实体类
 * @author gwm
 */
public class ChatMessage{
	private String content; //聊天信息
	private byte[] bytes;
	private String type; //数据类型。用于区分表情、文字、文件、图片、语音类型
	private String format;  //如果是文件需标明文件格式
	private String path; //如果是语音只需要传递语音文件的路径
	public static final String IMAGE_TYPE = "image";  //图片
	public static final String FILE_TYPE = "file";  //文件
	public static final String FACE_TYPE = "face";  //表情
	public static final String TEXT_TYPE = "text";  //文字
	public static final String VOICE_TYPE = "voice";  //语音
	
	private static final int DEFAULT_CODE = Base64.DEFAULT;  //修改底层默认的编码格式。
	public ChatMessage(String content) {
		this.content = content;
		this.type = TEXT_TYPE;
	}
	/**
	 * 当聊天信息是一个表情时，使用该构造方法构造聊天信息
	 * @param resId 表情的ID
	 */
	public ChatMessage(Integer resId) {
		this.content = String.valueOf(resId);
		this.type = FACE_TYPE;
	}
	/**
	 * 当聊天数据不是字符串时需要使用该构造方法
	 * @param bytes
	 * @param type
	 */
	public ChatMessage(byte[] bytes, String type,String format) {
		this.bytes = bytes;
		this.type = type;
		this.format = format;
	}
	/**
	 * 构造一条语音信息
	 * @param path：语音文件的路径
	 * @param format  语音文件的格式
	 */
	public ChatMessage(String path,String format){
		this.path = path;
		this.format = format;
		this.type = VOICE_TYPE;
	}
	private ChatMessage() {}
	/**
	 * 当发送聊天信息时默认会调用该方法获取一条聊天信息
	 * @return 用JSON格式表示的聊天信息
	 */
	public String toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("type", type);
			if(type.equals(IMAGE_TYPE)){   //发送的是图片信息
				String content = getStringByByteArray(bytes);
				json.put("content", content);
				json.put("format", format);
			}else if(type.equals(FILE_TYPE)){  //发送的是文件
				json.put("content", getStringByByteArray(bytes));
				json.put("format", format);
			}else if(type.equals(FACE_TYPE)){//表情很简单，传递的是表情的id值，由于表情图片在App里面，当接收到表情消息时通过id找到对应的表情并显示出来即可
				json.put("content", content);
			}else if(type.equals(TEXT_TYPE)){
				json.put("content", content);
			}else if(type.equals(VOICE_TYPE)){
				json.put("content", getStringByFile(path));
				json.put("format", format);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	public String toXml() {
		return null;
	}
	/**
	 * 自定义数据格式时需重写该方法构造数据发送格式
	 * @return 构造好的数据
	 */
	public String toSelf(){
		return null;
	}
	/**
	 * 当发送数据时，由于XMPP是基于XML的，在XML中只能放文本类型，所以需要将字节类型数据转换成字符串
	 * @param bytes
	 * @return
	 */
	private String getStringByByteArray(byte[] bytes) {
		return Base64.encodeToString(bytes, DEFAULT_CODE);
	}
	/**
	 * 将字符串转换成字节
	 * @param str
	 * @return
	 */
	public byte[] getFile(String str){
		byte[] buf = Base64.decode(str, DEFAULT_CODE);
		return buf;
	}
	/**
	 * 将JSON数据解析成聊天信息对象
	 * @param json
	 * @return
	 */
	public static ChatMessage fromJson(String json){
		ChatMessage message = new ChatMessage();
		try {
			JSONObject jsonObj = new JSONObject(json);
			message.type = jsonObj.getString("type");
			if(message.type.equals(IMAGE_TYPE)){
				String content = jsonObj.getString("content");
				message.bytes = message.getFile(content);
			}else if(message.type.equals(FILE_TYPE)){
				String content = jsonObj.getString("content");
				message.bytes = message.getFile(content);
			}else if(message.type.equals(FACE_TYPE)){//表情很简单，传递的是表情的id值，由于表情图片在App里面，当接收到表情消息时通过id找到对应的表情并显示出来即可
				message.content = jsonObj.getString("content");
			}else if(message.type.equals(TEXT_TYPE)){
				message.content = jsonObj.getString("content");
			}else if(message.type.equals(VOICE_TYPE)){
				String content = jsonObj.getString("content");
				message.bytes = message.getFile(content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}
	/**
	 * 当发送语音时会调用该方法将语音数据转换成String
	 * @param path 语音文件的路径
	 * @return
	 */
	private String getStringByFile(String path){
		ByteArrayOutputStream bos = null;
		FileInputStream fis = null;
		try {
			bos = new ByteArrayOutputStream();
			fis = new FileInputStream(path);
			int len = 0;
			byte[] buf = new byte[1024];
			while((len = fis.read(buf)) != -1){
				bos.write(buf, 0, len);
			}
			byte[] buff = bos.toByteArray();
			String str = Base64.encodeToString(buff, DEFAULT_CODE);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis != null){
					fis.close();
				}
				if(bos != null){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
