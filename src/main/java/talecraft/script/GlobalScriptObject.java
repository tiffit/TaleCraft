package talecraft.script;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.script.wrappers.block.BlockObjectWrapper;
import talecraft.script.wrappers.block.BlockStateObjectWrapper;
import talecraft.script.wrappers.item.ItemObjectWrapper;
import talecraft.script.wrappers.item.ItemStackObjectWrapper;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;
import talecraft.script.wrappers.potion.PotionEffectObjectWrapper;
import talecraft.script.wrappers.potion.PotionObjectWrapper;
import talecraft.util.GObjectTypeHelper;

public class GlobalScriptObject implements IObjectWrapper {
	private GlobalScriptManager globalScriptManager;

	protected GlobalScriptObject(GlobalScriptManager globalScriptManager) {
		this.globalScriptManager = globalScriptManager;
	}

	@Override
	public Object internal() {
		return null;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public Object eval(String script) {
		return globalScriptManager.interpret(script, "<?>", null);
	}

	public int command(String command) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		return server.getCommandManager().executeCommand(server, command);
	}
	
	public void setTimeout(final Runnable callable, final int delay) {
		TaleCraft.timedExecutor.executeLater(callable, delay);
	}

	public BlockStateObjectWrapper getBlock(String id) {
		IBlockState block = GObjectTypeHelper.findBlockState(id);

		if(block == null)
			return null;

		return new BlockStateObjectWrapper(block);
	}

	public BlockStateObjectWrapper getBlock(String id, int meta) {
		Block block = GObjectTypeHelper.findBlock(id);

		if(block == null)
			return null;

		return new BlockStateObjectWrapper(block.getStateFromMeta(meta));
	}

	public ItemObjectWrapper getItem(String id) {
		Item item = Item.getByNameOrId(id);

		if(item == null)
			return null;

		return new ItemObjectWrapper(item);
	}

	public PotionObjectWrapper getPotion(String name) {
		return new PotionObjectWrapper(Potion.getPotionFromResourceLocation(name));
	}

	public PotionObjectWrapper getPotion(int id) {
		return new PotionObjectWrapper(Potion.getPotionById(id));
	}

	// TODO: newItemStack(String identifier, ...);

	public ItemStackObjectWrapper newItemStack(BlockObjectWrapper block) {
		ItemStack stack = new ItemStack(block.internal(), 1);
		return new ItemStackObjectWrapper(stack);
	}

	public ItemStackObjectWrapper newItemStack(BlockObjectWrapper block, int amount) {
		ItemStack stack = new ItemStack(block.internal(), 1);
		return new ItemStackObjectWrapper(stack);
	}

	public ItemStackObjectWrapper newItemStack(ItemObjectWrapper item) {
		ItemStack stack = new ItemStack(item.internal(), 1);
		return new ItemStackObjectWrapper(stack);
	}

	public ItemStackObjectWrapper newItemStack(ItemObjectWrapper item, int amount) {
		ItemStack stack = new ItemStack(item.internal(), amount);
		return new ItemStackObjectWrapper(stack);
	}

	public ItemStackObjectWrapper newItemStack(ItemObjectWrapper item, int amount, int meta) {
		ItemStack stack = new ItemStack(item.internal(), amount, meta);
		return new ItemStackObjectWrapper(stack);
	}

	public ItemStackObjectWrapper newItemStack(String ID) {
		Item item = GObjectTypeHelper.findItem(ID);

		if(item == null)
			return null;

		return new ItemStackObjectWrapper(new ItemStack(item));
	}

	public ItemStackObjectWrapper newItemStack(String ID, int amount) {
		Item item = GObjectTypeHelper.findItem(ID);

		if(item == null)
			return null;

		return new ItemStackObjectWrapper(new ItemStack(item, amount));
	}

	public ItemStackObjectWrapper newItemStack(String ID, int amount, int damage) {
		Item item = GObjectTypeHelper.findItem(ID);

		if(item == null)
			return null;

		return new ItemStackObjectWrapper(new ItemStack(item, amount, damage));
	}

	public PotionEffectObjectWrapper newPotionEffect(PotionObjectWrapper potionWrapper, int duration) {
		Potion potion = potionWrapper.internal();
		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration));
	}

	public PotionEffectObjectWrapper newPotionEffect(PotionObjectWrapper potionWrapper, int duration, int amplifier) {
		Potion potion = potionWrapper.internal();
		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration, amplifier));
	}

	public PotionEffectObjectWrapper newPotionEffect(PotionObjectWrapper potionWrapper, int duration, int amplifier, boolean ambient, boolean showParticles) {
		Potion potion = potionWrapper.internal();
		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration, amplifier, ambient, showParticles));
	}

	public PotionEffectObjectWrapper newPotionEffect(String name, int duration) {
		Potion potion = GObjectTypeHelper.findPotion(name);

		if(potion == null)
			return null;

		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration));
	}

	public PotionEffectObjectWrapper newPotionEffect(String name, int duration, int amplifier) {
		Potion potion = GObjectTypeHelper.findPotion(name);

		if(potion == null)
			return null;

		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration, amplifier));
	}

	public PotionEffectObjectWrapper newPotionEffect(String name, int duration, int amplifier, boolean ambient, boolean showParticles) {
		Potion potion = GObjectTypeHelper.findPotion(name);

		if(potion == null)
			return null;

		return new PotionEffectObjectWrapper(new PotionEffect(potion, duration, amplifier, ambient, showParticles));
	}

	/**
	 * Creates a completely new compound-tag.
	 **/
	public CompoundTagWrapper newCompoundTag() {
		return new CompoundTagWrapper();
	}

	/**
	 * Takes a string that contains JSON and converts that into a new compound-tag.
	 **/
	public CompoundTagWrapper newCompoundTag(String json) {
		try {
			return new CompoundTagWrapper(JsonToNBT.getTagFromJson(json));
		} catch (NBTException e) {
			e.printStackTrace();
			return new CompoundTagWrapper();
		}
	}

	/**
	 * Takes a compound-tag and makes a exact copy of it.
	 **/
	public CompoundTagWrapper newCompoundTag(CompoundTagWrapper wrapper) {
		return new CompoundTagWrapper(wrapper);
	}

}
