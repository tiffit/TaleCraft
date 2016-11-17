package talecraft.script.wrappers.entity;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import talecraft.TaleCraft;
import talecraft.script.wrappers.potion.PotionEffectObjectWrapper;

public class EntityLivingObjectWrapper extends EntityObjectWrapper {
	private final EntityLiving entity;

	public EntityLivingObjectWrapper(EntityLiving entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public EntityLiving internal() {
		return entity;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public void navigateTo(double x, double y, double z) {
		entity.getNavigator().tryMoveToXYZ(x, y, z, 0.5D);
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

	public float getHealth() {
		return entity.getHealth();
	}

	public void addPotionEffect(PotionEffectObjectWrapper potionEffect) {
		entity.addPotionEffect(potionEffect.internal());
	}
	
	public void clearActivePotions(){
		entity.clearActivePotions();
	}
	
	public boolean canBeHitWithPotion(){
		return entity.canBeHitWithPotion();
	}
	
	public boolean canBePushed(){
		return entity.canBePushed();
	}
	
	



}
