package de.longor.talecraft.voxelbrush;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.voxelbrush.actions.EraserAction;
import de.longor.talecraft.voxelbrush.actions.GrassifyAction;
import de.longor.talecraft.voxelbrush.actions.MaskReplaceAction;
import de.longor.talecraft.voxelbrush.actions.ReplaceAction;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class ActionFactory {

	public static IAction create(String type, NBTTagCompound data) {
		// TaleCraft.logger.info("Creating Action: " + data);
		
		if(type.equals("replace")) {
			String blockID = data.getString("blockID");
			String blockMeta = data.getString("blockMeta");
			
			ResourceLocation blockLocation = new ResourceLocation(blockID);
			
			Block block = (Block) Block.blockRegistry.getObject(blockLocation);
			IBlockState blockState = block.getStateFromMeta(MathHelper.parseIntWithDefault(blockMeta, 0));
			
			return new ReplaceAction(block, blockState);
		}
		
		if(type.equals("maskreplace")) {
			String blockID = data.getString("blockID");
			String blockMeta = data.getString("blockMeta");
			
			String mask_blockID = data.getString("mask_blockID");
			String mask_blockMeta = data.getString("mask_blockMeta");
			
			ResourceLocation blockLocation = new ResourceLocation(blockID);
			ResourceLocation mask_blockLocation = new ResourceLocation(mask_blockID);
			
			Block block = (Block) Block.blockRegistry.getObject(blockLocation);
			IBlockState blockState = block.getStateFromMeta(MathHelper.parseIntWithDefault(blockMeta, 0));
			
			Block mask_block = (Block) Block.blockRegistry.getObject(mask_blockLocation);
			IBlockState mask_blockState = mask_block.getStateFromMeta(MathHelper.parseIntWithDefault(mask_blockMeta, 0));
			
			return new MaskReplaceAction(block, blockState, mask_block, mask_blockState);
		}
		
		if(type.equals("grassify")) {
			return new GrassifyAction();
		}
		
		if(type.equals("erase")) {
			return new EraserAction();
		}
		
		return null;
	}

}
