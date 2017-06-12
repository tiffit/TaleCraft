package talecraft.entity.NPC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public enum EnumNPCSkin {
	Custom(null, null),
	Steve(new ResourceLocation("minecraft:textures/entity/steve.png"), EnumNPCModel.Player),
	Alex(new ResourceLocation("minecraft:textures/entity/alex.png"),  EnumNPCModel.Player),
	Green_Steve(new ResourceLocation("talecraft:textures/entity/NPC/player/greensteve.png"),  EnumNPCModel.Player),
	Red_Steve(new ResourceLocation("talecraft:textures/entity/NPC/player/redsteve.png"),  EnumNPCModel.Player),
	Tiffit(new ResourceLocation("talecraft:textures/entity/NPC/player/tiffit.png"),  EnumNPCModel.Player),
	Longor1996(new ResourceLocation("talecraft:textures/entity/NPC/player/longor1996.png"),  EnumNPCModel.Player),
	Invisible(null,  null),
	Villager(new ResourceLocation("talecraft:textures/entity/NPC/player/villager.png"),  "NicoKing60", EnumNPCModel.Player),
	Modern_Female(new ResourceLocation("talecraft:textures/entity/NPC/player/modern_female.png"), "NicoKing60", EnumNPCModel.Player),
	Knight(new ResourceLocation("talecraft:textures/entity/NPC/player/knight.png"),  "WinterWolfee", EnumNPCModel.Player),
	Knight2(new ResourceLocation("talecraft:textures/entity/NPC/player/knight2.png"),  "WinterWolfee", EnumNPCModel.Player),
	Huntress(new ResourceLocation("talecraft:textures/entity/NPC/player/huntress.png"),  "WinterWolfee", EnumNPCModel.Player),
	Farmer(new ResourceLocation("talecraft:textures/entity/NPC/player/farmer.png"),  "WinterWolfee", EnumNPCModel.Player),
	Priest(new ResourceLocation("talecraft:textures/entity/NPC/player/priest.png"),  "WinterWolfee", EnumNPCModel.Player),
	
	
	Creeper(new ResourceLocation("textures/entity/creeper/creeper.png"), EnumNPCModel.Creeper),
	Zombie(new ResourceLocation("textures/entity/zombie/zombie.png"), EnumNPCModel.Zombie),
	Zombie_Husk(new ResourceLocation("textures/entity/zombie/husk.png"), EnumNPCModel.Zombie),
	
	Skeleton(new ResourceLocation("textures/entity/skeleton/skeleton.png"), EnumNPCModel.Skeleton),
	Wither_Skeleton(new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"), EnumNPCModel.Skeleton),
	Skeleton_Stray(new ResourceLocation("textures/entity/skeleton/stray.png"), EnumNPCModel.Skeleton),
	
	Villager_Butcher(new ResourceLocation("textures/entity/villager/butcher.png"), EnumNPCModel.Villager),
	Villager_Farmer(new ResourceLocation("textures/entity/villager/farmer.png"), EnumNPCModel.Villager),
	Villager_Librarian(new ResourceLocation("textures/entity/villager/librarian.png"), EnumNPCModel.Villager),
	Villager_Priest(new ResourceLocation("textures/entity/villager/priest.png"), EnumNPCModel.Villager),
	Villager_Smith(new ResourceLocation("textures/entity/villager/smith.png"), EnumNPCModel.Villager),
	Villager_Default(new ResourceLocation("textures/entity/villager/villager.png"), EnumNPCModel.Villager),
	Zombie_Pigman(new ResourceLocation("textures/entity/zombie_pigman.png"), EnumNPCModel.Zombie),
	Pig(new ResourceLocation("textures/entity/pig/pig.png"), EnumNPCModel.Pig),
	Cow(new ResourceLocation("textures/entity/cow/cow.png"), EnumNPCModel.Cow),
	Mooshroom(new ResourceLocation("textures/entity/cow/mooshroom.png"), EnumNPCModel.Cow);
	
	private ResourceLocation resloc;
	private EnumNPCModel model;
	private String author;

	private EnumNPCSkin(ResourceLocation location, EnumNPCModel model){
		this(location, null, model);
	}

	private EnumNPCSkin(ResourceLocation location, String skinauthor, EnumNPCModel model){
		resloc = location;
		author = skinauthor;
		this.model = model;
	}
	
	public ResourceLocation getResourceLocation(){
		return resloc;
	}
	
	public EnumNPCModel getModelType(){
		return model;
	}
	
	public boolean hasAuthor(){
		return author != null;
	}
	
	public String getAuthor(){
		return author;
	}
	
	@Override
	public String toString() {
		return name().replace("_", " ");
	}
	
	public static EnumNPCSkin[] getSkinsWithModel(EnumNPCModel type){
		List<EnumNPCSkin> skins = new ArrayList<EnumNPCSkin>();
		for(int i = 0; i < values().length; i++){
			if(values()[i].getModelType() == type) skins.add(values()[i]);
		}
		return skins.toArray(new EnumNPCSkin[0]);
	}
	

}
