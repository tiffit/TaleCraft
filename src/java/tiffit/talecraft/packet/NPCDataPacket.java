package tiffit.talecraft.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.entity.NPC.EntityNPC;

public class NPCDataPacket implements IMessage {

	NBTTagCompound data;
	String interact;
	String update;
	String death;
	UUID uuid;
	

	public NPCDataPacket() {
	}

	public NPCDataPacket(UUID uuid, NBTTagCompound tag, String interact, String update, String death) {
		data = tag;
		this.interact = interact;
		this.update = update;
		this.death = death;
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		interact = ByteBufUtils.readUTF8String(buf);
		update = ByteBufUtils.readUTF8String(buf);
		death = ByteBufUtils.readUTF8String(buf);
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		ByteBufUtils.writeUTF8String(buf, interact);
		ByteBufUtils.writeUTF8String(buf, update);
		ByteBufUtils.writeUTF8String(buf, death);
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<NPCDataPacket, IMessage> {

		@Override
		public IMessage onMessage(NPCDataPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityNPC npc = (EntityNPC) server.getEntityFromUuid(message.uuid);
			npc.setNPCData(message.data);
			npc.setScriptInteractName((message.interact));
			npc.setScriptUpdateName(message.update);
			npc.setScriptDeathName(message.death);
			return null;
		}
	}
}
