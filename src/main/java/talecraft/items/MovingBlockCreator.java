package talecraft.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.client.gui.entity.MovingBlockGui;

public class MovingBlockCreator extends TCItem {
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)Minecraft.getMinecraft().displayGuiScreen(new MovingBlockGui(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ));
		return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
	}
	
}
