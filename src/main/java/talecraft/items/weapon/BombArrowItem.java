package talecraft.items.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import talecraft.TaleCraftTabs;
import talecraft.entity.projectile.EntityBombArrow;

public class BombArrowItem extends ItemArrow {

	public BombArrowItem() {
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftWeaponTab);
	}

    @Override
		public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter){
        return new EntityBombArrow(worldIn, shooter);
    }

}
