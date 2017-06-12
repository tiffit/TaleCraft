package talecraft.network.packets;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.decorator.Decoration;
import talecraft.network.handlers.client.DecoratorGuiPacketHandler;

public class DecoratorGuiPacket implements IMessage {
	public NBTTagCompound tag;
	
	public DecoratorGuiPacket() {
	}
	
	public DecoratorGuiPacket(List<Decoration> decor, NBTTagCompound tag){
		NBTTagList tl = new NBTTagList();
		for(Decoration d : decor){
			tl.appendTag(new NBTTagString(d.name()));
		}
		tag.setTag("decorations_list", tl);
		this.tag = tag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class Handler implements IMessageHandler<DecoratorGuiPacket, IMessage> {
		
		@Override
		public IMessage onMessage(DecoratorGuiPacket message, MessageContext ctx) {
			DecoratorGuiPacketHandler.handle(message);
			return null;
		}
	}
}
