package talecraft.network.handlers.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.TaleCraft;
import talecraft.client.gui.items.GuiDecorator;
import talecraft.network.packets.DecoratorGuiPacket;
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
