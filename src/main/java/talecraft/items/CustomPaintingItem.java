package talecraft.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CustomPaintingItem extends TCItem implements TCITriggerableItem{
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		BlockPos blockpos = pos.offset(facing);
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, stack)){
			EntityPainting painting = new EntityPainting(worldIn, blockpos, facing);

			if (painting != null && painting.onValidSurface()){
				if (!worldIn.isRemote){
					painting.playPlaceSound();
					painting.art = EnumArt.valueOf(stack.getTagCompound().getString("art"));
					worldIn.spawnEntity(painting);
				}
			}

			return EnumActionResult.SUCCESS;
		}
		else{
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if(!stack.hasTagCompound())return;
		EnumArt painting = EnumArt.valueOf(stack.getTagCompound().getString("art"));
		tooltip.add("Painting: " + painting.title);
		tooltip.add("Size: " + painting.sizeX/16 + "x" + painting.sizeY/16);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
		if(world.isRemote)return;
		if(stack.hasTagCompound())return;
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("art", EnumArt.KEBAB.name());
		stack.setTagCompound(tag);
	}

	@Override
	public void trigger(World world, EntityPlayerMP player, ItemStack stack) {
		if(world.isRemote) return;
		int current = EnumArt.valueOf(stack.getTagCompound().getString("art")).ordinal();
		current++;
		if(current >= EnumArt.values().length)current = 0;
		EnumArt art = EnumArt.values()[current];
		stack.getTagCompound().setString("art", art.name());
		player.sendMessage(new TextComponentString("Changed painting to " + TextFormatting.GOLD + art.title + TextFormatting.GREEN + " (" + art.sizeX/16 + "x" + art.sizeY/16 + ")"));
	}
	
}
