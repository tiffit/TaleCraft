package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import talecraft.client.gui.items.voxelator.GuiVoxelator;
import talecraft.network.packets.VoxelatorGuiPacket;

public class VoxelatorGuiPacketHandler{
	
	public static void handle(VoxelatorGuiPacket message){
		Minecraft.getMinecraft().displayGuiScreen(new GuiVoxelator(message.tag));
	}
}
