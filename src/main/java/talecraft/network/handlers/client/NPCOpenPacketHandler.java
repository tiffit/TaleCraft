package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import talecraft.client.gui.entity.npc.NPCEditorGui;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.NPCData;
import talecraft.network.packets.NPCOpenPacket;

public class NPCOpenPacketHandler{
	
	public static void handle(NPCOpenPacket message) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityNPC npc = (EntityNPC) mc.theWorld.getEntityByID(message.id);
		EntityNPC newNPC = new EntityNPC(npc.worldObj);
		mc.displayGuiScreen(new NPCEditorGui(npc.getUniqueID(), NPCData.fromNBT(newNPC, npc.getNPCData().toNBT())));
	}
	
}
