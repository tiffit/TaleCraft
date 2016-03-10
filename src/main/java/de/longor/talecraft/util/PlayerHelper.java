package de.longor.talecraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerHelper {
	
	public static final boolean isOp(EntityPlayerMP player) {
		MinecraftServer server = MinecraftServer.getServer();
		
		if(server.isSinglePlayer()) {
			return true;
		}
		
		String name = player.getName();
		String[] listOps = server.getConfigurationManager().getOppedPlayerNames();
		
		for(String string : listOps) {
			if(string.equals(name))
				return true;
		}
		
		return false;
	}
	
}
