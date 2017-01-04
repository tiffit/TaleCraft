package talecraft.tileentity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.IScriptInvoke;
import talecraft.invoke.Invoke;
import talecraft.script.wrappers.entity.EntityObjectWrapper;
import talecraft.util.MutableInteger;

public class CollisionTriggerBlockTileEntity extends TCTileEntity{
	IInvoke collisionStartTrigger = BlockTriggerInvoke.ZEROINSTANCE;
	IInvoke collisionStopTrigger = BlockTriggerInvoke.ZEROINSTANCE;
	private int entityFilter = 1;

	// This map contains all entities that are currently colliding with this block.
	private Map<Entity,MutableInteger> collidingEntities = Maps.newHashMap();

	public void collision(Entity entityIn) {
		if(!filter(entityIn))return;

		if(collidingEntities.containsKey(entityIn)) {
			collidingEntities.get(entityIn).set(3);
		} else {
			collidingEntities.put(entityIn, new MutableInteger(3));

			Map<String,Object> map = null;
			if(collisionStartTrigger instanceof IScriptInvoke) {
				map = Maps.newHashMap();
				map.put("entity", new EntityObjectWrapper(entityIn));
			}

			Invoke.invoke(collisionStartTrigger, this, null, EnumTriggerState.ON);
		}

		propagateCollisionStart(entityIn);
	}

	private boolean filter(Entity entityIn) {
		if(entityFilter == 0) {
			return true;
		}

		if(entityFilter == 1) {
			return entityIn instanceof EntityPlayer;
		}

		if(entityFilter == 2) {
			return entityIn instanceof EntityLiving;
		}

		return true;
	}

	private void propagateCollisionStart(Entity entityIn) {
		propagateCollisionStart(entityIn, this.getPos().up());
		propagateCollisionStart(entityIn, this.getPos().down());
		propagateCollisionStart(entityIn, this.getPos().north());
		propagateCollisionStart(entityIn, this.getPos().east());
		propagateCollisionStart(entityIn, this.getPos().south());
		propagateCollisionStart(entityIn, this.getPos().west());
	}

	private void propagateCollisionStart(Entity entityIn, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if(tileEntity != null && tileEntity instanceof CollisionTriggerBlockTileEntity) {
			Map<Entity,MutableInteger> otherMap = ((CollisionTriggerBlockTileEntity)tileEntity).collidingEntities;

			if(otherMap.containsKey(entityIn)) {
				MutableInteger integer = otherMap.get(entityIn);

				if(integer.get() < 3) {
					// refresh collision
					integer.set(3);
					((CollisionTriggerBlockTileEntity) tileEntity).propagateCollisionStart(entityIn);
				} else {
					// do NOT propagate
				}

			} else {
				otherMap.put(entityIn, new MutableInteger(3));
				((CollisionTriggerBlockTileEntity) tileEntity).propagateCollisionStart(entityIn);
			}
		}
	}

	private void propagateCollisionStop(Entity entityIn) {
		propagateCollisionStop(entityIn, this.getPos().up());
		propagateCollisionStop(entityIn, this.getPos().down());
		propagateCollisionStop(entityIn, this.getPos().north());
		propagateCollisionStop(entityIn, this.getPos().east());
		propagateCollisionStop(entityIn, this.getPos().south());
		propagateCollisionStop(entityIn, this.getPos().west());
	}

	private void propagateCollisionStop(Entity entityIn, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if(tileEntity != null && tileEntity instanceof CollisionTriggerBlockTileEntity) {
			Map<Entity,MutableInteger> otherMap = ((CollisionTriggerBlockTileEntity)tileEntity).collidingEntities;

			if(otherMap.containsKey(entityIn)) {
				otherMap.remove(entityIn);
				((CollisionTriggerBlockTileEntity) tileEntity).propagateCollisionStop(entityIn);
			}
		}
	}

	@Override
	public void update() {
		super.update();
		LinkedList<Entity> toRemove = null;

		for(Entry<Entity,MutableInteger> entity : collidingEntities.entrySet()) {
			if(entity.getValue().decrement() <= 0) {
				if(toRemove == null)
					toRemove = Lists.newLinkedList();
				toRemove.add(entity.getKey());
			}
		}

		if(toRemove != null) {
			while(toRemove.size() > 0) {
				Entity entity = toRemove.removeFirst();
				collidingEntities.remove(entity);

				Map<String,Object> map = null;
				if(collisionStartTrigger instanceof IScriptInvoke) {
					map = Maps.newHashMap();
					map.put("entity", new EntityObjectWrapper(entity));
				}

				Invoke.invoke(collisionStopTrigger, this, null, EnumTriggerState.OFF);

				propagateCollisionStop(entity);
			}
		}
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		// ?

		super.commandReceived(command, data);
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(collisionStartTrigger);
		invokes.add(collisionStopTrigger);
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.00f;
		color[1] = 0.75f;
		color[2] = 0.00f;
	}

	@Override
	public String getName() {
		return "CollisionTriggerBlock@"+getPos();
	}

	@Override
	public void init() {

	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		collisionStartTrigger = IInvoke.Serializer.read(comp.getCompoundTag("collisionStartTrigger"));
		collisionStopTrigger = IInvoke.Serializer.read(comp.getCompoundTag("collisionStopTrigger"));
		entityFilter = comp.getInteger("filter");
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("collisionStartTrigger", IInvoke.Serializer.write(collisionStartTrigger));
		comp.setTag("collisionStopTrigger", IInvoke.Serializer.write(collisionStopTrigger));
		comp.setInteger("filter", entityFilter);
		return comp;
	}

	public int getEntityFilter() {
		return entityFilter;
	}

	public IInvoke getCollisionStartInvoke() {
		return collisionStartTrigger;
	}

	public IInvoke getCollisionStopInvoke() {
		return collisionStopTrigger;
	}

}
