package de.longor.talecraft.blocks.util.tileentity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.Constants.NBT;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity.SummonOption;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;

public class SummonBlockTileEntity extends TCTileEntity {
	private static final int[] ZEROBOUNDS = new int[6];
	int[] summonRegionBounds;
	int summonCount;
	SummonOption[] summonOptions;
	boolean useWeightAsCount;
	
	public SummonBlockTileEntity() {
		summonRegionBounds = new int[6];
		summonCount = 1;
		useWeightAsCount = false;
		
		int j = 3;
		summonOptions = new SummonOption[j];
		for(int i = 0; i < j; i++) {
			summonOptions[i] = new SummonOption();
			summonOptions[i].summonWeight = 1f;
			summonOptions[i].summonData = new NBTTagCompound();
			summonOptions[i].summonData.setString("id", "Zombie");
		}
	}
	
	public void init() {
		if(Arrays.equals(summonRegionBounds, ZEROBOUNDS)) {
			summonRegionBounds[0] = this.pos.getX() - 2;
			summonRegionBounds[1] = this.pos.getY() - 0;
			summonRegionBounds[2] = this.pos.getZ() - 2;
			summonRegionBounds[3] = this.pos.getX() + 2;
			summonRegionBounds[4] = this.pos.getY() + 1;
			summonRegionBounds[5] = this.pos.getZ() + 2;
		}
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.00f;
		color[1] = 0.25f;
		color[2] = 0.50f;
	}

	@Override
	public String getName() {
		return "SummonBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		summonCount = comp.getInteger("summonCount");
		summonRegionBounds = comp.getIntArray("summonRegionBounds");
		useWeightAsCount = comp.getBoolean("useWeightAsCount");
		
		NBTTagList list = comp.getTagList("summonOptions", NBT.TAG_COMPOUND);
		summonOptions = new SummonOption[list.tagCount()];
		
		for(int i = 0; i < list.tagCount(); i++) {
			summonOptions[i] = new SummonOption().read(list.getCompoundTagAt(i));
		}
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setInteger("summonCount", summonCount);
		comp.setIntArray("summonRegionBounds", summonRegionBounds);
		comp.setBoolean("useWeightAsCount", useWeightAsCount);
		
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < summonOptions.length; i++) {
			SummonOption option = summonOptions[i];
			
			if(option != null)
				list.appendTag(option.write());
			else
				TaleCraft.logger.error(getName() + " : Option #" + i + " is NULL! -> " + list);
		}
		
