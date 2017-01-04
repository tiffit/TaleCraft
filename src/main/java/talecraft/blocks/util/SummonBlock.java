package talecraft.blocks.util;

import java.util.UUID;

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
import talecraft.TaleCraft;
import talecraft.TaleCraftItems;
import talecraft.blocks.TCBlockContainer;
import talecraft.blocks.TCITriggerableBlock;
import talecraft.client.gui.blocks.GuiSummonBlock;
import talecraft.invoke.EnumTriggerState;
import talecraft.tileentity.SummonBlockTileEntity;
import talecraft.tileentity.SummonBlockTileEntity.SummonOption;

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
		if(!triggerState.equals(EnumTriggerState.ON)) return;
		TileEntity tileentity = world.getTileEntity(position);

		if (tileentity instanceof SummonBlockTileEntity) {
			((SummonBlockTileEntity)tileentity).trigger(triggerState);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if(heldItem != null && heldItem.getItem() == TaleCraftItems.entityclone && !world.isRemote){
			if(heldItem.hasTagCompound()){
				if(heldItem.getTagCompound().hasKey("entity_data")){
					SummonBlockTileEntity te = (SummonBlockTileEntity) world.getTileEntity(pos);
					NBTTagCompound entitydat = heldItem.getTagCompound().getCompoundTag("entity_data");
					entitydat.setUniqueId("UUID", UUID.randomUUID());
					SummonOption[] oldArray = te.getSummonOptions();
					SummonOption[] newArray = new SummonOption[oldArray.length+1];
					System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
					SummonOption option = new SummonOption();
					option.setWeight(1F);
					NBTTagCompound tag = new NBTTagCompound();
					tag.merge(entitydat);
					option.setData(tag);
					newArray[oldArray.length] = option;
					te.setSummonOptions(newArray);
					te.markDirty();
					if(!world.isRemote)playerIn.sendMessage(new TextComponentString("Entity data has been added to summon block!"));
					return true;
				}else{
					if(!world.isRemote)playerIn.sendMessage(new TextComponentString("No entity has been selected!"));
					return true;
				}
			}
		}
		if(!world.isRemote){
			return true;
		}
		return openGui(world, pos, state, playerIn, hand, heldItem, facing, hitX, hitY, hitZ);
	}
	
	@SideOnly(Side.CLIENT)
	private boolean openGui(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!TaleCraft.proxy.isBuildMode())
			return false;
		if(playerIn.isSneaking())
			return true;
		if(heldItem == null || heldItem.getItem() != TaleCraftItems.entityclone){
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiSummonBlock((SummonBlockTileEntity)world.getTileEntity(pos)));
		}
		return true;
	}

}
