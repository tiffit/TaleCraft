package de.longor.talecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerList {
	private Map<EntityPlayerMP, PlayerMirror> players;
	
	public PlayerList() {
		this.players = new HashMap<EntityPlayerMP, PlayerMirror>();
	}
	
	public void destroy() {
		this.players.clear();
	}
	
	public void playerJoin(EntityPlayerMP player) {
		PlayerMirror mirror = new PlayerMirror(player);
		players.put(player, mirror);
	}
	
	public void playerLeave(EntityPlayerMP player) {
		players.remove(player);
	}
	
	/**
	 * @return The mirror for the given player, or NULL if the player does not exist.
	 **/
	public PlayerMirror getPlayer(EntityPlayerMP player) {
		return players.get(player);
	}
	
	public Collection<PlayerMirror> getBackingList() {
		return players.values();
	}
	
}
