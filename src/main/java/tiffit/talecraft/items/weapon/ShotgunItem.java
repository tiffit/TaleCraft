package tiffit.talecraft.items.weapon;

import de.longor.talecraft.TCSoundHandler;
import de.longor.talecraft.TaleCraftItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import tiffit.talecraft.entity.projectile.EntityBullet;

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
		return 2D;
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
