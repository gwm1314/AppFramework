package com.gwm.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 该类已经自动嵌入到MVC设计模式中
 */
@Deprecated
public class JsonParseUtils {
	
	public static void parse(JSONObject jo,Object item){
		parse(jo, item, item.getClass(), true);
	}
	
	public static void parse(JSONObject jo,Object item,Class<?> clazz){
		parse(jo, item, clazz, true);
	}
	
	public static void parse(JSONObject jo,Object item,Class<?> c,boolean bUseLowerCase){
		Field fs[] = c.getDeclaredFields();
		for(Field f : fs){
			String name = f.getName();
			if(bUseLowerCase){
				name = name.toLowerCase(Locale.getDefault());
			}
			if(jo.has(name) && !jo.isNull(name)){
				final Class<?> clazz = f.getType();
				try{
					f.setAccessible(true);
					if(clazz.equals(String.class)){
						f.set(item, jo.getString(name));
					}else if(clazz.equals(int.class)){
						f.set(item, jo.getInt(name));
					}else if(clazz.equals(boolean.class)){
						final String value = jo.getString(name);
						f.set(item, "1".equals(value) || "true".equals(value));
					}else if(clazz.equals(long.class)){
						f.set(item, jo.getLong(name));
					}else if(clazz.equals(double.class)){
						f.set(item, jo.getDouble(name));
					}else if(clazz.equals(float.class)){
						f.set(item, (float)jo.getDouble(name));
					}
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
		}
	}
	
	public static <T> void parseArrays(JSONObject jo,List<T> items,String fieldName,Class<T> clazz){
		try{
			JSONArray ja = jo.getJSONArray(fieldName);
			int length = ja.length();
			for(int index = 0;index < length;++index){
				Constructor<T> c = clazz.getDeclaredConstructor(JSONObject.class);
				try{
					c.setAccessible(true);
					items.add(c.newInstance(ja.getJSONObject(index)));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static <T> List<T> parseArrays(JSONObject jo,String fieldName,Class<T> clazz){
		List<T> items = new ArrayList<T>();
		try{
			JSONArray ja = jo.getJSONArray(fieldName);
			int length = ja.length();
			for(int index = 0;index < length;++index){
				Constructor<T> c = clazz.getConstructor(JSONObject.class);
				try{
					items.add(c.newInstance(ja.getJSONObject(index)));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return items;
	}
}
