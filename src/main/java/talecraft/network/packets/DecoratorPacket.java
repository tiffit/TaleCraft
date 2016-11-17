package talecraft.network.packets;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.TaleCraft;
import talecraft.TaleCraftItems;

public class DecoratorPacket implements IMessage {

	UUID uuid;
	int xoff;
	int yoff;
	int zoff;
	String decoration;
	int amount;
	int radius;

	public DecoratorPacket() {
	}

	public DecoratorPacket(UUID uuid, int xoff, int yoff, int zoff, String decoration, int amount, int radius) {
		this.uuid = uuid;
		this.xoff = xoff;
		this.yoff = yoff;
		this.zoff = zoff;
		this.decoration = decoration;
		this.amount = amount;
		this.radius = radius;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xoff = buf.readInt();
		yoff = buf.readInt();
		zoff = buf.readInt();
		decoration = ByteBufUtils.readUTF8String(buf);
		amount = buf.readInt();
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		radius = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xoff);
		buf.writeInt(yoff);
		buf.writeInt(zoff);
		ByteBufUtils.writeUTF8String(buf, decoration);
		buf.writeInt(amount);
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
		buf.writeInt(radius);
	}

	public static class Handler implements IMessageHandler<DecoratorPacket, IMessage> {

		@Override
		public IMessage onMessage(DecoratorPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.uuid);
			
			ItemStack item = player.inventory.getCurrentItem();
			if(item.getItem() == TaleCraftItems.decorator){
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("xoff", message.xoff);
				tag.setInteger("yoff", message.yoff);
				tag.setInteger("zoff", message.zoff);
				tag.setString("decor", message.decoration);
				tag.setInteger("amount", message.amount);
				tag.setInteger("radius", message.radius);
				item.getTagCompound().setTag("decorator_data", tag);
			}else TaleCraft.logger.error("Currently Held Item Is Not A Decorator");
			return null;
		}
	}
}
