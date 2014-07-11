package org.mcsg.api.lilypad;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mcsg.api.API;
import org.mcsg.api.events.NetworkServerCommandEvent;

public class ResponseRequest implements Listener{
    
    long id = -2;
    NetworkServerCommandEvent ev;
    
    @EventHandler
    public void listen(NetworkServerCommandEvent ev){
	if(ev.getGroupId() == id){
	    this.ev = ev;
	    synchronized (this) {
		this.notify();
	    }
	}
    }
    
    
    public NetworkServerCommandEvent listen(long id){
	return listen(id, 10000);
    }
    
    public NetworkServerCommandEvent listen(long id, long timeout){
	this.id = id;
	Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
	try {
	    synchronized (this) {
		
		this.wait(timeout);
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	if(ev == null){
	    throw new RuntimeException("Timed out while waiting for response. GroupID: "+id);
	}
	
	return ev;
    }
    
    
}
