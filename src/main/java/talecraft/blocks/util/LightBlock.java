package talecraft.blocks.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraft;
import talecraft.blocks.TCBlockContainer;
import talecraft.blocks.TCITriggerableBlock;
import talecraft.client.gui.blocks.GuiLightBlock;
import talecraft.invoke.EnumTriggerState;
import talecraft.tileentity.LightBlockTileEntity;

public class LightBlock extends TCBlockContainer implements TCITriggerableBlock {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new LightBlockTileEntity();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if(tileEntity != null && tileEntity instanceof LightBlockTileEntity) {
			return ((LightBlockTileEntity) tileEntity).getLightValue();
		}

		return 15;
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
			return;

		TileEntity tileEntity = world.getTileEntity(position);

		if(tileEntity != null && tileEntity instanceof LightBlockTileEntity) {
			switch (triggerState) {
			case ON: ((LightBlockTileEntity) tileEntity).setLightActive(true); break;
			case OFF: ((LightBlockTileEntity) tileEntity).setLightActive(false); break;
			case INVERT: ((LightBlockTileEntity) tileEntity).toggleLightActive(); break;
			case IGNORE: ((LightBlockTileEntity) tileEntity).setLightActive(true); break;
			default: break;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote)
			return true;
		if(!TaleCraft.proxy.isBuildMode())
			return false;
		if(playerIn.isSneaking())
			return true;

		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiLightBlock((LightBlockTileEntity)worldIn.getTileEntity(pos)));

		return true;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		world.checkLight(pos);
	}

}