		comp.setTag("summonOptions", list);
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger(EnumTriggerState.ON);
			return;
		}
		
		// fall trough
		super.commandReceived(command, data);
	}
	
	public void trigger(EnumTriggerState on) {
		if(summonOptions == null) {
			return;
		}
		
		if(summonOptions.length == 0) {
			return;
		}
		
		if(useWeightAsCount) {
			// Spawns: (foreach OPTION in OPTIONS do SPAWN(option,option[X].WEIGHT as count)
			// ...entities randomly selecting them from a weighted list.
			
			// for(int i = 0; i < summonCount; i++)
			{
				for(SummonOption option : summonOptions) {
					for(int j = 0; j < option.getWeight(); j++) {
						summonEntity(option);
					}
				}
			}
		} else {
			// Spawns: SUMMONCOUNT
			// ...entities randomly selecting them from a weighted list.
			for(int i = 0; i < summonCount; i++) {
				SummonOption option = selectRandomWeightedOption();
				summonEntity(option);
			}
		}
	}
	
	public void summonEntity(SummonOption option) {
		// Select 'random' position.
		Vec3 position = selectRandomBoundedLocation();
		double posX = position.xCoord;
		double posY = position.yCoord;
		double posZ = position.zCoord;
		
		// Create the entity, merge the existing NBT into it, then spawn the entity.
		NBTTagCompound entityNBT = option.getData();
		String typeStr = entityNBT.getString("id");
		Entity entity = EntityList.createEntityFromNBT(entityNBT, worldObj);
		
		if(entity == null) {
			TaleCraft.logger.error("FAILED TO SUMMON ENTITY: " + option.getData());
			return;
		}
		
		entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		worldObj.spawnEntityInWorld(entity);
		
		int spawnCount = 1;
		
		// This takes care of 'riding' entities.
		{
			Entity mountEntity = entity;
			
			for (
				NBTTagCompound mountEntityNBT = entityNBT;
				mountEntity != null && mountEntityNBT.hasKey("Riding", 10);
				mountEntityNBT = mountEntityNBT.getCompoundTag("Riding")
			) {
				Entity ridingEntity = EntityList.createEntityFromNBT(mountEntityNBT.getCompoundTag("Riding"), worldObj);
				
				if (ridingEntity != null) {
					ridingEntity.setLocationAndAngles(posX, posY, posZ, ridingEntity.rotationYaw, ridingEntity.rotationPitch);
					worldObj.spawnEntityInWorld(ridingEntity);
					mountEntity.mountEntity(ridingEntity);
					spawnCount++;
				}
				
				mountEntity = ridingEntity;
			}
		}
		
	}
	
	public Vec3 selectRandomBoundedLocation() {
		Random random = worldObj.rand;
		double error = 0.5d;
		
		double minX = summonRegionBounds[0] + error;
		double minY = summonRegionBounds[1] + error;
		double minZ = summonRegionBounds[2] + error;
		
		double maxX = summonRegionBounds[3] + 1 - error;
		double maxY = summonRegionBounds[4] - error;
		double maxZ = summonRegionBounds[5] + 1 - error;
		
		double posX = MathHelper.getRandomDoubleInRange(random, minX, maxX);
		double posY = MathHelper.getRandomDoubleInRange(random, minY, maxY);
		double posZ = MathHelper.getRandomDoubleInRange(random, minZ, maxZ);
		
		return new Vec3(posX, posY, posZ);
	}
	
	public SummonOption selectRandomWeightedOption() {
		SummonOption[] items = summonOptions;
		
		// Compute the total weight of all items together
		double totalWeight = 0.0d;
		for (SummonOption i : items) {
			totalWeight += i.getWeight();
		}
		
		// Now choose a random item
		int randomIndex = -1;
		double random = Math.random() * totalWeight;
		
		for (int i = 0; i < items.length; ++i) {
			random -= items[i].getWeight();
			
			if (random <= 0.0d) {
				randomIndex = i;
				break;
			}
		}

		return items[randomIndex];
	}
	
	
	
	
	
	public static class SummonOption {
		float summonWeight;
		NBTTagCompound summonData;
		
		public SummonOption() {
			
		}
		
		public float getWeight() {
			return summonWeight;
		}

		public void setWeight(float f) {
			summonWeight = f;
		}
		
		public NBTTagCompound getData() {
			return summonData;
		}
		
		public void setData(NBTTagCompound entityData) {
			if(entityData == null) {
				TaleCraft.logger.error("'entityData' is null!");
			}
			
			if(summonData == null) {
				summonData = entityData;
				return;
			}
			
			summonData.merge(entityData);
		}
		
		public SummonOption read(NBTTagCompound compound) {
			summonWeight = compound.getFloat("summonWeight");
			summonData = compound.getCompoundTag("summonData");
			
			String ID = summonData.getString("id");
			
			if(ID != null && !ID.isEmpty() && summonData.getKeySet().size() < 3) {
				Entity entity = EntityList.createEntityByName(ID, null);
				if(entity != null) {
					NBTTagCompound mergecompound = new NBTTagCompound();
					entity.writeToNBT(mergecompound);
					
					// remove 'spatial' and 'identifiying' information
					mergecompound.removeTag("Pos");
					mergecompound.removeTag("OnGround");
					mergecompound.removeTag("Dimension");
					mergecompound.removeTag("FallDistance");
					mergecompound.removeTag("PortalCooldown");
					mergecompound.removeTag("UUIDMost");
					mergecompound.removeTag("UUIDLeast");
					mergecompound.removeTag("ConversionTime");
					mergecompound.removeTag("HurtByTimestamp");
					mergecompound.removeTag("Leashed");
					mergecompound.removeTag("Air");
					mergecompound.removeTag("Fire");
					mergecompound.removeTag("DeathTime");
					mergecompound.removeTag("DropChances");
					mergecompound.removeTag("AbsorptionAmount");
					mergecompound.removeTag("CanPickUpLoot");
					mergecompound.removeTag("PersistenceRequired");
					mergecompound.removeTag("HurtTime");
					
					summonData.merge(mergecompound);
				}
			}
			
			return this;
		}
		
		public NBTTagCompound write() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setFloat("summonWeight", summonWeight);
			compound.setTag("summonData", summonData);
			return compound;
		}
	}





	public int[] getSummonRegionBounds() {
		return summonRegionBounds;
	}
	
	public int getSummonCount() {
		return summonCount;
	}
	
	public SummonOption[] getSummonOptions() {
		return summonOptions;
	}
	
	public void setSummonOptions(SummonOption[] result) {
		summonOptions = result;
	}

	public boolean getSummonWeightAsCount() {
		return useWeightAsCount;
	}

	public void setWeightAsCount(boolean newState) {
		useWeightAsCount = true;
	}
	
	
	
}
