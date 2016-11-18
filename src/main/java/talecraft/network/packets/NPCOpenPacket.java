package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.client.gui.npc.NPCEditorGui;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.NPCData;

public class NPCOpenPacket implements IMessage {
	
	int id;
	

	public NPCOpenPacket() {
	}

	public NPCOpenPacket(int id) {
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<NPCOpenPacket, IMessage> {

		@Override
		public IMessage onMessage(NPCOpenPacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityNPC npc = (EntityNPC) mc.theWorld.getEntityByID(message.id);
			EntityNPC newNPC = new EntityNPC(npc.worldObj);
			mc.displayGuiScreen(new NPCEditorGui(npc.getUniqueID(), NPCData.fromNBT(newNPC, npc.getNPCData().toNBT())));
			return null;
		}
	}
}
