package tiffit.talecraft.packet;

import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;
import tiffit.talecraft.entity.NPC.EntityNPC;

public class NPCDataPacket implements IMessage {

	NBTTagCompound data;
	String script;
	UUID uuid;
	

	public NPCDataPacket() {
	}

	public NPCDataPacket(UUID uuid, NBTTagCompound tag, String script) {
		data = tag;
		this.script = script;
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		script = ByteBufUtils.readUTF8String(buf);
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		ByteBufUtils.writeUTF8String(buf, script);
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<NPCDataPacket, IMessage> {

		@Override
		public IMessage onMessage(NPCDataPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityNPC npc = (EntityNPC) server.getEntityFromUuid(message.uuid);
			npc.setNPCData(message.data);
			npc.setScriptName(message.script);
			return null;
		}
	}
}
