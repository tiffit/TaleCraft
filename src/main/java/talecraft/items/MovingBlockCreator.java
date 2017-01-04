package talecraft.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import talecraft.client.gui.entity.MovingBlockGui;
import talecraft.entity.EntityMovingBlock;
import talecraft.util.MutableBlockPos;

public class MovingBlockCreator extends TCItem {
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)Minecraft.getMinecraft().displayGuiScreen(new MovingBlockGui(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ));
		return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
	}
	
}
