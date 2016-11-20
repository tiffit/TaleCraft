package talecraft;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import talecraft.entity.EntityPoint;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.projectile.EntityBomb;
import talecraft.entity.projectile.EntityBombArrow;
import talecraft.entity.projectile.EntityBoomerang;
import talecraft.entity.projectile.EntityBullet;
import talecraft.entity.projectile.EntityKnife;

public class TaleCraftEntities {
	public static void init() {

		int tc_point_id = "tc_point".hashCode();
		EntityRegistry.registerModEntity(EntityPoint.class, "tc_point", tc_point_id, "talecraft", 256, 20, false);
		EntityRegistry.registerModEntity(EntityBomb.class, "tc_bomb", 0, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityNPC.class, "tc_NPC", 1, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityBullet.class, "tc_bullet", 2, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityBombArrow.class, "tc_bombarrow", 3, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityBoomerang.class, "tc_boomerang", 4, "talecraft", 128, 1, true);
		EntityRegistry.registerModEntity(EntityKnife.class, "tc_knife", 5, "talecraft", 128, 1, true);
		EntityRegistry.registerEgg(EntityNPC.class, 0x663300, 0x996633);
		
	}
}
