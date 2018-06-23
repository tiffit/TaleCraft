package talecraft;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Instrument;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import talecraft.items.CameraItem;
import talecraft.items.CopyItem;
import talecraft.items.CustomPaintingItem;
import talecraft.items.CutItem;
import talecraft.items.DecoratorItem;
import talecraft.items.EraserItem;
import talecraft.items.FillerItem;
import talecraft.items.InstaKillItem;
import talecraft.items.MetaSwapperItem;
import talecraft.items.MovingBlockCreator;
import talecraft.items.EntityCloneItem;
import talecraft.items.NPCEditorItem;
import talecraft.items.NudgeItem;
import talecraft.items.PasteItem;
import talecraft.items.SpawnPointItem;
import talecraft.items.TeleporterItem;
import talecraft.items.TriggerItem;
import talecraft.items.VoxelatorItem;
import talecraft.items.WandItem;
import talecraft.items.weapon.BombArrowItem;
import talecraft.items.weapon.BombItem;
import talecraft.items.weapon.BoomerangItem;
import talecraft.items.weapon.KnifeItem;
import talecraft.items.weapon.PistolClipItem;
import talecraft.items.weapon.PistolItem;
import talecraft.items.weapon.RifleClipItem;
import talecraft.items.weapon.RifleItem;
import talecraft.items.weapon.ShotgunClipItem;
import talecraft.items.weapon.ShotgunItem;
import talecraft.items.world.HeartItem;
import talecraft.items.world.KeyItem;
import talecraft.items.world.TCInstrumentItem;
import talecraft.items.world.TCWorldItem;

@EventBusSubscriber
public class TaleCraftItems {
	public static final List<Item> ALL_TC_ITEMS = Lists.newArrayList();
	private static IForgeRegistry<Item> registry;

	public static WandItem wand;
	public static FillerItem filler;
	public static EraserItem eraser;
	public static TeleporterItem teleporter;
	public static InstaKillItem instakill;
	public static VoxelatorItem voxelbrush;
	public static NudgeItem nudger;
	public static CopyItem copy;
	public static PasteItem paste;
	public static CutItem cut;
	public static MetaSwapperItem metaswapper;
	public static SpawnPointItem spawnpoint;
	public static NPCEditorItem npceditor;
	public static EntityCloneItem entityclone;
	public static CameraItem camera;
	public static DecoratorItem decorator;
	public static TriggerItem trigger;
	public static CustomPaintingItem custompainting;
	public static MovingBlockCreator movingblockcreator;
	
	public static KeyItem silverKey;
	public static KeyItem goldKey;
	public static HeartItem heart;
	public static BombItem bomb;
	
	public static TCWorldItem goldCoin;
	public static TCWorldItem silverCoin;
	public static TCWorldItem emeraldCoin;
	
	public static PistolItem pistol;
	public static PistolClipItem pistolClip;
	public static RifleItem rifle;
	public static RifleClipItem rifleClip;
	public static ShotgunItem shotgun;
	public static ShotgunClipItem shotgunClip;
	public static BombArrowItem bombArrow;
	public static BoomerangItem boomerang;
	public static KnifeItem knife;
	
	public static TCInstrumentItem harp;
	public static TCInstrumentItem guitar;
	public static TCInstrumentItem drums;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		registry = event.getRegistry();	
		
		wand = register(new WandItem(), "wand");
		filler = register(new FillerItem(), "filler");
		eraser = register(new EraserItem(), "eraser");
		teleporter = register(new TeleporterItem(), "teleporter");
		instakill = register(new InstaKillItem(), "instakill");
		voxelbrush = register(new VoxelatorItem(), "voxelbrush");
		nudger = register(new NudgeItem(), "nudger");
		copy = register(new CopyItem(), "copy");
		paste = register(new PasteItem(), "paste");
		cut = register(new CutItem(), "cut");
		metaswapper = register(new MetaSwapperItem(), "metaswapper");
		spawnpoint = register(new SpawnPointItem(), "spawnpoint");
		npceditor = register(new NPCEditorItem(), "npceditor");
		entityclone = register(new EntityCloneItem(), "npcclone");
		camera = register(new CameraItem(), "camera");
		decorator = register(new DecoratorItem(), "decorator");
		trigger = register(new TriggerItem(), "trigger");
		custompainting = register(new CustomPaintingItem(), "custompainting");
		movingblockcreator = register(new MovingBlockCreator(), "movingblockcreator");
		
		silverKey = register(new KeyItem(), "silverkey");
		goldKey = register(new KeyItem(), "goldkey");
		bomb = register(new BombItem(), "bomb");
		heart = register(new HeartItem(), "heart");
		goldCoin = register(new TCWorldItem(), "goldcoin");
		silverCoin = register(new TCWorldItem(), "silvercoin");
		emeraldCoin = register(new TCWorldItem(), "emeraldcoin");
		
		pistolClip = register(new PistolClipItem(), "pistolclip");
		pistol = register(new PistolItem(), "pistol");
		rifleClip = register(new RifleClipItem(), "rifleclip");
		rifle = register(new RifleItem(), "rifle");
		shotgunClip = register(new ShotgunClipItem(), "shotgunclip");
		shotgun = register(new ShotgunItem(), "shotgun");
		bombArrow = register(new BombArrowItem(), "bombarrow");
		boomerang = register(new BoomerangItem(), "boomerang");
		knife = register(new KnifeItem(), "knife");
		
		harp = register(new TCInstrumentItem(Instrument.PIANO), "harp");
		guitar = register(new TCInstrumentItem(Instrument.BASSGUITAR), "guitar");
		drums = register(new TCInstrumentItem(Instrument.BASSDRUM), "drums");
		
		MinecraftForge.EVENT_BUS.register(boomerang);
		
		registerItemBlocks();
	}
	
	private static void registerItemBlocks() {
		for(Block block : TaleCraftBlocks.blocks) {
			if(TaleCraftBlocks.customItemBlocks.contains(block)) {
				registry.register(new TaleCraftBlocks.ItemBlockBlankBlock(block).setRegistryName(block.getRegistryName()));
			} else {
				registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
			}
		}
	}

	private static <T extends Item> T register(T item, String name) {
		item.setUnlocalizedName(name);
		registry.register(item.getRegistryName() == null ? item.setRegistryName(name) : item);
		ALL_TC_ITEMS.add(item);
		return item;
	}
}
