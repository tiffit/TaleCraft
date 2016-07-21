package tiffit.talecraft.packet;

import de.longor.talecraft.client.ClientFadeEffect;
import de.longor.talecraft.client.ClientRenderer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ForceF1Packet implements IMessage {

	boolean f1;
	
	public ForceF1Packet() {
	}
	
	public ForceF1Packet(boolean f1){
		this.f1 = f1;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		f1 = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(f1);
	}

	public static class Handler implements IMessageHandler<ForceF1Packet, IMessage> {

		@Override
		public IMessage onMessage(ForceF1Packet message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			if(message.f1){
				mc.gameSettings.thirdPersonView = 0;
			}
			mc.gameSettings.hideGUI = message.f1;
			return null;
		}
	}
}
