package tiffit.talecraft.items.world;

import java.util.List;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import tiffit.talecraft.entity.throwable.EntityBomb;

public class BombItem extends TCItem {

	public BombItem(){
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftWorldTab);
	}
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
        if (!playerIn.capabilities.isCreativeMode){
            itemStackIn.stackSize--;
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.entity_snowball_throw, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote){
            EntityBomb bomb = new EntityBomb(worldIn, playerIn);
            bomb.func_184538_a(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntityInWorld(bomb);
        }
        
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
	
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv){
		if(player.isCreative()){
			list.add("This item is buggy, but it works!");
		}
	}
	
}
