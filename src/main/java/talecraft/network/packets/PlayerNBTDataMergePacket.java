package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.network.handlers.client.PlayerNBTDataMergePacketHandler;

public class PlayerNBTDataMergePacket implements IMessage {

	public NBTTagCompound data;

	public PlayerNBTDataMergePacket() {
		data = new NBTTagCompound();
	}

	public PlayerNBTDataMergePacket(NBTTagCompound in) {
		data = in;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public static class Handler implements IMessageHandler<PlayerNBTDataMergePacket, IMessage> {

		@Override
		public IMessage onMessage(PlayerNBTDataMergePacket message, MessageContext ctx) {
			PlayerNBTDataMergePacketHandler.handle(message);
			return null;
		}
	}
}
