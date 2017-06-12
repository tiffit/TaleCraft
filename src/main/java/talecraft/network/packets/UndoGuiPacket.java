package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.network.handlers.client.UndoGuiPacketHandler;

public class UndoGuiPacket implements IMessage {
	
	public NBTTagCompound tag;
	
	public UndoGuiPacket() {
	}
	
	public UndoGuiPacket(NBTTagCompound tag){
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
	
	public static class Handler implements IMessageHandler<UndoGuiPacket, IMessage> {
		@Override
		public IMessage onMessage(UndoGuiPacket message, MessageContext ctx) {
			UndoGuiPacketHandler.handle(message);
			return null;
		}
	}


}
