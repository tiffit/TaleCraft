package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.TaleCraftSounds.SoundEnum;
import talecraft.network.handlers.client.SoundsPacketHandler;

public class SoundsPacket implements IMessage {

	public boolean mute;
	public boolean repeat;
	public boolean constant;
	public int delay;
	public SoundEnum sound;
	
	public SoundsPacket() {
		mute = true;
	}
	
	public SoundsPacket(SoundEnum sound, boolean repeat, int delay, boolean constant) {
		mute = false;
		this.sound = sound;
		this.repeat = repeat;
		this.delay = delay;
		this.constant = constant;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if(!(mute = buf.readBoolean())){
			sound = SoundEnum.values()[buf.readInt()];
			repeat = buf.readBoolean();
			delay = buf.readInt();
			constant = buf.readBoolean();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(mute);
		if(!mute){
			buf.writeInt(sound.ordinal());
			buf.writeBoolean(repeat);
			buf.writeInt(delay);
			buf.writeBoolean(constant);
		}
	}

	public static class Handler implements IMessageHandler<SoundsPacket, IMessage> {

		@Override
		public IMessage onMessage(SoundsPacket message, MessageContext ctx) {
			SoundsPacketHandler.handle(message);
			return null;
		}
	}
}
