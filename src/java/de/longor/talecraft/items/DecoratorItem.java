package de.longor.talecraft.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.realmsclient.gui.ChatFormatting;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.items.GuiDecorator;
import de.longor.talecraft.decorator.Decoration;
import de.longor.talecraft.decorator.Decorator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.packet.DecoratorGuiPacket;
import tiffit.talecraft.packet.VoxelatorGuiPacket;

public class DecoratorItem extends TCItem {

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		NBTTagCompound comp = stack.getTagCompound();
		if(comp == null) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		NBTTagCompound tag = stack.getTagCompound().getCompoundTag("decorator_data");
		if(!player.isSneaking()){
			EntityPlayerMP p = (EntityPlayerMP) player;
			if(!p.capabilities.isCreativeMode || !p.capabilities.allowEdit)return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			if(tag.hasNoTags()) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			Decoration decor = Decorator.getDecorationFromString(tag.getString("decor"));
			if(decor == null){
				p.addChatMessage(new TextComponentString(ChatFormatting.RED + "Unknown decoration!"));
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
			int amount = tag.getInteger("amount");
			BlockPos offset = new BlockPos(tag.getInteger("xoff"), tag.getInteger("yoff"), tag.getInteger("zoff"));
			float lerp = 1F;
			float dist = 256;
			Vec3d start = this.getPositionEyes(lerp, p);
			Vec3d direction = p.getLook(lerp);
			Vec3d end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);
			RayTraceResult result = p.worldObj.rayTraceBlocks(start, end, false, false, false);
			BlockPos center = result.getBlockPos().up().add(offset);
			int changes = decor.plant(p.worldObj, getInRadius(center, tag.getInteger("radius"), amount), tag);
		}else{
			if(player.isSneaking()){
				TaleCraft.network.sendTo(new DecoratorGuiPacket(Decorator.getAllDecorations(), stack.getTagCompound()), (EntityPlayerMP) player);
				return ActionResult.newResult(EnumActionResult.PASS, stack);
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	private BlockPos[] getInRadius(BlockPos center, int radius, int amount){
		List<BlockPos> allPos = new ArrayList<BlockPos>();
		BlockPos[] returnPos = new BlockPos[amount];
		int rsqr = radius*radius;
		for(int x = center.getX() - radius; x < center.getX() + radius; x++){
			for(int z = center.getZ() - radius; z < center.getZ() + radius; z++){
				BlockPos current = new BlockPos(x, center.getY(), z);
				if(center.distanceSq(current) < rsqr){
					allPos.add(current);
				}
			}
		}
		for(int i = 0; i < amount; i++){
			int rint = itemRand.nextInt(allPos.size());
			returnPos[i] = allPos.get(rint);
		}
		return returnPos;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if(!stack.hasTagCompound()){
			super.addInformation(stack, player, tooltip, advanced);
			return;
		}

		NBTTagCompound data = stack.getTagCompound().getCompoundTag("decorator_data");
		addDesc(data, tooltip);
		super.addInformation(stack, player, tooltip, advanced);
	}
	
	private static void addDesc(NBTTagCompound data, List<String> tooltip) {
		if(data.hasNoTags()){
			tooltip.add(TextFormatting.RED + "Not Defined Yet");
			return;
		}
		
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
	

}
