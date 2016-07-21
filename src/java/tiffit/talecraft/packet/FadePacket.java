package tiffit.talecraft.packet;

import de.longor.talecraft.client.ClientFadeEffect;
import de.longor.talecraft.client.ClientRenderer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FadePacket implements IMessage {

	int color;
	int time;
	
	public FadePacket() {
	}
	
	public FadePacket(int color, int time){
		this.color = color;
		this.time = time;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		color = buf.readInt();
		time = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(color);
		buf.writeInt(time);
	}

	public static class Handler implements IMessageHandler<FadePacket, IMessage> {

		@Override
		public IMessage onMessage(FadePacket message, MessageContext ctx) {
			ClientRenderer.fadeEffect = new ClientFadeEffect(message.color, message.time);
			return null;
		}
	}
}
