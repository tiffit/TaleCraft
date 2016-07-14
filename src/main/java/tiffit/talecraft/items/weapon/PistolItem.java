package tiffit.talecraft.items.weapon;

import de.longor.talecraft.TCSoundHandler;
import de.longor.talecraft.TaleCraftItems;
import net.minecraft.util.SoundEvent;

public class PistolItem extends TCGunItem {
	
	@Override
	protected TCGunClipItem getClip() {
		return TaleCraftItems.pistolClip;
	}

	@Override
	protected float getDamage() {
		return 3F;
	}

	@Override
	protected double range() {
		return 30D;
	}

	@Override
	protected SoundEvent fireSound() {
		return TCSoundHandler.PistolFire;
	}

	@Override
	protected int fireSpeed() {
		return 100;
	}
	
	@Override
	protected boolean isPistol() {
		return true;
	}

}
