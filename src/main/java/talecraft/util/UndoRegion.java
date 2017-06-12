package talecraft.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class UndoRegion{

	private BlockRegion region;
	public final int xPos, yPos, zPos;
	public final int width, height, length;
	public final World world;
	public IBlockState[] blocks;
	
	public UndoRegion(BlockRegion region, World world){
		region = new BlockRegion(region.getMinX() - 1, region.getMinY() - 1, region.getMinZ() - 1, region.getMaxX() + 1, region.getMaxY() + 1, region.getMaxZ() + 1);
		this.region = region;
		xPos = region.getMinX();
		yPos = region.getMinY();
		zPos = region.getMinZ();
		width = region.getWidth() + 1;
		height = region.getHeight() + 1;
		length = region.getLength() + 1;
		this.world = world;
		runIterator();
	}
	
	private UndoRegion(int xPos, int yPos, int zPos, int width, int height, int length, World world){
		BlockRegion region = new BlockRegion(xPos, yPos, zPos, xPos + width, yPos + height, zPos + length);
		this.region = region;
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.width = width;
		this.height = height;
		this.length = length;
		this.world = world;
		runIterator();
	}
	
	private void runIterator(){
		ArrayList<IBlockState> stateList = new ArrayList<IBlockState>();
		for(BlockPos pos : BlockPos.getAllInBox(new BlockPos(region.getMinX(), region.getMinY(), region.getMinZ()), new BlockPos(region.getMaxX(), region.getMaxY(), region.getMaxZ()))){
			IBlockState state = world.getBlockState(pos);
			if(state == null)state = Blocks.AIR.getDefaultState();
			stateList.add(state);
		}
		blocks = new IBlockState[stateList.size()];
		for(int i = 0; i < stateList.size(); i++){
			blocks[i] = stateList.get(i);
		}
	}
	
	public BlockPos getOrigin(){
		return region.getMin();
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", xPos);
		tag.setInteger("y", yPos);
		tag.setInteger("z", zPos);
		
		tag.setInteger("width", width);
		tag.setInteger("height", height);
		tag.setInteger("length", length);
		
		tag.setInteger("world", world.provider.getDimension());
		
		NBTTagList list = new NBTTagList();
		for(IBlockState state : blocks){
			list.appendTag(new NBTTagInt(Block.getStateId(state)));
		}
		tag.setTag("blocks", list);
		return tag;
	}
	
	public static UndoRegion fromNBT(NBTTagCompound tag){
		UndoRegion ureg = new UndoRegion(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"), tag.getInteger("width"), tag.getInteger("height"), tag.getInteger("length"), DimensionManager.getWorld(tag.getInteger("world")));
		NBTTagList list = tag.getTagList("blocks", 3);
		IBlockState[] blocks = new IBlockState[list.tagCount()];
		for(int i = 0; i < list.tagCount(); i++){
			blocks[i] = Block.getStateById(list.getIntAt(i));
		}
		ureg.blocks = blocks;
		return ureg;
	}
	
	
}
