package tiffit.talecraft.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class CustomWorldData extends WorldSavedData {

	public CustomWorldData() {
		super("TCMapData");
	}

	final static String key = "my.unique.string";

	public NBTTagCompound customdata;
	
	public static CustomWorldData forWorld(World world) {
                // Retrieves the MyWorldData instance for the given world, creating it if necessary
		MapStorage storage = world.getPerWorldStorage();
		CustomWorldData result = (CustomWorldData)storage.loadData(CustomWorldData.class, key);
		if (result == null) {
			result = new CustomWorldData();
			storage.setData(key, result);
		}
		return result;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		customdata = nbt.getCompoundTag("tc_world_data");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		nbt.setTag("tc_world_data", customdata);
	}

}
