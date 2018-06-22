package talecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import talecraft.TaleCraftSounds;
import talecraft.TaleCraftItems;

public class RifleItem extends TCGunItem {
	
	@Override
	protected TCGunClipItem getClip() {
		return TaleCraftItems.rifleClip;
	}

	@Override
	protected float getDamage() {
		return 3F;
	}

	@Override
	protected double range() {
		return 100D;
	}

	@Override
	protected SoundEvent fireSound() {
		return TaleCraftSounds.RifleFire;
	}

	@Override
	protected int fireSpeed() {
		return 0;
	}

}
