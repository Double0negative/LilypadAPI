package org.mcsg.api.util;

import org.bukkit.Bukkit;

public class MessageUtil {
    
    
    public static String format(String msg, Object ... args){ //alias for newbs
	return _(msg, args);
    }
    
    public static String _(String msg, Object ... args){ //will replace colors and take args like {0}
	return StringUtil.replaceVars(msg, args).replaceAll("(&([a-fk-or0-9]))", "\u00A7$2");
    }
    
    public static void bcast(String msg){  //alias for newbs
	$(msg);
    }
    
    public static void sendPlayerMessage(String player, String msg){  //alias for newbs
	$(player, msg);
    }
    
    public static void $(String msg){ //bcast a message
	Bukkit.broadcastMessage(_(msg));
    }
    
    public static void $(String user, String msg){  //sends a message to a player
	Bukkit.getPlayer(user).sendMessage(_(msg));
    }
}
