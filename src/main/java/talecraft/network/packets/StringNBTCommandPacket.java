package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.server.ServerHandler;

public class StringNBTCommandPacket implements IMessage {

	public NBTTagCompound data;
	public String command;

	public StringNBTCommandPacket() {
		data = new NBTTagCompound();
		command = "";
	}

	public StringNBTCommandPacket(String cmdIN, NBTTagCompound dataIN) {
		this.data = dataIN != null ? dataIN : new NBTTagCompound();
		this.command = cmdIN;
	}

	public StringNBTCommandPacket(String cmd) {
		data = new NBTTagCompound();
		command = cmd;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		command = ByteBufUtils.readUTF8String(buf);
		if(!buf.readBoolean()){
			data = ByteBufUtils.readTag(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, command);
		buf.writeBoolean(data == null);
		ByteBufUtils.writeTag(buf, data);
	}

	public static class Handler implements IMessageHandler<StringNBTCommandPacket, IMessage> {

		@Override
		public IMessage onMessage(StringNBTCommandPacket message, MessageContext ctx) {
			ServerHandler.handleSNBTCommand(ctx.getServerHandler(), message);
			return null;
		}
	}
}
