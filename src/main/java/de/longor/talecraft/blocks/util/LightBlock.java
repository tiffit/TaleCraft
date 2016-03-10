package de.longor.talecraft.blocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCBlockContainer;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.LightBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.client.gui.blocks.GuiLightBlock;
import de.longor.talecraft.client.gui.blocks.GuiRedstoneTriggerBlock;
import de.longor.talecraft.invoke.EnumTriggerState;

public class LightBlock extends TCBlockContainer implements TCITriggerableBlock {
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new LightBlockTileEntity();
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
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
	
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    	worldIn.checkLight(pos);
    }
	
}
