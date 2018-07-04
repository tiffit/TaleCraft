package talecraft;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import talecraft.blocks.deco.BlankBlock;
import talecraft.blocks.util.BlockUpdateDetector;
import talecraft.blocks.util.CameraBlock;
import talecraft.blocks.util.ClockBlock;
import talecraft.blocks.util.CollisionTriggerBlock;
import talecraft.blocks.util.DelayBlock;
import talecraft.blocks.util.EmitterBlock;
import talecraft.blocks.util.HiddenBlock;
import talecraft.blocks.util.ImageHologramBlock;
import talecraft.blocks.util.InverterBlock;
import talecraft.blocks.util.KillBlock;
import talecraft.blocks.util.LightBlock;
import talecraft.blocks.util.MemoryBlock;
import talecraft.blocks.util.MessageBlock;
import talecraft.blocks.util.MusicBlock;
import talecraft.blocks.util.RedstoneActivatorBlock;
import talecraft.blocks.util.RedstoneTriggerBlock;
import talecraft.blocks.util.RelayBlock;
import talecraft.blocks.util.ScriptBlock;
import talecraft.blocks.util.StorageBlock;
import talecraft.blocks.util.SummonBlock;
import talecraft.blocks.util.TriggerFilterBlock;
import talecraft.blocks.util.URLBlock;
import talecraft.blocks.world.LockedDoorBlock;
import talecraft.blocks.world.SpikeBlock;
import talecraft.blocks.world.WorkbenchBlock;
import talecraft.tileentity.BlockUpdateDetectorTileEntity;
import talecraft.tileentity.CameraBlockTileEntity;
import talecraft.tileentity.ClockBlockTileEntity;
import talecraft.tileentity.CollisionTriggerBlockTileEntity;
import talecraft.tileentity.DelayBlockTileEntity;
import talecraft.tileentity.EmitterBlockTileEntity;
import talecraft.tileentity.ImageHologramBlockTileEntity;
import talecraft.tileentity.InverterBlockTileEntity;
import talecraft.tileentity.LightBlockTileEntity;
import talecraft.tileentity.LockedDoorTileEntity;
import talecraft.tileentity.MemoryBlockTileEntity;
import talecraft.tileentity.MessageBlockTileEntity;
import talecraft.tileentity.MusicBlockTileEntity;
import talecraft.tileentity.RedstoneTriggerBlockTileEntity;
import talecraft.tileentity.RelayBlockTileEntity;
import talecraft.tileentity.ScriptBlockTileEntity;
import talecraft.tileentity.StorageBlockTileEntity;
import talecraft.tileentity.SummonBlockTileEntity;
import talecraft.tileentity.TriggerFilterBlockTileEntity;
import talecraft.tileentity.URLBlockTileEntity;

@EventBusSubscriber
public class TaleCraftBlocks {
	public static HashMap<String, Block> blocksMap = Maps.newHashMap();
	public static List<Block> blocks = Lists.newArrayList();
	public static List<Block> customItemBlocks = Lists.newArrayList();
	private static IForgeRegistry<Block> registry;

	// UTILITY
	public static KillBlock killBlock;
	public static ClockBlock clockBlock;
	public static RedstoneTriggerBlock redstoneTrigger;
	public static RedstoneActivatorBlock redstoneActivator;
	public static RelayBlock relayBlock;
	public static ScriptBlock scriptBlock;
	public static BlockUpdateDetector updateDetectorBlock;
	public static StorageBlock storageBlock;
	public static EmitterBlock emitterBlock;
	public static ImageHologramBlock imageHologramBlock;
	public static CollisionTriggerBlock collisionTriggerBlock;
	public static LightBlock lightBlock;
	public static HiddenBlock hiddenBlock;
	public static MessageBlock messageBlock;
	public static InverterBlock inverterBlock;
	public static MemoryBlock memoryBlock;
	public static TriggerFilterBlock triggerFilterBlock;
	public static DelayBlock delayBlock;
	public static URLBlock urlBlock;
	public static SummonBlock summonBlock;
	public static MusicBlock musicBlock;
	public static CameraBlock cameraBlock;
	
	// WORLD
	public static LockedDoorBlock lockedDoorBlock;
	public static SpikeBlock spikeBlock;
	public static WorkbenchBlock workbench;
	
