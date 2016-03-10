package de.longor.talecraft.blocks.util;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlockContainer;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.client.gui.blocks.GuiRedstoneTriggerBlock;
import de.longor.talecraft.client.gui.blocks.GuiRelayBlock;
import de.longor.talecraft.invoke.EnumTriggerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RelayBlock extends TCBlockContainer implements TCITriggerableBlock {
	
	public RelayBlock() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new RelayBlockTileEntity();
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
		mc.displayGuiScreen(new GuiRelayBlock((RelayBlockTileEntity)worldIn.getTileEntity(pos)));
		
		return true;
    }
	
	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		RelayBlockTileEntity tEntity = (RelayBlockTileEntity)world.getTileEntity(position);
		if(tEntity != null) {
			tEntity.triggerRelayInvoke(triggerState);
		}
	}
	
}
