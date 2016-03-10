package de.longor.talecraft.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class ServerMirror {
	private MinecraftServer server;
	private PlayerList players;
	private ServerClipboard clipboard;
	private ServerFileSystem fileSystem;
	private boolean trackInvokes;
	
	public MinecraftServer getServer() {
		return server;
	}
	
	public void create(MinecraftServer server) {
		TaleCraft.logger.info("Creating Server Mirror: " + server);
		
		this.server = server;
		this.players = new PlayerList();
		this.clipboard = new ServerClipboard();
		this.fileSystem = new ServerFileSystem();
		this.trackInvokes = true;
	}
	
	public void destroy() {
		TaleCraft.logger.info("Destroying Server Mirror: " + server);
		this.players.destroy();
	}
	
	public PlayerList playerList() {
		return players;
	}
	
	public ServerClipboard getClipboard() {
		return clipboard;
	}

	public ServerFileSystem getFileSystem() {
		return fileSystem;
	}
	
	public static ServerMirror instance() {
		return ServerHandler.getServerMirror(null);
	}
	
	public void trackInvoke(IInvokeSource source, IInvoke invoke) {
		if(!trackInvokes) return;
		
		for(PlayerMirror playerMirror : players.getBackingList()) {
			playerMirror.trackInvoke(source, invoke);
		}
	}
	
}
