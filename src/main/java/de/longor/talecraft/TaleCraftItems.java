package de.longor.talecraft;

import java.util.List;

import com.google.common.collect.Lists;

import de.longor.talecraft.items.CopyItem;
import de.longor.talecraft.items.CutItem;
import de.longor.talecraft.items.EraserItem;
import de.longor.talecraft.items.FillerItem;
import de.longor.talecraft.items.InstaKillItem;
import de.longor.talecraft.items.MetaSwapperItem;
import de.longor.talecraft.items.NudgeItem;
import de.longor.talecraft.items.PasteItem;
import de.longor.talecraft.items.SpawnPointItem;
import de.longor.talecraft.items.TeleporterItem;
import de.longor.talecraft.items.VoxelBrushItem;
import de.longor.talecraft.items.WandItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TaleCraftItems {
	public static final List<Item> ALL_TC_ITEMS = Lists.newArrayList();

	public static WandItem wand;
	public static FillerItem filler;
	public static EraserItem eraser;
	public static TeleporterItem teleporter;
	public static InstaKillItem instakill;
	public static VoxelBrushItem voxelbrush;
	public static NudgeItem nudger;
	public static CopyItem copy;
	public static PasteItem paste;
	public static CutItem cut;
	public static MetaSwapperItem metaswapper;
	public static SpawnPointItem spawnpoint;

	public static void init() {
		wand = register(new WandItem(), "wand");
		filler = register(new FillerItem(), "filler");
		eraser = register(new EraserItem(), "eraser");
		teleporter = register(new TeleporterItem(), "teleporter");
		instakill = register(new InstaKillItem(), "instakill");
		voxelbrush = register(new VoxelBrushItem(), "voxelbrush");
		nudger = register(new NudgeItem(), "nudger");
		copy = register(new CopyItem(), "copy");
		paste = register(new PasteItem(), "paste");
		cut = register(new CutItem(), "cut");
		metaswapper = register(new MetaSwapperItem(), "metaswapper");
		spawnpoint = register(new SpawnPointItem(), "spawnpoint");
	}

	private static <T extends Item> T register(T item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		ALL_TC_ITEMS.add(item);
		return item;
	}

}
