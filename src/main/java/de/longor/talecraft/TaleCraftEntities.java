package de.longor.talecraft;

import de.longor.talecraft.entities.EntityPoint;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TaleCraftEntities {
	
	static void init() {
		
		int tc_point = "tc_point".hashCode();
		EntityRegistry.registerModEntity(EntityPoint.class, "tc_point", tc_point, "talecraft", 256, 20, false);
        
	}
	
}