	// DECORATION
	public static BlankBlock blankBlock;
	public static BlankBlock deco_stone_a;
	public static BlankBlock deco_stone_b;
	public static BlankBlock deco_stone_c;
	public static BlankBlock deco_stone_d;
	public static BlankBlock deco_stone_e;
	public static BlankBlock deco_stone_f;
	public static BlankBlock deco_wood_a;
	public static BlankBlock deco_glass_a;
	public static BlankBlock deco_cage_a;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Block> event) {
		registry = event.getRegistry();
		
		blocksMap = Maps.newHashMap();
		blocks = Lists.newArrayList();

		init_utility();
		init_decoration();
		init_world();
	}

	private static void init_world() {
		lockedDoorBlock = registerWithTE("lockeddoorblock", new LockedDoorBlock(), LockedDoorTileEntity.class);
	}
	
	private static void init_utility() {
		killBlock = register("killblock", new KillBlock());

		clockBlock = registerWithTE("clockblock", new ClockBlock(), ClockBlockTileEntity.class);

		redstoneTrigger = registerWithTE("redstone_trigger", new RedstoneTriggerBlock(), RedstoneTriggerBlockTileEntity.class);

		redstoneActivator = register("redstone_activator", new RedstoneActivatorBlock());

		relayBlock = registerWithTE("relayblock", new RelayBlock(), RelayBlockTileEntity.class);

		scriptBlock = registerWithTE("scriptblock", new ScriptBlock(), ScriptBlockTileEntity.class);

		updateDetectorBlock = registerWithTE("updatedetectorblock", new BlockUpdateDetector(), BlockUpdateDetectorTileEntity.class);

		storageBlock = registerWithTE("storageblock", new StorageBlock(), StorageBlockTileEntity.class);

		emitterBlock = registerWithTE("emitterblock", new EmitterBlock(), EmitterBlockTileEntity.class);

		imageHologramBlock = registerWithTE("imagehologramblock", new ImageHologramBlock(), ImageHologramBlockTileEntity.class);

		collisionTriggerBlock = registerWithTE("collisiontriggerblock", new CollisionTriggerBlock(), CollisionTriggerBlockTileEntity.class);

		lightBlock = registerWithTE("lightblock", new LightBlock(), LightBlockTileEntity.class);

		hiddenBlock = register("hiddenblock", new HiddenBlock());

		messageBlock = registerWithTE("messageblock", new MessageBlock(), MessageBlockTileEntity.class);

		inverterBlock = registerWithTE("inverterblock", new InverterBlock(), InverterBlockTileEntity.class);

		memoryBlock = registerWithTE("memoryblock", new MemoryBlock(), MemoryBlockTileEntity.class);

		triggerFilterBlock = registerWithTE("triggerfilterblock", new TriggerFilterBlock(), TriggerFilterBlockTileEntity.class);

		delayBlock = registerWithTE("delayblock", new DelayBlock(), DelayBlockTileEntity.class);

		urlBlock = registerWithTE("urlblock", new URLBlock(), URLBlockTileEntity.class);

		summonBlock = registerWithTE("summonblock", new SummonBlock(), SummonBlockTileEntity.class);
		
		musicBlock = registerWithTE("musicblock", new MusicBlock(), MusicBlockTileEntity.class);
		
		cameraBlock = registerWithTE("camerablock", new CameraBlock(), CameraBlockTileEntity.class);
		
		spikeBlock = register("spikeblock", new SpikeBlock());
		workbench = register("workbench", new WorkbenchBlock());
	}

	private static void init_decoration() {
		blankBlock = register("blankblock", new BlankBlock(SoundType.STONE), true);
		deco_stone_a = register("deco_stone_a", new BlankBlock(SoundType.STONE), true);
		deco_stone_b = register("deco_stone_b", new BlankBlock(SoundType.STONE), true);
		deco_stone_c = register("deco_stone_c", new BlankBlock(SoundType.STONE), true);
		deco_stone_d = register("deco_stone_d", new BlankBlock(SoundType.STONE), true);
		deco_stone_e = register("deco_stone_e", new BlankBlock(SoundType.STONE), true);
		deco_stone_f = register("deco_stone_f", new BlankBlock(SoundType.STONE), true);

		deco_wood_a = register("deco_wood_a", new BlankBlock(SoundType.WOOD), true);

		deco_glass_a = register("deco_glass_a", new BlankBlock(SoundType.GLASS), true);
		deco_glass_a.blockLayer = 1; // CUTOUT layer
		deco_glass_a.ignoreSimilarity = false;
		deco_glass_a.setLightOpacity(0);

		deco_cage_a = register("deco_cage_a", new BlankBlock(SoundType.METAL), true);
		deco_cage_a.blockLayer = 1; // CUTOUT layer
		deco_cage_a.ignoreSimilarity = true;
		deco_cage_a.setLightOpacity(0);

	}

	private static <T extends Block> T register(String name, T block) {
		block.setUnlocalizedName("talecraft:" + name);
		block.setRegistryName(Reference.MOD_ID, name);
		registry.register(block);
		addToMaps(block, name);
		return block;
	}
	
	private static <T extends Block> T register(String name, T block, boolean customItemBlock) {
		block.setUnlocalizedName("talecraft:" + name);
		block.setRegistryName(Reference.MOD_ID, name);
		registry.register(block);
		addToMaps(block, name);
		if(customItemBlock) customItemBlocks.add(block);
		return block;
	}
	
	private static <T extends Block, E extends TileEntity> T registerWithTE(String name, T block, Class<E> tileEntityClass) {
		T returnBlock = register(name, block);
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation("talecraft", name));
		return returnBlock;
	}
	
	private static void addToMaps(Block block, String name) {
		blocksMap.put(name, block);
		blocks.add(block);
		Item item = Item.getItemFromBlock(block);
		if(item != null)TaleCraftItems.ALL_TC_ITEMS.add(item);
	}

	public static class ItemBlockBlankBlock extends ItemMultiTexture {
		public ItemBlockBlankBlock(Block block) {
			super(block, block, new String[] {
					"0",
					"1",
					"2",
					"3",
					"4",
					"5",
					"6",
					"7",
					"8",
					"9",
					"10",
					"11",
					"12",
					"13",
					"14",
					"15"
			});
		}

		@Override
		public int getMetadata(int damage) {
			return damage;
		}
	}
}
