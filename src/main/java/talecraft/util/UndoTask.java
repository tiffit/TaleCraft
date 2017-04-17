package talecraft.util;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class UndoTask {

	public static final List<UndoTask> TASKS = new ArrayList<UndoTask>();
	
	private final UndoRegion before;
	private final UndoRegion after;
	private final HashMap<BlockPos, IBlockState> changed = new HashMap<BlockPos, IBlockState>();
	
	public final String tool, user;
	public OffsetDateTime time;
	
	public UndoTask(UndoRegion before, UndoRegion after, String tool, String user){
		this.before = before;
		this.after = after;
		IBlockState[] beforeArray = before.blocks;
		IBlockState[] afterArray = after.blocks;
		for(int i = 0; i < beforeArray.length; i++){
			IBlockState beforeState = beforeArray[i];
			IBlockState afterState = afterArray[i];
			if(getStateId(beforeState) != getStateId(afterState)){
				MutableBlockPos pos = getPosFromIndex(i, before.width, before.height, before.length);
				pos.__add(before.getOrigin());
				changed.put(pos, beforeState);
			}
		}
		
		this.tool = tool;
		this.user = user;
		time = OffsetDateTime.now();
	}
	
	private static MutableBlockPos getPosFromIndex(int index, int width, int height, int length){
		if(index < width)return new MutableBlockPos(index, 0, 0);
		int x = 0;
		while(index >= width*height){
			x++;
			index-=width*height;
		}
		int y = 0;
		while(index >= width){
			y++;
			index-=width;
		}
		int z = index;
		return new MutableBlockPos(z, y, x);
	}
	
	public int getChangeSize(){
		return changed.size();
	}
	
	private int getStateId(IBlockState state){
		if(state == null) return -1;
		return Block.getStateId(state);
	}
	
	public void undo(){
		for(BlockPos pos : changed.keySet()){
			IBlockState state = changed.get(pos);
			before.world.setBlockState(pos, state);
		}
	}
	
	public static UndoTask getLastJob(){
		return TASKS.get(TASKS.size() - 1);
	}
	
	public static NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(UndoTask task : TASKS){
			NBTTagCompound taskTag = new NBTTagCompound();
			taskTag.setString("tool", task.tool);
			taskTag.setString("user", task.user);
			taskTag.setString("time", task.time.toString());
			taskTag.setTag("before", task.before.toNBT());
			taskTag.setTag("after", task.after.toNBT());
			list.appendTag(taskTag);
		}
		tag.setTag("list", list);
		return tag;
	}
	
	public static void loadFromNBT(NBTTagCompound tag){
		NBTTagList list = tag.getTagList("list", tag.getId());
		TASKS.clear();
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound taskTag = list.getCompoundTagAt(i);
			String tool = taskTag.getString("tool");
			String user = taskTag.getString("user");
			String time = taskTag.getString("time");
			UndoRegion before = UndoRegion.fromNBT(taskTag.getCompoundTag("before"));
			UndoRegion after = UndoRegion.fromNBT(taskTag.getCompoundTag("after"));
			UndoTask task = new UndoTask(before, after, tool, user);
			task.time = OffsetDateTime.parse(time);
			TASKS.add(task);
		}
	}
	
}
