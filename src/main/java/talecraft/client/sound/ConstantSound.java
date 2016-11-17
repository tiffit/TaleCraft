package talecraft.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class ConstantSound implements ISound {
	private ResourceLocation soundLocation;
	private SoundEventAccessor soundEvent;
	private Sound sound;
	
	private float pitch;
	private float volume;
	
	private boolean repeating;
	private int repeatDelay;
	
	public ConstantSound(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
		this.soundEvent = null;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setNonRepeating() {
		repeating = false;
		repeatDelay = 100;
	}
	
	public void setRepeating(int repeatingDelay) {
		this.repeating = true;
		this.repeatDelay = repeatingDelay;
	}
	
	@Override
	public boolean canRepeat() {
		return repeating;
	}

	@Override
	public SoundEventAccessor createAccessor(SoundHandler arg0) {
    this.soundEvent = arg0.getAccessor(this.getSoundLocation());
    this.sound = this.soundEvent == null ? SoundHandler.MISSING_SOUND : this.soundEvent.cloneEntry();
    return this.soundEvent;
	}

	@Override
	public AttenuationType getAttenuationType() {
		return AttenuationType.NONE;
	}

	@Override
	public SoundCategory getCategory() {
		return SoundCategory.MUSIC;
	}

	@Override
	public float getPitch() {
		return pitch;
	}

	@Override
	public int getRepeatDelay() {
		return repeatDelay;
	}

	@Override
	public Sound getSound() {
		return sound;
	}

	@Override
	public ResourceLocation getSoundLocation() {
		return soundLocation;
	}

	@Override
	public float getVolume() {
		return volume;
	}

	@Override
	public float getXPosF() {
		return 0; // no op
	}

	@Override
	public float getYPosF() {
		return 0; // no op
	}

	@Override
	public float getZPosF() {
		return 0; // no op
	}
	
}
