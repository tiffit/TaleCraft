package de.longor.talecraft.client.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.proxy.ClientProxy;

public class PlayerDataMergeMessageHandler implements IMessageHandler {
	
	@Override public IMessage onMessage(final IMessage message, MessageContext ctx) {
		if(message instanceof PlayerNBTDataMerge) {
			final ClientProxy cproxy = TaleCraft.proxy.asClient();
			cproxy.sheduleClientTickTask(new Runnable(){
				Minecraft micr = cproxy.mc;
				PlayerNBTDataMerge mpakDataMerge = (PlayerNBTDataMerge) message;
				@Override public void run() {
					if(micr.thePlayer != null) {
						micr.thePlayer.getEntityData().merge((mpakDataMerge.data));
					}
				}
			});
		}
		return null;
	}
	
}
