package talecraft.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
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
import talecraft.TaleCraft;
import talecraft.network.packets.VoxelatorGuiPacket;
import talecraft.voxelator.VXAction;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.VXShape;
import talecraft.voxelator.Voxelator;

public class VoxelatorItem extends TCItem implements TCITriggerableItem{

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(world.isRemote)
			return;

		if(world.getGameRules().hasRule("disableTCVoxelBrush") && world.getGameRules().getBoolean("disableTCVoxelBrush")) {
			return;
		}

		NBTTagCompound stackCompound = stack.getTagCompound();

		if(stackCompound == null) {
			stackCompound = new NBTTagCompound();
			stack.setTagCompound(stackCompound);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
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
		
		NBTTagCompound data = stack.getTagCompound().getCompoundTag("brush_data");
		
		if(data.hasNoTags())
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		
		float lerp = 1F;
		float dist = 256;
		Vec3d start = this.getPositionEyes(lerp, player);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.x * dist, direction.y * dist, direction.z * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

		if(result == null) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		NBTTagCompound NBTshape = data.getCompoundTag("shape");
		NBTTagCompound NBTfilter = data.getCompoundTag("filter");
		NBTTagCompound NBTaction = data.getCompoundTag("action");
		VXShape shape = Voxelator.newShape(NBTshape, result.getBlockPos());
		VXPredicate filter = Voxelator.newFilter(NBTfilter);
		VXAction action = Voxelator.newAction(NBTaction);
		Voxelator.apply(shape, filter, action, world, player);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if(!stack.hasTagCompound()){
			super.addInformation(stack, player, tooltip, advanced);
			return;
		}

		NBTTagCompound data = stack.getTagCompound().getCompoundTag("brush_data");
		addDesc(data, tooltip);
		super.addInformation(stack, player, tooltip, advanced);
	}

	@Override
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
		NBTTagCompound shape = data.getCompoundTag("shape");
		NBTTagCompound filter = data.getCompoundTag("filter");
		NBTTagCompound action = data.getCompoundTag("action");
		tooltip.add(shape.getString("type"));
		tooltip.add(filter.getString("type"));
		tooltip.add(action.getString("type"));
	}
	
	@Override
	public void trigger(World world, EntityPlayerMP player, ItemStack stack) {
		TaleCraft.network.sendTo(new VoxelatorGuiPacket(stack.getTagCompound().getCompoundTag("brush_data")), player);
	}

}
