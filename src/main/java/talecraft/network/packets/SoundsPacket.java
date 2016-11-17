package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.TCSoundHandler.SoundEnum;
import talecraft.client.sound.ConstantSound;

public class SoundsPacket implements IMessage {

	boolean mute;
	boolean repeat;
	boolean constant;
	int delay;
	SoundEnum sound;
	
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
			Minecraft mc = Minecraft.getMinecraft();
			if(message.mute){
				mc.getSoundHandler().stopSounds();
			}else{
				EntityPlayer player = mc.thePlayer;
				
				ISound record = null;
				
				ResourceLocation soundName = message.sound.getSoundEvent().getSoundName();
				SoundCategory category = SoundCategory.MUSIC;
				boolean repeat = message.repeat;
				int delay = message.delay;
				AttenuationType attenuation = message.constant ? AttenuationType.NONE : AttenuationType.LINEAR;
				
				if(message.constant) {
					ConstantSound c = new ConstantSound(soundName);
					record = c;
					
					if(repeat)
						c.setRepeating(delay);
					else
						c.setNonRepeating();
					
					c.setVolume(1);
					c.setPitch(1);
				} else {
					record = new PositionedSoundRecord(
							soundName, category,
							1F, 1F,
							repeat, delay, attenuation,
							(float)player.posX, (float)player.posY, (float)player.posZ
					);
				}
				
				
				mc.getSoundHandler().playSound(record);
			}
			return null;
		}
	}
}
