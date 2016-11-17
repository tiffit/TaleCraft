package talecraft.items.weapon;

import net.minecraft.util.SoundEvent;
import talecraft.TCSoundHandler;
import talecraft.TaleCraftItems;

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
