package de.longor.talecraft.blocks.util;

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
import de.longor.talecraft.blocks.util.tileentity.InverterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MemoryBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MessageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.TriggerFilterBlockTileEntity;
import de.longor.talecraft.client.gui.blocks.GuiInverterBlock;
import de.longor.talecraft.client.gui.blocks.GuiMemoryBlock;
import de.longor.talecraft.client.gui.blocks.GuiMessageBlock;
import de.longor.talecraft.client.gui.blocks.GuiTriggerFilterBlock;
import de.longor.talecraft.invoke.EnumTriggerState;

public class TriggerFilterBlock extends TCBlockContainer implements TCITriggerableBlock {
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TriggerFilterBlockTileEntity();
	}
	
	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
    		return;
		
        TileEntity tileentity = world.getTileEntity(position);
        
        if (tileentity instanceof TriggerFilterBlockTileEntity) {
            ((TriggerFilterBlockTileEntity)tileentity).trigger(triggerState);
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
		mc.displayGuiScreen(new GuiTriggerFilterBlock((TriggerFilterBlockTileEntity)worldIn.getTileEntity(pos)));
		
		return true;
    }
	
}
