package de.longor.talecraft.script.wrappers.entity;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.potion.PotionEffectObjectWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.PotionEffect;

public class EntityLivingObjectWrapper extends EntityObjectWrapper implements IObjectWrapper {
	private final EntityLiving entity;
	
	public EntityLivingObjectWrapper(EntityLiving entity) {
		super(entity);
		this.entity = entity;
	}
	
	public EntityLiving internal() {
		return entity;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public void navigateTo(double x, double y, double z, double speed) {
		entity.getNavigator().tryMoveToXYZ(x, y, z, speed);
	}
	
	public void navigateTo(EntityObjectWrapper entity2, double speed) {
		entity.getNavigator().tryMoveToEntityLiving(entity2.internal(), speed);
	}
	
	public void setHealth(float health) {
		entity.setHealth(health);;
	}
	
	public float setHealth() {
		return entity.getHealth();
	}
	
	public void addPotionEffect(PotionEffectObjectWrapper potionEffect) {
		entity.addPotionEffect(potionEffect.internal());
	}
	
	
	
}
