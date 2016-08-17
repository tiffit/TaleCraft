package tiffit.talecraft.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoundsMutePacket implements IMessage {

	public SoundsMutePacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<SoundsMutePacket, IMessage> {

		@Override
		public IMessage onMessage(SoundsMutePacket message, MessageContext ctx) {
			Minecraft.getMinecraft().getSoundHandler().stopSounds();
			return null;
		}
	}
}
