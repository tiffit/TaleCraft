package de.longor.talecraft.util;

import java.util.Arrays;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTHelper {
	
	public static final NBTTagCompound getOrCreate(NBTTagCompound parent, String name) {
		if(parent.hasKey(name, parent.getId())) {
			return parent.getCompoundTag(name);
		} else {
			NBTTagCompound compound = new NBTTagCompound();
			parent.setTag(name, compound);
			return compound;
		}
	}

	public static NBTTagCompound getOrNull(NBTTagCompound parent, String name) {
		if(parent.hasKey(name, parent.getId())) {
			return parent.getCompoundTag(name);
		} else {
			return null;
		}
	}
	
	public static String asJson(NBTTagCompound compound) {
		StringBuilder builder = new StringBuilder(1024);
		asJson(compound, builder);
		return builder.toString();
	}
	
	private static void asJson(NBTTagCompound compound, StringBuilder builder) {
		builder.append('{');
		
		for(Object keyObject : compound.getKeySet()) {
			String key = (String)keyObject;
			
			builder.append(key);
			builder.append(':');
			asJson(compound.getTag(key), builder);
			builder.append(", ");
		}
		
		// Remove ", " at the end of the string if it's there.
		char last0 = builder.charAt(builder.length()-1);
		char last1 = builder.charAt(builder.length()-2);
		if(last0 == ' ' && last1 == ',') {
			builder.setLength(builder.length()-2);
		}
		
		builder.append('}');
	}
	
	private static void asJson(NBTTagList tag, StringBuilder builder) {
		builder.append('[');
		
		for(int i = 0; i < tag.tagCount(); i++) {
			asJson(tag.get(i), builder);
			builder.append(", ");
		}
		
		// Remove ", " at the end of the string if it's there.
		char last0 = builder.charAt(builder.length()-1);
		char last1 = builder.charAt(builder.length()-2);
		if(last0 == ' ' && last1 == ',') {
			builder.setLength(builder.length()-2);
		}
		
		builder.append(']');
	}
	
	private static void asJson(NBTBase tag, StringBuilder builder) {
		switch(tag.getId()) {
		case NBT.TAG_BYTE: builder.append(((NBTTagByte)tag).getByte()).append('b'); break;
		case NBT.TAG_SHORT: builder.append(((NBTTagShort)tag).getByte()).append('b'); break;
		case NBT.TAG_INT: builder.append(((NBTTagInt)tag).getInt()); break;
		case NBT.TAG_LONG: builder.append(((NBTTagLong)tag).getByte()).append('l'); break;
		case NBT.TAG_FLOAT: builder.append(((NBTTagFloat)tag).getFloat()).append('f'); break;
		case NBT.TAG_DOUBLE: builder.append(((NBTTagDouble)tag).getDouble()).append('d'); break;
		case NBT.TAG_STRING: builder.append('"').append(((NBTTagString)tag).getString()).append('"'); break;
		case NBT.TAG_BYTE_ARRAY: builder.append(Arrays.toString(((NBTTagByteArray)tag).getByteArray())); break;
		case NBT.TAG_INT_ARRAY: builder.append(Arrays.toString(((NBTTagIntArray)tag).getIntArray())); break;
		case NBT.TAG_COMPOUND: asJson((NBTTagCompound) tag, builder); break;
		case NBT.TAG_LIST: asJson((NBTTagList) tag, builder); break;
		}
		
	}
	
	public static NBTTagCompound newSingleStringCompound(String key, String value) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString(key, value);
		return compound;
	}
	
}
