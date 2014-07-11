package org.mcsg.api;

import lilypad.client.connect.api.Connect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.api.lilypad.LinkerServer;
import org.mcsg.api.util.MessageUtil;

public class API extends JavaPlugin{
    
    private static API api;
    private static boolean debug = false;
    private static boolean shuttingdown = false;
    
    private static Connect connect;
    
    private static ChatColor DEFAULT_COLOR = ChatColor.DARK_AQUA;
    private static ChatColor DEBUG_COLOR = ChatColor.DARK_GRAY;
    
    public static final String HOST_SERVER = "host";
    public static final String CONTROLLER_REQUEST = "c_req";
    
    @Override
    public void onLoad(){
	api = this;
    }
    
    @Override
    public void onEnable(){
	
	
	try{
	    getBukkitConnect().registerEvents(LinkerServer.getInstance());
	    LinkerServer.getInstance().enable();	    
	}catch (final Throwable e){
	    _("Could not connect to Lilypad. Does it exist?");
	}
	
	
    }
    @Override
    public void onDisable(){
	API.shuttingdown = true;
    }
    
    
    
    public static API getPlugin(){
	return api;
    }
    
    
    public static void setDefaultColor(ChatColor color){
	DEFAULT_COLOR = color;
    }
    
    public static void setDebugColor(ChatColor color){
	DEBUG_COLOR = color;
    }
    
    
    public static void _(String msg){
	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[MC-SG.API][Alert] "+MessageUtil.format(msg));
    }
    
    public static void _(String prefix, String msg){
	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[MC-SG.API][Alert]"+"["+prefix+"] "+MessageUtil.format(msg));
    }
    
    public static void $(String msg){
	$(DEFAULT_COLOR, msg);
    }
    
    public static void $(Plugin plugin, String msg){
	$(plugin, DEFAULT_COLOR, msg);
    }
    
    public static void $(ChatColor color, String msg){
	$(getPlugin(), color, msg);
    }
    
    public static void $(Plugin plugin, ChatColor color, String msg){
	Bukkit.getConsoleSender().sendMessage(color+"["+plugin.getName()+"] "+MessageUtil.format(msg));
    }
    
    public static void $(String prefix, String msg){
	$(DEFAULT_COLOR, prefix, msg);
    }
    
    public static void $(ChatColor color, String prefix, String msg){
	$(getPlugin(), color, prefix, msg);
    }
    
    public static void $(Plugin plugin, String prefix, String msg){
	$(plugin, DEFAULT_COLOR, prefix, msg);
    }
    
    public static void $(Plugin plugin, ChatColor color, String prefix, String msg){
	Bukkit.getConsoleSender().sendMessage(color+"["+plugin.getName()+"]"+"["+prefix+"] " + MessageUtil.format(msg));
	
    }
    
    public static void setDebugEnabled(boolean b){
	debug = b;
    }
    
    public static void debug(String prefix, String msg) {
	if(debug){
	    $(DEBUG_COLOR,"Debug]["+prefix , msg);
	}
    }
    
    public static void debug(String msg){
	if(debug){
	    $(DEBUG_COLOR,"debug", msg);
	}
    }
    
    public static Connect getBukkitConnect(){
	if(connect == null){
	    connect = Bukkit.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
	}
	return connect;
    }
    
    public static void fireEvent(Event e){
	if(!shuttingdown){
	    Bukkit.getServer().getPluginManager().callEvent(e);
	}
    }
    
    public static boolean isShuttingDown(){
	return shuttingdown;
    }
    
    
    public static String getServerName() {
	return getPlugin().getServer().getServerName();
    }
    
    public static String getServerId(){
	return getBukkitConnect().getSettings().getUsername();
    }
}

