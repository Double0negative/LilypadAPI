package org.mcsg.api.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mcsg.api.lilypad.LinkerServer;
import org.mcsg.api.util.MapWrapper;

import com.google.gson.Gson;

public @Data class NetworkServerCommandEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private static final Gson gson = new Gson();
	
	private String json;
	private String id;
	private String server;
	private String channel;
	private String command;

	public NetworkServerCommandEvent(String server, String id, String channel, String command, String json){
		this.json = json;
		this.id = id;
		this.command = command;
		this.channel = channel;
		this.server = server;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getServer(){
		return server;
	}

	public String getChannel(){
		return channel;
	}

	public String getCommand(){
		return command;
	}

	@SuppressWarnings("unchecked")
        public <T> T getData(Class<?> type){
		return  (T) gson.fromJson(json, type);
	}
	
	public int getGroupId(){
		return Integer.parseInt(id);
	}

	@SuppressWarnings("unchecked")
        public List<String> getDataAsList(){
		return (List<String>) getData(List.class);
	}

	@SuppressWarnings("unchecked")
        public MapWrapper getDataAsMap(){
		Map<String, String>map = new HashMap<String, String>();
		return new MapWrapper((Map<String, String>) getData(map.getClass()));
	}
	
	public void respond(Object data){
		LinkerServer.getInstance().sendCommand(server, getGroupId(), channel, command, data);
	}
}