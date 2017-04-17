package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import talecraft.client.gui.misc.GuiUndo;
import talecraft.network.packets.UndoGuiPacket;

public class UndoGuiPacketHandler{
	
	public static void handle(UndoGuiPacket message){
		Minecraft.getMinecraft().displayGuiScreen(new GuiUndo(message.tag));
	}
}
