package talecraft.script.wrappers.world;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.script.wrappers.block.BlockObjectWrapper;
import talecraft.script.wrappers.block.BlockStateObjectWrapper;
import talecraft.script.wrappers.block.WorkbenchObjectWrapper;
import talecraft.script.wrappers.entity.EntityObjectWrapper;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;
import talecraft.script.wrappers.scoreboard.ScoreboardObjectWrapper;
import talecraft.util.GObjectTypeHelper;
import talecraft.util.MutableBlockPos;
import talecraft.util.WorkbenchManager;
import talecraft.util.WorldCommandSender;

public class WorldObjectWrapper implements IObjectWrapper {
	private World world;
	private WorldCommandSender worldCommandSender;
	private Scriptable worldScope;

	public WorldObjectWrapper(World world) {
		this.world = world;
		this.worldScope = null;
		this.worldCommandSender = null;
	}

	@Override
	public World internal() {
		return world;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public EntityObjectWrapper newEntity(String identifier) {
		if(identifier == null)
			return null;

		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(identifier), world);
		this.world.spawnEntity(entity);
		if(entity == null)
			return null;
		return EntityObjectWrapper.transform(entity);
	}

	public EntityObjectWrapper newEntity(CompoundTagWrapper nbt) {
		if(nbt == null)
			return null;

		Entity entity = EntityList.createEntityFromNBT(nbt.internal(), world);

		if(entity == null)
			return null;

		return EntityObjectWrapper.transform(entity);
	}

	public EntityObjectWrapper newEntity(String identifier, CompoundTagWrapper nbt) {
		if(identifier == null)
			return null;
		if(nbt == null)
			return null;

		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(identifier), world);

		if(entity == null)
			return null;

		EntityObjectWrapper wrap = EntityObjectWrapper.transform(entity);
		wrap.merge(nbt);
		return wrap;
	}

	public Object eval(String script) {
		if(worldScope == null) {
			worldScope = TaleCraft.globalScriptManager.createNewWorldScope(world);
		}

		return TaleCraft.globalScriptManager.interpret(script, "<?>", worldScope);
	}

	public int command(String command) {
		if(worldCommandSender == null) {
			worldCommandSender = new WorldCommandSender(world);
		}

		return FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(worldCommandSender, command);
	}

	public List<EntityObjectWrapper> getEntities(String selector) {
		if(worldCommandSender == null) {
			worldCommandSender = new WorldCommandSender(world);
		}

		List<Entity> entities = null;
		try {
			entities = EntitySelector.matchEntities(worldCommandSender, selector, Entity.class);
		} catch (CommandException e) {
			e.printStackTrace();
		}
		return EntityObjectWrapper.transform(entities);
	}
	
	public WorkbenchObjectWrapper getWorkbenchManager(){
		return new WorkbenchObjectWrapper(WorkbenchManager.getInstance());
	}

	public EntityObjectWrapper getEntity(String selector) {
		if(worldCommandSender == null) {
			worldCommandSender = new WorldCommandSender(world);
		}

		List<Entity> entities = null;
		try {
			entities = EntitySelector.matchEntities(worldCommandSender, selector, Entity.class);
		} catch (CommandException e) {
			e.printStackTrace();
		}
		return EntityObjectWrapper.transform(entities.get(0));
	}

	public List<EntityObjectWrapper> getEntitiesByName(final String name) {
		List<Entity> entities = world.getEntities(Entity.class, new Predicate<Entity>() {
			@Override public boolean apply(Entity input) {
				return input.getName().equals(name);
			}
		});

		return EntityObjectWrapper.transform(entities);
	}
	
	public EntityObjectWrapper getEntityByUUID(int id) {
		return new EntityObjectWrapper(world.getEntityByID(id));
	}
	
	public EntityObjectWrapper getEntityByName(final String name) {
		for(Entity ent : world.loadedEntityList) {
			if(ent.getName().equals(name)) {
				return EntityObjectWrapper.transform(ent);
			}
		}

		return null;
	}

	public BlockStateObjectWrapper getBlockState(int x, int y, int z) {
		return new BlockStateObjectWrapper(world.getBlockState(new MutableBlockPos(x, y, z)));
	}

	public BlockStateObjectWrapper getBlockState(MutableBlockPos pos) {
		return new BlockStateObjectWrapper(world.getBlockState(pos));
	}

	public BlockStateObjectWrapper getBlockState(BlockPos pos) {
		return new BlockStateObjectWrapper(world.getBlockState(pos));
	}

	public BlockObjectWrapper getBlock(int x, int y, int z) {
		return new BlockObjectWrapper(world.getBlockState(new MutableBlockPos(x, y, z)).getBlock());
	}

	public BlockObjectWrapper getBlock(MutableBlockPos pos) {
		return new BlockObjectWrapper(world.getBlockState(pos).getBlock());
	}

	public BlockObjectWrapper getBlock(BlockPos pos) {
		return new BlockObjectWrapper(world.getBlockState(pos).getBlock());
	}

	public void setBlock(BlockPos pos, String type) {
		world.setBlockState(pos, GObjectTypeHelper.findBlockState(type));
	}

	public void setBlock(BlockPos pos, IBlockState blockState) {
		world.setBlockState(pos, blockState);
	}

	public void setBlock(MutableBlockPos pos, IBlockState blockState) {
		world.setBlockState(pos, blockState);
	}

	public void setBlock(MutableBlockPos pos, BlockObjectWrapper blockObjectWrapper) {
		world.setBlockState(pos, blockObjectWrapper.internal().getDefaultState());
	}

	public void setBlock(MutableBlockPos pos, BlockStateObjectWrapper blockStateObjectWrapper) {
		world.setBlockState(pos, blockStateObjectWrapper.internal());
	}

	public void setBlock(int x, int y, int z, String type) {
		world.setBlockState(new MutableBlockPos(x, y, z), GObjectTypeHelper.findBlockState(type));
	}

	public void setBlock(int x, int y, int z, IBlockState blockState) {
		world.setBlockState(new MutableBlockPos(x, y, z), blockState);
	}

	public void setBlock(int x, int y, int z, BlockObjectWrapper blockObjectWrapper) {
		world.setBlockState(new MutableBlockPos(x, y, z), blockObjectWrapper.internal().getDefaultState());
	}

	public void setBlock(int x, int y, int z, BlockStateObjectWrapper blockStateObjectWrapper) {
		world.setBlockState(new MutableBlockPos(x, y, z), blockStateObjectWrapper.internal());
	}

	public float getBrightness(int x, int y, int z) {
		return world.getLightBrightness(new MutableBlockPos(x, y, z));
	}

	public float getBrightness(MutableBlockPos pos) {
		return world.getLightBrightness(pos);
	}

	public float getBrightness(BlockPos pos) {
		return world.getLightBrightness(pos);
	}

	public ScoreboardObjectWrapper getScoreboard() {
		return new ScoreboardObjectWrapper(world.getScoreboard());
	}

	public void setTime(long time) {
		world.setTotalWorldTime(time);
	}

	public long getTime() {
		return world.getWorldTime();
	}

	public long getTotalTime() {
		return world.getTotalWorldTime();
	}

	public long getSeed() {
		return world.getSeed();
	}

	public GameRulesObjectWrapper getGameRules() {
		return new GameRulesObjectWrapper(world.getGameRules());
	}

	public WorldBorderObjectWrapper getWorldBorder() {
		return new WorldBorderObjectWrapper(world.getWorldBorder());
	}

}
