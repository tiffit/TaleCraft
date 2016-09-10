package tiffit.talecraft.packet;

import de.longor.talecraft.TCSoundHandler.SoundEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoundsPacket implements IMessage {

	boolean mute;
	boolean repeat;
	int delay;
	SoundEnum sound;
	
	public SoundsPacket() {
		mute = true;
	}
	
	public SoundsPacket(SoundEnum sound, boolean repeat, int delay) {
		mute = false;
		this.sound = sound;
		this.repeat = repeat;
		this.delay = delay;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if(!(mute = buf.readBoolean())){
			sound = SoundEnum.values()[buf.readInt()];
			repeat = buf.readBoolean();
			delay = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(mute);
		if(!mute){
			buf.writeInt(sound.ordinal());
			buf.writeBoolean(repeat);
			buf.writeInt(delay);
		}
	}

	public static class Handler implements IMessageHandler<SoundsPacket, IMessage> {

		@Override
		public IMessage onMessage(SoundsPacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			if(message.mute){
				mc.getSoundHandler().stopSounds();
			}else{
				EntityPlayer player = mc.thePlayer;
				PositionedSoundRecord record = new PositionedSoundRecord(message.sound.getSoundEvent().getSoundName(), SoundCategory.MUSIC, 1F, 1F, message.repeat, message.delay, ISound.AttenuationType.LINEAR, (float)player.posX, (float)player.posY, (float)player.posZ);
				mc.getSoundHandler().playSound(record);
			}
			return null;
		}
	}
}
