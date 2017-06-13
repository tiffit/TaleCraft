package talecraft.blocks.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraft;
import talecraft.blocks.TCBlockContainer;
import talecraft.blocks.TCITriggerableBlock;
import talecraft.client.gui.blocks.GuiRedstoneTriggerBlock;
import talecraft.invoke.EnumTriggerState;
import talecraft.tileentity.RedstoneTriggerBlockTileEntity;

public class RedstoneTriggerBlock extends TCBlockContainer implements TCITriggerableBlock {
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

	public RedstoneTriggerBlock() {
		super();
		setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new RedstoneTriggerBlockTileEntity();
	}

	@Deprecated
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos from) {
		if (!worldIn.isRemote) {
			boolean flag = worldIn.isBlockPowered(pos);
			boolean flag1 = state.getValue(TRIGGERED).booleanValue();

			if (flag && !flag1) {
				worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			} else if (!flag && flag1) {
				worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote)
			return;

		TileEntity tileentity = worldIn.getTileEntity(pos);
		boolean flag1 = state.getValue(TRIGGERED).booleanValue();

		if (tileentity instanceof RedstoneTriggerBlockTileEntity) {
			((RedstoneTriggerBlockTileEntity)tileentity).invokeFromUpdateTick(EnumTriggerState.IGNORE, flag1);
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
		mc.displayGuiScreen(new GuiRedstoneTriggerBlock((RedstoneTriggerBlockTileEntity)worldIn.getTileEntity(pos)));

		return true;
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf((meta & 1) > 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (state.getValue(TRIGGERED).booleanValue())
		{
			i |= 1;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TRIGGERED});
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf(false));
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
			return;

		TileEntity tileentity = world.getTileEntity(position);

		if (tileentity instanceof RedstoneTriggerBlockTileEntity) {
			((RedstoneTriggerBlockTileEntity)tileentity).invokeFromUpdateTick(triggerState, true);
			((RedstoneTriggerBlockTileEntity)tileentity).invokeFromUpdateTick(triggerState, false);
		}
	}

}
