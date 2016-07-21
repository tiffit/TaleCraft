package tiffit.talecraft.items;

import java.util.List;

import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tiffit.talecraft.tileentity.CameraBlockTileEntity;
import tiffit.talecraft.tileentity.CameraBlockTileEntity.CameraPos;

public class CameraItem extends TCItem {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		if(stack.getTagCompound() == null || !stack.getTagCompound().hasKey("selected")){
			player.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "No camera block selected!"));
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}else{
			BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("selected"));
			TileEntity te = world.getTileEntity(pos);
			if(te == null || !(te instanceof CameraBlockTileEntity)){
				player.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "No camera block found!"));
				return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
			}else{
				CameraBlockTileEntity camera = (CameraBlockTileEntity) te;
				CameraPos cpos = camera.addPos(player);
				player.addChatComponentMessage(new TextComponentString("Added Camera Pos: " + cpos.toString()));
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("selected")){
			BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("selected"));
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			tooltip.add("Selected Camera Block: [x=" + x + ", y=" + y + ", z=" + z + "]");
		}
	}
	
}
