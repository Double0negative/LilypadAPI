package org.mcsg.api.lilypad;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;

import org.mcsg.api.API;
import org.mcsg.api.events.NetworkServerCommandEvent;

import com.google.gson.Gson;

public class LinkerServer{
    
    private static LinkerServer instance = new LinkerServer();
    public static final String SPLIT_CHAR = "\0!";
    public static final String PREFIX = "mcsg";
    
    private boolean debug = false;
    private HashSet<String> chans = new HashSet<String>();
    
    private ArrayList<CommandData> queue = new ArrayList<CommandData>();
    private Thread queueExecuter;
    
    private Gson gson;
    private boolean enabled = false;
    private long id = 0;
    
    private long getId(){
	return ++id;
    }
    
    public void enable(){
	this.enabled = true;
    }
    
    private void setId(long id){
	this.id = id;
    }
    
    private LinkerServer(){
	this.gson = new Gson();
    }
    
    public static LinkerServer getInstance(){
	return instance;
    }
    
    @EventListener
    public void onMessage(MessageEvent e) {
	try{
	    String[] request = e.getMessageAsString().split(SPLIT_CHAR);
	    if(request.length > 2){
		String server = e.getSender();
		String command = request[0];
		String id = request[1];
		
		String channel = e.getChannel();
		String json = request[2];
		
		if (debug && (chans.size() == 0 || chans.contains(channel))) {
		    API.debug("LinkerServer", "&c<- &8Received command &2"+id+":"+command+"&8 from &2"+e.getSender()+".&8 args ["+json + "] on &2"+channel);
		}
		
		NetworkServerCommandEvent ev = new NetworkServerCommandEvent(server, id, channel, command, json);
		API.fireEvent(ev);
		
		if(ev.getGroupId() > this.id){
		    setId(ev.getGroupId());
		}
		
	    }
	} catch (Exception ee){
	    try {
		API.debug("LinkerServer", e.getMessageAsString());
	    } catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	}
    }
    
    public boolean redirectRequest(String server, String player, int attempts) {
	try {
	    Connect c = API.getBukkitConnect();
	    c.request(new RedirectRequest(server, player));
	    return true;
	} catch (Exception exception) {
	    if(attempts > 0){
		return redirectRequest(server, player, attempts--);
	    }
	    else{
		return false;
	    }
	}
    }
    
    public long sendCommand(CommandData data){
	return sendCommand(data.server, data.id, data.channel, data.command, data.json);
    }
    
    public  long sendCommand(String server, String channel, String command){
	return sendCommand(getList(server), getId(), channel, command, new Object());
    }
    
    
    
    public  long sendCommand(String server, String channel, String command, Object data){
	return sendCommand(getList(server), getId(), channel, command, data);
    }
    
    public  long sendCommand(String server, long id, String channel, String command, Object data){
	return sendCommand(getList(server), id, channel, command, data);
    }
    
    public  long sendCommand(List<String> server, String channel, String command, Object data){
	return sendCommand(server, getId(), channel, command, data);
    }
    
    protected  long sendCommand(List<String> server, long id, String channel, String command, Object data){
	if(!enabled) return 0;
	String json = gson.toJson(data);
	if(!API.getBukkitConnect().isConnected()){
	    queue.add(new CommandData(server, id, channel, command, json));
	    startQueue();
	    return id;
	}
	StringBuilder sb = new StringBuilder();
	sb.append(command);
	sb.append(SPLIT_CHAR);
	sb.append(id);
	sb.append(SPLIT_CHAR);
	sb.append(json);
	
	String message = sb.toString();
	
	try {
	    if (debug && (chans.size() == 0 || chans.contains(channel))) 
		API.debug("LinkerServer", "&a-> &8Dispatching command [&2"+id+":"+message+"&8] to &2"+((server.size() == 0)? "All" : server)+"&8 on &2"+channel);
	    API.getBukkitConnect().request(new MessageRequest(server, channel, message));
	    return id;
	} catch (UnsupportedEncodingException | RequestException e){
	    e.printStackTrace();
	    return -1;
	}
    }
    
    /**
     * Delay commands until Connect is connected
     */
    public void startQueue(){
	if(queueExecuter == null){
	    queueExecuter = new Thread(){
		public void run(){
		    while(!API.getBukkitConnect().isConnected()){
			try{ Thread.sleep(1000); } catch (Exception e){}
		    }
		    for(CommandData data : queue){
			sendCommand(data);
		    }
		    queue.clear();
		}
	    };
	    queueExecuter.start();
	}
    }
    
    private List<String> getList(String str){
	List<String> list = new ArrayList<String>();
	if(str != null && !str.equals(""))
	    list.add(str);
	return list;
    }
    
    public void enableDebug(String ... chan){
	if(chan.length != 0){
	    for(String c : chan){
		chans.add(c);
	    }
	}
	debug = true;
    }
    
    public void disableDebug(){
	chans.clear();
	debug = false;
    }
    
    
    class CommandData{
	List<String> server;
	long id;
	String channel;
	String command;
	String json;
	
	public CommandData(List<String> server, long id, String channel, String command, String json){
	    this.server = server;
	    this.id = id;
	    this.channel = channel;
	    this.command = command;
	    this.json = json;
	}
    }
}

