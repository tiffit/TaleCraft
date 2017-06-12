package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import talecraft.TaleCraft;
import talecraft.network.packets.PlayerNBTDataMergePacket;
import talecraft.proxy.ClientProxy;

public class PlayerNBTDataMergePacketHandler{
	
	public static void handle(PlayerNBTDataMergePacket message){
		final ClientProxy cproxy = TaleCraft.proxy.asClient();
		final PlayerNBTDataMergePacket mpakDataMerge = message;
		cproxy.sheduleClientTickTask(new Runnable(){
			Minecraft micr = ClientProxy.mc;
			@Override public void run() {
				if(micr.player != null) {
					micr.player.getEntityData().merge((mpakDataMerge.data));
				}
			}
		});
	}
}
