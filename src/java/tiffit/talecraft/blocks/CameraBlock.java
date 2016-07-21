package tiffit.talecraft.blocks;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.blocks.TCBlockContainer;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.invoke.EnumTriggerState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.client.gui.GuiCameraBlock;
import tiffit.talecraft.tileentity.CameraBlockTileEntity;

public class CameraBlock extends TCBlockContainer implements TCITriggerableBlock {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CameraBlockTileEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			if(heldItem != null && heldItem.getItem() == TaleCraftItems.camera){
				NBTTagCompound tag = new NBTTagCompound();
				tag.setLong("selected", pos.toLong());
				heldItem.setTagCompound(tag);
				playerIn.addChatComponentMessage(new TextComponentString("Selected camera block!"));
			}
			return true;
		}
		if(!TaleCraft.proxy.isBuildMode())
			return false;
		if(playerIn.isSneaking())
			return true;

		if(heldItem == null || heldItem.getItem() != TaleCraftItems.camera){
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiCameraBlock((CameraBlockTileEntity)worldIn.getTileEntity(pos)));
		}
		return true;
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
			return;

		TileEntity tileentity = world.getTileEntity(position);

		if (tileentity instanceof CameraBlockTileEntity) {
			((CameraBlockTileEntity)tileentity).trigger(triggerState);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add("Currently a W.I.P.");
		tooltip.add("More features will come soon");
	}

}
