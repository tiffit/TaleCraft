package talecraft.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InstaKillItem extends TCItem {

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		target.setHealth(0);
		target.isDead = true;
		return false;
	}

	@Override
	// Warning: Forge Method
	// Override the method override from TCItem class.
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return false;
	}

}
