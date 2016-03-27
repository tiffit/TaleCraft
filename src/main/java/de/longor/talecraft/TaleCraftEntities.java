package de.longor.talecraft;

import de.longor.talecraft.entities.EntityPoint;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class TaleCraftEntities {
	public static void init() {

		int tc_point_id = "tc_point".hashCode();
		EntityRegistry.registerModEntity(EntityPoint.class, "tc_point", tc_point_id, "talecraft", 256, 20, false);

	}
}
