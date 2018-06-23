package talecraft.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraftTabs;

public class TCItem extends Item {

	public TCItem() {
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return EnumActionResult.PASS;

		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));

		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	// Warning: Forge Method
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		// By returning TRUE, we prevent damaging the entity being hit.
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean canItemEditBlocks() {
		return false;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		player.world.notifyBlockUpdate(pos, player.world.getBlockState(pos), player.world.getBlockState(pos), 0);
		return false;
	}
	
	public final NBTTagCompound getNBTfromItemStack(ItemStack stack) {
		NBTTagCompound comp = null;
		
		if(stack.hasTagCompound()) {
			comp = stack.getTagCompound();
		} else {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}
		
		return comp;
	}
	
	
	protected Vec3d getPositionEyes(float partialTicks, EntityPlayer player) {
		if(partialTicks == 1.0F) {
			return new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		} else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialTicks + player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}
	
	public RayTraceResult rayTrace(double blockReachDistance, EntityPlayer player){
		Vec3d vec3d = getPositionEyes(1.0F, player);
		Vec3d vec3d1 = player.getLook(1.0F);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
		return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
	}
	
	public static final boolean isDoubleCall(ItemStack stack, long currentWorldTime) {
		NBTTagCompound compound = stack.getTagCompound();
		if(compound!=null){
			// Double Call Prevention Hack
			long timeNow = currentWorldTime;
			long timePre = compound.getLong("DCPH");
			
			if(timeNow == timePre) {
				return true;
			} else {
				compound.setLong("DCPH", timeNow);
				return false;
			}
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setLong("DCPH", currentWorldTime);
			stack.setTagCompound(nbt);
		}
		return false;
	}

}
