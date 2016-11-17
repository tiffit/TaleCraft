package talecraft.decorator;

import com.google.common.primitives.UnsignedBytes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SchematicDecoration implements Decoration {

	private final Schematic schematic;
	private final String name;
	
	public SchematicDecoration(Schematic schematic, String name) {
		this.schematic = schematic;
		this.name = name;
	}

	@Override
	public int plant(World world, BlockPos[] positions, NBTTagCompound options) {
		int count = 0;
		for(BlockPos pos : positions){
			BlockPos corner = new BlockPos(pos.getX()- schematic.width/2, pos.getY() , pos.getZ() - schematic.length/2);
			for(int i = 0; i < schematic.blocks.length; i++){
				AdvBlockInfo bi = schematic.blocks[i];
				world.setBlockState(bi.pos.add(corner), bi.state);
				count++;
			}
		}
		return count;
	}
	
	public static class Schematic{
		
		private final NBTTagCompound tag;
		private final short width;
		private final short height;
		private final short length;
		private AdvBlockInfo[] blocks;
		
		public Schematic(NBTTagCompound tag) throws InvalidSchematicException{
			this.tag = tag;
			String materials = tag.getString("Materials");
			if(!materials.equals("Alpha")){
				throw new InvalidSchematicException();
			}
			width = tag.getShort("Width");
			height = tag.getShort("Height");
			length = tag.getShort("Length");
			blocks = new AdvBlockInfo[width * height * length];

			byte[] blockData = tag.getByteArray("Blocks");
			byte[] blockMeta = tag.getByteArray("Data");

			int index = 0;
		    for(int i = 0; i < height; i++) {
		    	for(int j = 0; j < length; j++) {
		    		for(int k = 0; k < width; k++) {
		    			int blockId = UnsignedBytes.toInt(blockData[index]);
		    			BlockPos pos = new BlockPos(k, i, j);
		    			IBlockState state = Block.getBlockById(blockId).getStateFromMeta(blockMeta[index]);
		    			blocks[index] = new AdvBlockInfo(pos, state);
		    			index++;
		    		}
		    	}
		    }
		}
	}
	
	public static class InvalidSchematicException extends Exception{
		private InvalidSchematicException(){
			super("Schematic not in\"Alpha\" format!");
		}
	}
	
	private static class AdvBlockInfo{
		
		private final BlockPos pos;
		private final IBlockState state;
		
		public AdvBlockInfo(BlockPos pos, IBlockState state){
			this.pos = pos;
			this.state = state;
		}
		
	}

	@Override
	public String name() {
		return name;
	}

}
