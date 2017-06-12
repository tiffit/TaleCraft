package talecraft;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import talecraft.entity.EntityMovingBlock;
import talecraft.entity.EntityPoint;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.projectile.EntityBomb;
import talecraft.entity.projectile.EntityBombArrow;
import talecraft.entity.projectile.EntityBoomerang;
import talecraft.entity.projectile.EntityBullet;
import talecraft.entity.projectile.EntityKnife;

public class TaleCraftEntities {
	public static void init() {
		register("point", EntityPoint.class, 256, 20, false);
		register("bomb", EntityBomb.class, 128, 1, true);
		register("npc", EntityNPC.class, 128, 1, true);
		register("bullet", EntityBullet.class, 128, 1, true);
		register("bombarrow", EntityBombArrow.class, 128, 1, true);
		register("boomerang", EntityBoomerang.class, 128, 1, true);
		register("knife", EntityKnife.class, 128, 1, true);
		register("movingblock", EntityMovingBlock.class, 128, 1, true);
	}
	
	private static int ID = 0;
	
	private static void register(String name, Class<? extends Entity> entity, int trackingrange, int updatefrequency, boolean sendVelocityUpdates){
		EntityRegistry.registerModEntity(new ResourceLocation("talecraft", name), entity, "tc_" + name, ID++, "talecraft", trackingrange, updatefrequency, sendVelocityUpdates);
	}
}
