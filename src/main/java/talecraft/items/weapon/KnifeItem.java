package talecraft.items.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import talecraft.entity.projectile.EntityKnife;

public class KnifeItem extends TCWeaponItem {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand){
		ItemStack stack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode){
            stack.shrink(1);;
        }

        worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote){
            EntityKnife knife = new EntityKnife(worldIn, player);
            knife.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.7F, 1F);
            worldIn.spawnEntity(knife);
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
	
}
