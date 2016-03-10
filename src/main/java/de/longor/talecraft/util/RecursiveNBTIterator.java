package de.longor.talecraft.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class RecursiveNBTIterator {
	
	public static final void iterate(NBTTagCompound nbt, NBTTreeConsumer consumer) {
		iterate(nbt, consumer, 0);
	}
	
	private static final void iterate(NBTTagCompound parent, NBTTreeConsumer consumer, int depth) {
		consumer.consume(depth+1, "<compound>", parent, null);
		
		for(Object keyObject : parent.getKeySet()) {
			String key = (String)keyObject;
			NBTBase value = parent.getTag(key);
			
			consumer.consume(depth+1, key, value, parent);
			
			if(value instanceof NBTTagCompound) {
				iterate((NBTTagCompound)value, consumer, depth+1);
			}
			
			if(value instanceof NBTTagList) {
				NBTTagList list = (NBTTagList) value;
				if(list.getTagType() == NBT.TAG_COMPOUND) {
					for(int i = 0; i < list.tagCount(); i++) {
						NBTTagCompound compound = list.getCompoundTagAt(i);
						iterate(compound, consumer, depth+1);
					}
				}
			}
		}
		consumer.consume(depth+1, "", null, parent);
	}
	
	public static interface NBTTreeConsumer {
		public void consume(int depth, String name, NBTBase tag, NBTTagCompound parent);
	}
	
}
