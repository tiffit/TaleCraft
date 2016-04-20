package tiffit.talecraft.blocks.world;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tiffit.talecraft.items.world.KeyItem;
import tiffit.talecraft.packet.DoorPacket;
import tiffit.talecraft.tileentity.LockedDoorTileEntity;
import tiffit.talecraft.tileentity.LockedDoorTileEntity.DoorCorner;

public class LockedDoorBlock extends TCBlock implements ITileEntityProvider{ //NOTE: This is the first time I used IBlockStates so I might have done something terribly

	public static final PropertyEnum CORNER = PropertyEnum.create("corner", DoorCorner.class);
	
	public LockedDoorBlock(){
		super();
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftWorldTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CORNER, DoorCorner.BottomLeftX));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		LockedDoorTileEntity te = (LockedDoorTileEntity) world.getTileEntity(pos);
		DoorCorner corner = te.corner;
		if(playerIn.isSneaking() && playerIn.isCreative() && !world.isRemote){
			if(corner == DoorCorner.BottomLeftX){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(1, 0, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(1, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.BottomRightX){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(-1, 0, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(-1, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.TopLeftX){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(1, 0, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(1, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.TopRightX){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(-1, 0, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(-1, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			
			if(corner == DoorCorner.BottomLeftZ){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 0, 1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, 1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.BottomRightZ){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 0, -1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 1, -1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.TopLeftZ){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 0, 1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, 1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			if(corner == DoorCorner.TopRightZ){
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, 0, -1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, 0))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos.add(0, -1, -1))).toggleKey();
				((LockedDoorTileEntity) world.getTileEntity(pos)).toggleKey();
			}
			LockedDoorTileEntity tehere = (LockedDoorTileEntity)world.getTileEntity(pos);
			String keyName = tehere.useSilverKey ? TextFormatting.DARK_GRAY + "silver " : TextFormatting.GOLD + "gold ";
			playerIn.addChatComponentMessage(new TextComponentString("This door now takes the " + keyName + TextFormatting.RESET + "key."));
			return true;
		}
		if(world.isRemote){
			if(heldItem != null && heldItem.getItem() instanceof KeyItem){
				if((heldItem.getItem() == TaleCraftItems.silverKey && te.useSilverKey) || (heldItem.getItem() == TaleCraftItems.goldKey && !te.useSilverKey))world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.block_chest_open, SoundCategory.BLOCKS, 100f, 1f, false);
			}
			return true;
		}
		if(heldItem != null && heldItem.getItem() instanceof KeyItem){
			if((heldItem.getItem() == TaleCraftItems.silverKey && te.useSilverKey) || (heldItem.getItem() == TaleCraftItems.goldKey && !te.useSilverKey)){
				world.setBlockToAir(pos);
				onBlockDestroyedByPlayer(world, pos, state);
				heldItem.stackSize--;
				return true;
			}
		}
		return true;
	}
	
	@Override
	//If you have a much better way of doing this, please tell me or even better, create a pull request
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		facing = EnumFacing.fromAngle(placer.rotationYawHead);
		boolean ZAxis = facing == EnumFacing.WEST || facing == EnumFacing.EAST;
		
		//Check here if there are any blocks that are in the way, but I am lazy
		boolean canPlace = true;
		if(ZAxis){
			
		}else{
			
		}
		world.setBlockState(pos.add(0, 1, 0), getStateFromMeta(DoorCorner.TopLeftX.ordinal() + (ZAxis ? 4 : 0)));
		world.setBlockState(pos.add(ZAxis ? 0 : 1, 0, ZAxis ? 1 : 0), getStateFromMeta(DoorCorner.BottomRightX.ordinal() + (ZAxis ? 4 : 0)));
		world.setBlockState(pos.add(ZAxis ? 0 : 1, 1, ZAxis ? 1 : 0), getStateFromMeta(DoorCorner.TopRightX.ordinal() + (ZAxis ? 4 : 0)));
		return this.getStateFromMeta(meta + (ZAxis ? 4 : 0));
	}
	
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state){
		DoorCorner corner = DoorCorner.values()[getMetaFromState(state)];
		if(corner == DoorCorner.BottomLeftX){
			world.setBlockToAir(pos.add(1, 0, 0));
			world.setBlockToAir(pos.add(0, 1, 0));
			world.setBlockToAir(pos.add(1, 1, 0));
		}
		if(corner == DoorCorner.BottomRightX){
			world.setBlockToAir(pos.add(-1, 0, 0));
			world.setBlockToAir(pos.add(0, 1, 0));
			world.setBlockToAir(pos.add(-1, 1, 0));
		}
		if(corner == DoorCorner.TopLeftX){
			world.setBlockToAir(pos.add(1, 0, 0));
			world.setBlockToAir(pos.add(0, -1, 0));
			world.setBlockToAir(pos.add(1, -1, 0));
		}
		if(corner == DoorCorner.TopRightX){
			world.setBlockToAir(pos.add(-1, 0, 0));
			world.setBlockToAir(pos.add(0, -1, 0));
			world.setBlockToAir(pos.add(-1, -1, 0));
		}
		
		if(corner == DoorCorner.BottomLeftZ){
			world.setBlockToAir(pos.add(0, 0, 1));
			world.setBlockToAir(pos.add(0, 1, 0));
			world.setBlockToAir(pos.add(0, 1, 1));
		}
		if(corner == DoorCorner.BottomRightZ){
			world.setBlockToAir(pos.add(0, 0, -1));
			world.setBlockToAir(pos.add(0, 1, 0));
			world.setBlockToAir(pos.add(0, 1, -1));
		}
		if(corner == DoorCorner.TopLeftZ){
			world.setBlockToAir(pos.add(0, 0, 1));
			world.setBlockToAir(pos.add(0, -1, 0));
			world.setBlockToAir(pos.add(0, -1, 1));
		}
		if(corner == DoorCorner.TopRightZ){
			world.setBlockToAir(pos.add(0, 0, -1));
			world.setBlockToAir(pos.add(0, -1, 0));
			world.setBlockToAir(pos.add(0, -1, -1));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, new IProperty[] { CORNER });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(CORNER, DoorCorner.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    DoorCorner type = (DoorCorner) state.getValue(CORNER);
	    return type.ordinal();
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		LockedDoorTileEntity te = new LockedDoorTileEntity();
		te.setDoorCorner((DoorCorner) getStateFromMeta(meta).getValue(CORNER));
		return te;
	}
	
}
