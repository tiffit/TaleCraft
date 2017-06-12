package talecraft.network.handlers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import talecraft.client.sound.ConstantSound;
import talecraft.network.packets.SoundsPacket;

public class SoundsPacketHandler{
	
	public static void handle(SoundsPacket message){
		Minecraft mc = Minecraft.getMinecraft();
		if (message.mute) {
			mc.getSoundHandler().stopSounds();
		} else {
			EntityPlayer player = mc.player;

			ISound record = null;

			ResourceLocation soundName = message.sound.getSoundEvent().getSoundName();
			SoundCategory category = SoundCategory.MUSIC;
			boolean repeat = message.repeat;
			int delay = message.delay;
			AttenuationType attenuation = message.constant ? AttenuationType.NONE : AttenuationType.LINEAR;

			if (message.constant) {
				ConstantSound c = new ConstantSound(soundName);
				record = c;

				if (repeat)
					c.setRepeating(delay);
				else
					c.setNonRepeating();

				c.setVolume(1);
				c.setPitch(1);
			} else {
				record = new PositionedSoundRecord(soundName, category, 1F, 1F, repeat, delay, attenuation,
						(float) player.posX, (float) player.posY, (float) player.posZ);
			}

			mc.getSoundHandler().playSound(record);
		}
	}
	
}
