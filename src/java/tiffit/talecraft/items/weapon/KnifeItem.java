package tiffit.talecraft.items.weapon;

import java.util.List;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import tiffit.talecraft.entity.projectile.EntityBomb;
import tiffit.talecraft.entity.projectile.EntityKnife;
import tiffit.talecraft.items.world.TCWorldItem;

public class KnifeItem extends TCWeaponItem {
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
        if (!playerIn.capabilities.isCreativeMode){
            itemStackIn.stackSize--;
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote){
            EntityKnife knife = new EntityKnife(worldIn, playerIn);
            knife.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.7F, 1F);
            worldIn.spawnEntityInWorld(knife);
        }
        
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
	
}
