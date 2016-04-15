package tiffit.talecraft.packet;

import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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

public class NPCClientUpdatePacket implements IMessage {

	NBTTagCompound data;
	int entID;

	public NPCClientUpdatePacket() {
	}

	public NPCClientUpdatePacket(int id, NBTTagCompound tag) {
		data = tag;
		entID = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		entID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		buf.writeInt(entID);
	}

	public static class Handler implements IMessageHandler<NPCClientUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(NPCClientUpdatePacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityNPC npc = (EntityNPC) Minecraft.getMinecraft().theWorld.getEntityByID(message.entID);
			npc.setNPCData(message.data);
			return null;
		}
	}
}
