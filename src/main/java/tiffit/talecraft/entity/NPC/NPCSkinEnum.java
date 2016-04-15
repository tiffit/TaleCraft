package tiffit.talecraft.entity.NPC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class NPCSkinEnum {

	public static enum NPCSkinType{
		Villager, Other;
	}
	
	public static enum NPCSkin{
		Steve(new ResourceLocation("minecraft:textures/entity/steve.png"), NPCSkinType.Other),
		Alex(new ResourceLocation("minecraft:textures/entity/alex.png"), NPCSkinType.Other),
		GreenSteve(new ResourceLocation("talecraft:textures/entity/NPC/other/greensteve.png"), NPCSkinType.Other),
		RedSteve(new ResourceLocation("talecraft:textures/entity/NPC/other/redsteve.png"), NPCSkinType.Other),
		Tiffit(new ResourceLocation("talecraft:textures/entity/NPC/other/tiffit.png"), NPCSkinType.Other),
		Longor1996(new ResourceLocation("talecraft:textures/entity/NPC/other/longor1996.png"), NPCSkinType.Other),
		Invisible(null, NPCSkinType.Other),
		Male(new ResourceLocation("talecraft:textures/entity/NPC/villager/male.png"), NPCSkinType.Villager, "NicoKing60"),
		Female(new ResourceLocation("talecraft:textures/entity/NPC/villager/female.png"), NPCSkinType.Villager, "NicoKing60");
		//GamerKingz(new ResourceLocation("talecraft:textures/entity/NPC/other/gamerkingz.png"), NPCSkinType.Other);
	
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
