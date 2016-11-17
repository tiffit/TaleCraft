package talecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import talecraft.TCSoundHandler;
import talecraft.TaleCraftItems;

public class PistolItem extends TCGunItem {
	
	@Override
	protected TCGunClipItem getClip() {
		return TaleCraftItems.pistolClip;
	}

	@Override
	protected float getDamage() {
		return 4F;
	}

	@Override
	protected double range() {
		return 60D;
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
