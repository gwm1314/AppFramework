package com.gwm.util;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 大数据存储工具类，该类适用于以下用途：
 * 		1.Activity之间的大数据传递。
 * 		2.大数据存储，一旦从该数据存储区取出就会消除该数据
 * 		3.暂不支持跨进程传递
 * @author gwm
 *
 */
public class CacheUtil {
	private Map<String,Object> cache = new LinkedHashMap<String,Object>();
	private static CacheUtil util = new CacheUtil();
	
	private CacheUtil(){}
	public static CacheUtil getInstance(){
		return util;
	}
	public void add(String key,Object obj){
		cache.put(key, obj);
	}
	public Object get(String key){
		return cache.remove(key);
	}
}
