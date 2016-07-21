package de.longor.talecraft.items;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.voxelator.VXAction;
import de.longor.talecraft.voxelator.VXAction.VXActions;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.VXShape;
import de.longor.talecraft.voxelator.VXShape.VXShapes;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.actions.VXActionGrassify;
import de.longor.talecraft.voxelator.actions.VXActionReplace;
import de.longor.talecraft.voxelator.actions.VXActionVariationsReplace;
import de.longor.talecraft.voxelator.predicates.VXPredicateHeightLimit;
import de.longor.talecraft.voxelator.shapes.VXShapeBox;
import de.longor.talecraft.voxelator.shapes.VXShapeCylinder;
import de.longor.talecraft.voxelator.shapes.VXShapeSphere;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.packet.VoxelatorGuiPacket;

public class VoxelBrushItem extends TCItem {

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(world.isRemote)
			return;

		if(world.getGameRules().getBoolean("disableTCVoxelBrush")) {
			return;
		}

		NBTTagCompound stackCompound = stack.getTagCompound();

		if(stackCompound == null) {
			stackCompound = new NBTTagCompound();
			stack.setTagCompound(stackCompound);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(!player.capabilities.isCreativeMode)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(!player.capabilities.allowEdit){
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		if(!stack.hasTagCompound()){
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		NBTTagCompound data = stack.getTagCompound().getCompoundTag("brush");
		float lerp = 1F;
		float dist = 256;

		Vec3d start = this.getPositionEyes(lerp, player);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

		if(result == null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

			if(player.isSneaking()){
				TaleCraft.network.sendTo(new VoxelatorGuiPacket(stack.getTagCompound()), (EntityPlayerMP) player);
				return ActionResult.newResult(EnumActionResult.PASS, stack);
			}else{
				if(data.hasNoTags())
					return ActionResult.newResult(EnumActionResult.PASS, stack);
			}
			VXAction action = null; int action_id = data.getInteger("action");
			if(action_id == 0) action = new VXActionGrassify();
			if(action_id == 1) action = new VXActionReplace(Block.getBlockById(data.getInteger("block_id_0")).getDefaultState());
			if(action_id == 2){
				IBlockState[] blockstates = new IBlockState[data.getInteger("block_size")];
				for(int i = 0; i < blockstates.length; i++){
					blockstates[i] = Block.getStateById(data.getInteger("block_id_" + i));
				}
				action = new VXActionVariationsReplace(blockstates);
			}
			
			boolean hollow = data.getBoolean("hollow");
			VXShape shape = null; int shape_id = data.getInteger("shape");
			if(shape_id == 0) shape = new VXShapeSphere(result.getBlockPos(), data.getFloat("radius"), hollow);
			if(shape_id == 1) shape = new VXShapeBox(result.getBlockPos(), data.getInteger("width"), data.getInteger("height"), data.getInteger("length"), hollow);
			if(shape_id == 2) shape = new VXShapeCylinder(result.getBlockPos(), data.getFloat("radius"), data.getInteger("height"), hollow);
			Voxelator.apply(shape, action_id == 0 ? VXPredicate.newIsSolid() : new VXPredicateHeightLimit(256), action, world);
			return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if(!stack.hasTagCompound()){
			super.addInformation(stack, player, tooltip, advanced);
			return;
		}

		NBTTagCompound data = stack.getTagCompound().getCompoundTag("brush");
		addDesc(data, tooltip);
		super.addInformation(stack, player, tooltip, advanced);
	}

	public Vec3d getPositionEyes(float partialTicks, EntityPlayer player) {
		if(partialTicks == 1.0F) {
			return new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		} else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialTicks + player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void addDesc(NBTTagCompound data, List<String> tooltip) {
		if(data.hasNoTags()){
			tooltip.add(TextFormatting.RED + "Not Defined Yet");
			return;
		}
		VXActions action = VXActions.get(data.getInteger("action"));
		VXShapes shape = VXShapes.get(data.getInteger("shape"));
		String shapeData = "";
		if(shape == VXShapes.Sphere) shapeData = "[r=" + data.getFloat("radius") + "]";
		if(shape == VXShapes.Box) shapeData  = "[w=" + data.getInteger("width") + ",h=" + data.getInteger("height") + ",l="+ data.getInteger("length") + "]";
		if(shape == VXShapes.Cylinder) shapeData = "[r=" + data.getFloat("radius") + ",h=" + data.getInteger("height") + "]";	
		tooltip.add("Shape: " + shape.toString() + shapeData);
		tooltip.add("Action: " + action.toString());
		tooltip.add("");

	}

}
