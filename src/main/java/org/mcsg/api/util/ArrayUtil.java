package org.mcsg.api.util;

public class ArrayUtil {

	public static String[] cutArray(String[] array, int i){
		String[]result = new String[array.length - i];
		for(int a = i;a<array.length;a++){
			result[a-i] = array[a];
		}
		return result;
	}
	
}
