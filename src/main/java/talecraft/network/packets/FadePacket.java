package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.client.ClientFadeEffect;
import talecraft.client.ClientRenderer;

public class FadePacket implements IMessage {

	int color;
	int time;
	String texture;
	
	public FadePacket() {
	}
	
	public FadePacket(int color, int time, String tex){
		this.color = color;
		this.time = time;
		this.texture = tex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		color = buf.readInt();
		time = buf.readInt();
		
		int len = buf.readShort();
		if(len != 0) {
			char[] c = new char[len];
			for(int i = 0; i < len; i++) {
				c[i] = buf.readChar();
			}
			texture = new String(c);
		} else texture = null;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(color);
		buf.writeInt(time);
		
		if(texture == null) {
			buf.writeByte(0);
		} else {
			int len = texture.length();
			buf.writeShort(len);
			for(int i = 0; i < len; i++) {
				buf.writeChar(texture.charAt(i));
			}
		}
	}

	public static class Handler implements IMessageHandler<FadePacket, IMessage> {

		@Override
		public IMessage onMessage(FadePacket message, MessageContext ctx) {
			ClientRenderer.fadeEffect = new ClientFadeEffect(message.color, message.time, message.texture);
			return null;
		}
	}
}
