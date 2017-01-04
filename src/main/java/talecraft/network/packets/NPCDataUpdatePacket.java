package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.entity.NPC.EntityNPC;

public class NPCDataUpdatePacket implements IMessage {

	NBTTagCompound data;
	int id;
	

	public NPCDataUpdatePacket() {
	}

	public NPCDataUpdatePacket(int id, NBTTagCompound tag) {
		data = tag;
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<NPCDataUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(NPCDataUpdatePacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			
			EntityNPC npc = (EntityNPC) mc.world.getEntityByID(message.id);
			npc.setNPCData(message.data);
			return null;
		}
	}
}
