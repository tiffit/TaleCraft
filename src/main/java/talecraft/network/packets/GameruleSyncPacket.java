package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.network.handlers.client.MusicDisableGamerulePacketHandler;

public class GameruleSyncPacket implements IMessage {

	public NBTTagCompound gr;

	public GameruleSyncPacket() {
	}

	public GameruleSyncPacket(NBTTagCompound gr) {
		this.gr = gr;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		gr = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, gr);
	}

	public static class Handler implements IMessageHandler<GameruleSyncPacket, IMessage> {

		@Override
		public IMessage onMessage(GameruleSyncPacket message, MessageContext ctx) {
			MusicDisableGamerulePacketHandler.handle(message);
			return null;
		}
	}
}
