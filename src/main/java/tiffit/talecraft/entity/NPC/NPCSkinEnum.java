package tiffit.talecraft.entity.NPC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class NPCSkinEnum {

	public static enum NPCSkinType{
		Villager, Other, Blank;
	}
	
	public static enum NPCSkin{
		Blank1(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank1.png"), NPCSkinType.Blank),
		Blank2(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank2.png"), NPCSkinType.Blank),
		Blank3(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank3.png"), NPCSkinType.Blank),
		Blank4(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank4.png"), NPCSkinType.Blank),
		Blank5(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank5.png"), NPCSkinType.Blank),
		Blank6(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank6.png"), NPCSkinType.Blank),
		Blank7(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank7.png"), NPCSkinType.Blank),
		Blank8(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank8.png"), NPCSkinType.Blank),
		Blank9(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank9.png"), NPCSkinType.Blank),
		Blank10(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank10.png"), NPCSkinType.Blank),
		Blank11(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank11.png"), NPCSkinType.Blank),
		Blank12(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank12.png"), NPCSkinType.Blank),
		Blank13(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank13.png"), NPCSkinType.Blank),
		Blank14(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank14.png"), NPCSkinType.Blank),
		Blank15(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank15.png"), NPCSkinType.Blank),
		Blank16(new ResourceLocation("talecraft:textures/entity/NPC/blank/Blank16.png"), NPCSkinType.Blank),
		
		Steve(new ResourceLocation("minecraft:textures/entity/steve.png"), NPCSkinType.Other),
		Alex(new ResourceLocation("minecraft:textures/entity/alex.png"), NPCSkinType.Other),
		GreenSteve(new ResourceLocation("talecraft:textures/entity/NPC/other/greensteve.png"), NPCSkinType.Other),
		RedSteve(new ResourceLocation("talecraft:textures/entity/NPC/other/redsteve.png"), NPCSkinType.Other),
		Tiffit(new ResourceLocation("talecraft:textures/entity/NPC/other/tiffit.png"), NPCSkinType.Other),
		Longor1996(new ResourceLocation("talecraft:textures/entity/NPC/other/longor1996.png"), NPCSkinType.Other),
		Invisible(null, NPCSkinType.Other),
		Male(new ResourceLocation("talecraft:textures/entity/NPC/villager/male.png"), NPCSkinType.Villager, "NicoKing60"),
		Female(new ResourceLocation("talecraft:textures/entity/NPC/villager/female.png"), NPCSkinType.Villager, "NicoKing60");
	
		private ResourceLocation resloc;
		private NPCSkinType type;
		private String author;
	
		NPCSkin(ResourceLocation location, NPCSkinType skintype){
			this(location, skintype, null);
		}
	
		NPCSkin(ResourceLocation location, NPCSkinType skintype, String skinauthor){
			resloc = location;
			type = skintype;
			author = skinauthor;
		}
		
		public ResourceLocation getResourceLocation(){
			return resloc;
		}
		
		public NPCSkinType getSkinType(){
			return type;
		}
		
		public boolean hasAuthor(){
			return author != null;
		}
		
		public String getAuthor(){
			return author;
		}
		
		public static NPCSkin[] getSkinsWithType(NPCSkinType type){
			List<NPCSkin> skins = new ArrayList<NPCSkin>();
			for(int i = 0; i < values().length; i++){
				if(values()[i].getSkinType() == type) skins.add(values()[i]);
			}
			return skins.toArray(new NPCSkin[0]);
		}
	}
	
}
