package tiffit.talecraft.items.weapon;

import de.longor.talecraft.TCSoundHandler;
import de.longor.talecraft.TaleCraftItems;
import net.minecraft.util.SoundEvent;

public class ShotgunItem extends TCGunItem {
	
	@Override
	protected TCGunClipItem getClip() {
		return TaleCraftItems.shotgunClip;
	}

	@Override
	protected float getDamage() {
		return 10F;
	}

	@Override
	protected double range() {
		return 5D;
	}

	@Override
	protected SoundEvent fireSound() {
		return TCSoundHandler.ShotgunFire;
	}
	
	@Override
	public SoundEvent reloadSound() {
		return TCSoundHandler.ShotgunReload;
	}

	@Override
	protected int fireSpeed() {
		return 35;
	}

}
