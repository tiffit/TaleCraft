package talecraft.tileentity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;
import talecraft.TaleCraft;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;

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

	@Override
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
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
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
		return comp;
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
						if(!option.isStable() || stableCheck(EntityList.createEntityFromNBT(option.getData(), world), (int) option.getWeight()))summonEntity(option);
					}
				}
			}
		} else {
			// Spawns: SUMMONCOUNT
			// ...entities randomly selecting them from a weighted list.
			for(int i = 0; i < summonCount; i++) {
				SummonOption option = selectRandomWeightedOption();
				if(!option.isStable() || stableCheck(EntityList.createEntityFromNBT(option.getData(), world), summonCount))summonEntity(option);
			}
		}
	}

	// XXX: Something is wrong here with typeStr and spawnCount...?
	public void summonEntity(SummonOption option) {
		// Select 'random' position.
		Vec3d position = selectRandomBoundedLocation();
		double posX = position.x;
		double posY = position.y;
		double posZ = position.z;

		// Create the entity, merge the existing NBT into it, then spawn the entity.
		NBTTagCompound entityNBT = option.getData();
		entityNBT.setUniqueId("UUID", UUID.randomUUID());
		Entity entity = EntityList.createEntityFromNBT(entityNBT, world);

		if(entity == null) {
			TaleCraft.logger.error("FAILED TO SUMMON ENTITY: " + option.getData());
			return;
		}

		entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		((WorldServer)world).spawnEntity(entity);
		
		// This takes care of 'riding' entities.
		{
			Entity mountEntity = entity;

			for (
					NBTTagCompound mountEntityNBT = entityNBT;
					mountEntity != null && mountEntityNBT.hasKey("Riding", 10);
					mountEntityNBT = mountEntityNBT.getCompoundTag("Riding")
					) {
				Entity ridingEntity = EntityList.createEntityFromNBT(mountEntityNBT.getCompoundTag("Riding"), world);

				if (ridingEntity != null) {
					ridingEntity.setLocationAndAngles(posX, posY, posZ, ridingEntity.rotationYaw, ridingEntity.rotationPitch);
					world.spawnEntity(ridingEntity);
					mountEntity.startRiding(ridingEntity);
				}

				mountEntity = ridingEntity;
			}
		}

	}
	
	private boolean stableCheck(Entity entity, int max){
		BlockPos bp1 = new BlockPos(summonRegionBounds[0], summonRegionBounds[1], summonRegionBounds[2]);
		BlockPos bp2 = new BlockPos(summonRegionBounds[3], summonRegionBounds[4], summonRegionBounds[5]);
		List<Entity> entities = world.getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(bp1, bp2));
		int count = 0;
		for(Entity ent: entities){
			if(ent.getClass().equals(entity.getClass())) count++;
		}
		int canSpawn = max - count;
		return canSpawn >= 1;
	}

	public Vec3d selectRandomBoundedLocation() {
		Random random = world.rand;
		double error = 0.5d;

		double minX = summonRegionBounds[0] + error;
		double minY = summonRegionBounds[1] + error;
		double minZ = summonRegionBounds[2] + error;

		double maxX = summonRegionBounds[3] + 1 - error;
		double maxY = summonRegionBounds[4] - error;
		double maxZ = summonRegionBounds[5] + 1 - error;
		double posX = MathHelper.nextDouble(random, minX, maxX);
		double posY = MathHelper.nextDouble(random, minY, maxY);
		double posZ = MathHelper.nextDouble(random, minZ, maxZ);

		return new Vec3d(posX, posY, posZ);
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

	//SummonOption class START
	public static class SummonOption {
		private float summonWeight;
		private NBTTagCompound summonData;
		private boolean stable;

		public SummonOption() {
		}

		public float getWeight() {
			return summonWeight;
		}

		public void setWeight(float f) {
			summonWeight = f;
		}
		
		public void setStable(boolean bool){
			stable = bool;
		}
		
		public boolean isStable(){
			return stable;
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
			stable = compound.getBoolean("stable");
			summonData = compound.getCompoundTag("summonData");

			String ID = summonData.getString("id");

			if(ID != null && !ID.isEmpty() && summonData.getKeySet().size() < 3) {
				Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(ID), null);
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
			compound.setBoolean("stable", stable);
			return compound;
		}
	}
	//SummonOption class END

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
