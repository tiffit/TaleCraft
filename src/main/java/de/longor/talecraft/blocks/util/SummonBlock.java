package de.longor.talecraft.blocks.util;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.blocks.TCBlockContainer;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity.SummonOption;
import de.longor.talecraft.client.gui.blocks.GuiSummonBlock;
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

// summonblock
public class SummonBlock extends TCBlockContainer implements TCITriggerableBlock {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SummonBlockTileEntity();
	}

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
			return;

		TileEntity tileentity = world.getTileEntity(position);

		if (tileentity instanceof SummonBlockTileEntity) {
			((SummonBlockTileEntity)tileentity).trigger(triggerState);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(heldItem != null && heldItem.getItem() == TaleCraftItems.npcclone){
			if(heldItem.hasTagCompound()){
				if(heldItem.getTagCompound().hasKey("npc")){
					SummonBlockTileEntity te = (SummonBlockTileEntity) world.getTileEntity(pos);
					NBTTagCompound npcdat = heldItem.getTagCompound().getCompoundTag("npc");
					SummonOption[] oldArray = te.getSummonOptions();
					SummonOption[] newArray = new SummonOption[oldArray.length+1];
					System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

					newArray[oldArray.length] = new SummonOption();
					newArray[oldArray.length].setWeight(1f);
					newArray[oldArray.length].setData(new NBTTagCompound());
					newArray[oldArray.length].getData().setString("id", "talecraft.tc_NPC");
					newArray[oldArray.length].getData().merge(npcdat);
					te.setSummonOptions(newArray);
					te.markDirty();
					if(!world.isRemote)playerIn.addChatMessage(new TextComponentString("NPC data has been added to summon block!"));
					return true;
				}
			}
		}
		if(!world.isRemote){
			return true;
		}
		if(!TaleCraft.proxy.isBuildMode())
			return false;
		if(playerIn.isSneaking())
			return true;
		if(heldItem == null || heldItem.getItem() != TaleCraftItems.npcclone){
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiSummonBlock((SummonBlockTileEntity)world.getTileEntity(pos)));
		}

		return true;
	}

}
