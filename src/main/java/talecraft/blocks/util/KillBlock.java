package talecraft.blocks.util;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.blocks.TCBlock;
import talecraft.entity.NPC.EntityNPC;

public class KillBlock extends TCBlock {
	public static final PropertyInteger KILLTYPE = PropertyInteger.create("ktype", 0, 6);

	public KillBlock() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(KILLTYPE, Integer.valueOf(0)));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(entity instanceof EntityPlayerSP) {
			return;
		}

		if(entity instanceof EntityPlayerMP) {
			EntityPlayerMP p = (EntityPlayerMP) entity;
			if(p.capabilities.isCreativeMode)
				return;
		}

		int type = state.getValue(KILLTYPE).intValue();

		/*
			0 "all",
			1 "npc",
			2 "items",
			3 "living",
			4 "player",
			5 "monster",
			6 "xor_player"
		 */

		switch(type) {
		case 1: if(entity instanceof EntityNPC) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 2: if(entity instanceof EntityItem) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 3: if(entity instanceof EntityLivingBase) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 4: if(entity instanceof EntityPlayer) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 5: if(entity instanceof EntityMob) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 6: if(!(entity instanceof EntityPlayer)) {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;

		case 0: default: {
			entity.setPosition(entity.posX, -1024, entity.posZ);
			entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 999999999F);
		} break;
		}

	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		float f = 0.45f;
		return new AxisAlignedBB(
				pos.getX() + f,
				pos.getY() + f,
				pos.getZ() + f,
				(pos.getX() + 1) - f,
				(pos.getY() + 1) - f,
				(pos.getZ() + 1) - f
				);
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		int meta = stack.getMetadata();
		String type = "";
		switch(meta) {
		case 1:
			type = "NPC"; break;
		case 2:
			type = "Item"; break;
		case 3:
			type = "Living"; break;
		case 4:
			type = "Player"; break;
		case 5:
			type = "Monster"; break;
		case 6:
			type = "Non-Player"; break;
		case 0: default:
			type = "All"; break;
		}
		tooltip.add(type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int j = 0; j < 7; ++j) {
			list.add(new ItemStack(this, 1, j));
		}
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(KILLTYPE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(KILLTYPE).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {KILLTYPE});
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

}
