package tiffit.talecraft.items.weapon;

import de.longor.talecraft.TCSoundHandler;
import de.longor.talecraft.TaleCraftItems;
import net.minecraft.util.SoundEvent;

public class RifleItem extends TCGunItem {
	
	@Override
	protected TCGunClipItem getClip() {
		return TaleCraftItems.rifleClip;
	}

	@Override
	protected float getDamage() {
		return 2F;
	}

	@Override
	protected double range() {
		return 60D;
	}

	@Override
	protected SoundEvent fireSound() {
		return TCSoundHandler.RifleFire;
	}

	@Override
	protected int fireSpeed() {
		return 0;
	}

}
