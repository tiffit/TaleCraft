package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.TaleCraft;

public class StringNBTCommandPacketClient implements IMessage {

	public NBTTagCompound data;
	public String command;

	public StringNBTCommandPacketClient() {
		data = new NBTTagCompound();
		command = "";
	}

	public StringNBTCommandPacketClient(String cmdIN, NBTTagCompound dataIN) {
		this.data = dataIN != null ? dataIN : new NBTTagCompound();
		this.command = cmdIN;
	}

	public StringNBTCommandPacketClient(String cmd) {
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

	public static class Handler implements IMessageHandler<StringNBTCommandPacketClient, IMessage> {

		@Override
		public IMessage onMessage(StringNBTCommandPacketClient message, MessageContext ctx) {
			TaleCraft.asClient().getNetworkHandler().handleClientCommand(message.command, message.data);
			return null;
		}
	}
}
