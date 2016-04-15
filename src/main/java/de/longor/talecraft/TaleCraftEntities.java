package de.longor.talecraft;

import de.longor.talecraft.entities.EntityPoint;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tiffit.talecraft.entity.NPC.EntityNPC;
import tiffit.talecraft.entity.throwable.EntityBomb;

public class TaleCraftEntities {
	public static void init() {

		int tc_point_id = "tc_point".hashCode();
		EntityRegistry.registerModEntity(EntityPoint.class, "tc_point", tc_point_id, "talecraft", 256, 20, false);

		EntityRegistry.registerModEntity(EntityBomb.class, "tc_bomb", 0, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityNPC.class, "tc_NPC", 1, "talecraft", 128, 1, true);
		EntityRegistry.registerEgg(EntityNPC.class, 0xffffff, 0x000000);
		
	}
}
