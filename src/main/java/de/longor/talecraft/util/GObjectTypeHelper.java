package de.longor.talecraft.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GObjectTypeHelper {

	public static final List<String> getParticleNameList() {
		List<String> list = Lists.newArrayList();
		EnumParticleTypes[] types = EnumParticleTypes.values();

		for(EnumParticleTypes type : types) {
			list.add(type.getParticleName());
		}

		return list;
	}

	public static final EnumParticleTypes findParticleType(String name) {
		EnumParticleTypes[] types = EnumParticleTypes.values();

		for(EnumParticleTypes type : types)
			if(type.getParticleName().equalsIgnoreCase(name))
				return type;

		throw new IllegalArgumentException("Given name is not a prtaicle name.");
	}

	public static final Item findItem(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = typeString;

		// no meta necessary

		int indexOfDot = typeMID.indexOf(':');

		String typeMod = null;
		String typeID = null;

		if(indexOfDot == -1) {
			typeMod = "minecraft";
			typeID = typeMID;
		} else {
			typeMod = typeMID.substring(0, indexOfDot);
			typeID = typeMID.substring(indexOfDot+1);
		}

		Item item = Item.getByNameOrId(typeID);

		if(item != null) {
			return item;
		}

		return null;
	}

	public static final String[] findBlockState_retAStr(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = null;
		int typeMeta = 0;

		if(indexOfSlash == -1) {
			typeMID = typeString;
		} else {
			typeMID = typeString.substring(0, indexOfSlash);
			typeMeta = Integer.valueOf(typeString.substring(indexOfSlash+1));
		}

		int indexOfDot = typeMID.indexOf(':');

		String typeMod = null;
		String typeID = null;

		if(indexOfDot == -1) {
			typeMod = "minecraft";
			typeID = typeMID;
		} else {
			typeMod = typeMID.substring(0, indexOfDot);
			typeID = typeMID.substring(indexOfDot+1);
		}

		ResourceLocation location = new ResourceLocation(typeMod+":"+typeID);
		Block block = Block.blockRegistry.getObject(location);

		if(block != null) {
			if(!GameRegistry.findUniqueIdentifierFor(block).name.contains(typeID)) {
				System.err.println("Block type mismatch: " + typeString + " | " + typeMod + " " + typeID + " " + typeMeta + " GOT " + block.getUnlocalizedName());
				return null; // This is the wrong block! D: (Probably minecraft:air)
			}

			return new String[]{typeMID,typeMod,typeID,Integer.toString(typeMeta)};
		}

		System.err.println("Block type not found: " + typeString + " | " + typeMod + " " + typeID + " " + typeMeta);
		return null;
	}

	public static final String findBlockState_type(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = null;

		if(indexOfSlash == -1) {
			typeMID = typeString;
		} else {
			typeMID = typeString.substring(0, indexOfSlash);
		}

		int indexOfDot = typeMID.indexOf(':');

		String typeMod = null;
		String typeID = null;

		if(indexOfDot == -1) {
			typeMod = "minecraft";
			typeID = typeMID;
		} else {
			typeMod = typeMID.substring(0, indexOfDot);
			typeID = typeMID.substring(indexOfDot+1);
		}

		return typeID;
	}

	public static final int findBlockState_meta(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = null;
		int typeMeta = 0;

		if(indexOfSlash == -1) {
			typeMID = typeString;
		} else {
			typeMID = typeString.substring(0, indexOfSlash);
			typeMeta = Integer.valueOf(typeString.substring(indexOfSlash+1));
		}

		return typeMeta;
	}

	public static final IBlockState findBlockState(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = null;
		int typeMeta = 0;

		if(indexOfSlash == -1) {
			typeMID = typeString;
		} else {
			typeMID = typeString.substring(0, indexOfSlash);
			typeMeta = Integer.valueOf(typeString.substring(indexOfSlash+1));
		}

		int indexOfDot = typeMID.indexOf(':');

		String typeMod = null;
		String typeID = null;

		if(indexOfDot == -1) {
			typeMod = "minecraft";
			typeID = typeMID;
		} else {
			typeMod = typeMID.substring(0, indexOfDot);
			typeID = typeMID.substring(indexOfDot+1);
		}

		ResourceLocation location = new ResourceLocation(typeMod+":"+typeID);
		Block block = Block.blockRegistry.getObject(location);

		if(block != null) {
			if(block.getUnlocalizedName().equals("tile.air") && !typeID.contains("air")){
				System.out.println("Block " + block.getUnlocalizedName());
				return null; // This is the wrong block! D: (Probably minecraft:air)
			}

			return block.getStateFromMeta(typeMeta);
		}

		return null;
	}

	public static final Block findBlock(String fully_qualified_name) {
		String typeString = fully_qualified_name;
		int indexOfSlash = typeString.indexOf('/');

		String typeMID = null;
		int typeMeta = 0;

		if(indexOfSlash == -1) {
			typeMID = typeString;
		} else {
			typeMID = typeString.substring(0, indexOfSlash);
			typeMeta = Integer.valueOf(typeString.substring(indexOfSlash+1));
		}

		int indexOfDot = typeMID.indexOf(':');

		String typeMod = null;
		String typeID = null;

		if(indexOfDot == -1) {
			typeMod = "minecraft";
			typeID = typeMID;
		} else {
			typeMod = typeMID.substring(0, indexOfDot);
			typeID = typeMID.substring(indexOfDot+1);
		}

		ResourceLocation location = new ResourceLocation(typeMod+":"+typeID);
		Block block = Block.blockRegistry.getObject(location);

		if(block != null) {
			if(block.getUnlocalizedName().equals("tile.air") && !typeID.contains("air")){
				System.out.println("Block " + block.getUnlocalizedName());
				return null; // This is the wrong block! D: (Probably minecraft:air)
			}

			return block;
		}

		return null;
	}

	public static final Potion findPotion(String name) {
		return Potion.getPotionFromResourceLocation(name);
	}

	public static Set<ResourceLocation> getItemNameList() {
		return Item.itemRegistry.getKeys();
	}

	public static Collection<String> getEntityNameList() {
		return Lists.newArrayList(EntityList.getEntityNameList());
	}

}
