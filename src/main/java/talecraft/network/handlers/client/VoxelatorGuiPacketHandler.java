package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.client.gui.items.voxelator.GuiVoxelator;
import talecraft.network.packets.VoxelatorGuiPacket;

public class VoxelatorGuiPacketHandler{
	
	public static void handle(VoxelatorGuiPacket message){
		Minecraft.getMinecraft().displayGuiScreen(new GuiVoxelator(message.tag));
	}
}
