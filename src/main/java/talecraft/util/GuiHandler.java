package talecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.client.gui.blocks.GuiWorkbench;
import talecraft.container.WorkbenchContainer;

public class GuiHandler implements IGuiHandler {
	
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0){
			return new WorkbenchContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0){
			return new GuiWorkbench(new WorkbenchContainer(player.inventory, world, new BlockPos(x, y, z)));
		}
		return null;
	}

}
