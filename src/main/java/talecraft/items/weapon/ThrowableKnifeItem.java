package talecraft.items.weapon;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraftItems;
import talecraft.entity.projectile.EntityBoomerang;

public class ThrowableKnifeItem extends TCWeaponItem {
	
	public ThrowableKnifeItem() {
		super();
		this.addPropertyOverride(new ResourceLocation("thrown"), new IItemPropertyGetter(){
            @Override
						@SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)	{
            	return isThrown(stack) ? 1.0F : 0.0F;
            }
        });
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return !isThrown(item);
	}
	
	@SubscribeEvent
	public void onItemDrop(ItemTossEvent event){
		if(!event.getPlayer().world.isRemote){
			if(isThrown(event.getEntityItem().getItem())){
				event.getEntityItem().getItem().getTagCompound().setBoolean("thrown", false);
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.AMBIENT, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote) {
			if(!isThrown(stack)){
				if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setBoolean("thrown", true);
				EntityBoomerang boomerang = new EntityBoomerang(world, player);
				boomerang.setItemStack(stack);
				boomerang.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
				world.spawnEntity(boomerang);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public static boolean isThrown(ItemStack stack){
		if(stack == null || stack.getItem() != TaleCraftItems.boomerang) return false;
		if(!stack.hasTagCompound()) return false;
		NBTTagCompound tag = stack.getTagCompound();
		return tag.getBoolean("thrown");
	}
	
}
