package talecraft.items;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import talecraft.entity.NPC.EntityNPC;

public class NPCEditorItem extends TCItem {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// ItemStack stack = player.getHeldItem(hand);
		float lerp = 1F;
		float dist = 7;
		Vec3d start = this.getPositionEyes(lerp, player);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.x * dist, direction.y * dist, direction.z * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);
		if(result != null && result.typeOfHit == Type.BLOCK){
			spawnNPC(world, result.getBlockPos());
			if(!world.isRemote) player.sendMessage(new TextComponentString("NPC spawned!"));
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	private void spawnNPC(World world, BlockPos pos){
		if(!world.isRemote){
			EntityNPC npc = new EntityNPC(world);
			npc.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
			npc.rotationYawHead = npc.rotationYaw;
			npc.renderYawOffset = npc.rotationYaw;
			npc.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(npc)), (IEntityLivingData)null);
			world.spawnEntity(npc);
			npc.playLivingSound();
		}
	}
	

	
}
