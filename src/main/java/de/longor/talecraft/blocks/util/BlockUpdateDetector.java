package de.longor.talecraft.blocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCBlockContainer;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.blocks.util.tileentity.BlockUpdateDetectorTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.client.gui.blocks.GuiClockBlock;
import de.longor.talecraft.client.gui.blocks.GuiUpdateDetectorBlock;
import de.longor.talecraft.invoke.EnumTriggerState;

public class BlockUpdateDetector extends TCBlockContainer implements TCITriggerableBlock {
	
	public BlockUpdateDetector() {
		super();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(!worldIn.isRemote)
    		return true;
    	if(!TaleCraft.proxy.isBuildMode())
    		return false;
    	if(playerIn.isSneaking())
			return true;
    	
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiUpdateDetectorBlock((BlockUpdateDetectorTileEntity)worldIn.getTileEntity(pos)));
		
		return true;
    }
	
	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    	BlockUpdateDetectorTileEntity tEntity = (BlockUpdateDetectorTileEntity)worldIn.getTileEntity(pos);
		if(tEntity != null) {
			tEntity.triggerUpdateInvoke(EnumTriggerState.ON);
		}
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BlockUpdateDetectorTileEntity();
	}
	
	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState){
    	BlockUpdateDetectorTileEntity tEntity = (BlockUpdateDetectorTileEntity)world.getTileEntity(position);
		if(tEntity != null) {
			tEntity.triggerUpdateInvoke(triggerState);
		}
	}
	
}
