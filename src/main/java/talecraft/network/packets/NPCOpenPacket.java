package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.network.handlers.client.NPCOpenPacketHandler;

public class NPCOpenPacket implements IMessage {
	
	public int id;
	
	public NPCOpenPacket() {
	}

	public NPCOpenPacket(int id) {
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}
	
	public static class Handler implements IMessageHandler<NPCOpenPacket, IMessage> {
		@Override
		public IMessage onMessage(NPCOpenPacket message, MessageContext ctx) {
			NPCOpenPacketHandler.handle(message);
			return null;
		}
	}
}
