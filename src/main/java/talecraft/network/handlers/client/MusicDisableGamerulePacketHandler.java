package talecraft.network.handlers.client;

import talecraft.TaleCraft;
import talecraft.network.packets.GameruleSyncPacket;

public class MusicDisableGamerulePacketHandler{
	
	public static void handle(GameruleSyncPacket message){
		TaleCraft.proxy.asClient().gamerules.readFromNBT(message.gr);
	}
	
}
