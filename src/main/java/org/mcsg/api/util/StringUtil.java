package org.mcsg.api.util;

public class StringUtil {
    
    public static String implode(String sep, Object ... list){
	StringBuilder sb = new StringBuilder();
	for(Object ob: list){
	    sb.append(ob.toString()).append(sep);
	}
	return sb.toString();		
    }
    
    public static String arrayToString(String[] args){
	StringBuilder sb = new StringBuilder();
	for(String str: args){
	    sb.append(str).append(" ");
	}
	return sb.toString();
    }
    
    public static String replaceVars(String str, Object ... args){
	int a = 0;
	for(Object ob: args){
	    str = str.replace("{"+a+"}", ob.toString());
	    a++;
	}
	return str;
    }
}
