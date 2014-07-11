package org.mcsg.api.util;

import java.util.Map;


public class ListUtil {
	
	public static long getLong(String key, Map<String, String> map){
		return Long.parseLong(map.get(key));
	}	
	
	public static int getInteger(String key, Map<String, String> map){
		return Integer.parseInt(map.get(key));
	}	
	
	public static double getDouble(String key, Map<String, String> map){
		return Double.parseDouble(map.get(key));
	}
	
	public static float getFloat(String key, Map<String, String> map){
		return Float.parseFloat(map.get(key));
	}
	
	public static boolean getBoolean(String key, Map<String, String>map){
		return Boolean.parseBoolean(map.get(key));
	}
}
