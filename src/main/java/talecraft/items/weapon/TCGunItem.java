package talecraft.items.weapon;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import talecraft.TaleCraftSounds;
import talecraft.entity.projectile.EntityBullet;

public abstract class TCGunItem extends TCWeaponItem {
	
	public TCGunItem(){
		this.setMaxStackSize(1);
		this.setMaxDamage(getClip().clipSize());
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, this.getMaxDamage()));
			list.add(new ItemStack(this));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		int max = stack.getMaxDamage();
		int ammo = stack.getMaxDamage() - stack.getItemDamage();
		tooltip.add("Ammo: " + ammo + "/" + max);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote || hand == EnumHand.OFF_HAND)return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound tag = stack.getTagCompound();
		if(!tag.hasKey("reloading")) tag.setLong("reloading", world.getTotalWorldTime());
		if(tag.getLong("reloading") > world.getTotalWorldTime()){
			tag.setLong("reloading", tag.getLong("reloading") - 1);
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}
		if(stack.getItemDamage() >= stack.getMaxDamage()){
			stack.setItemDamage(stack.getMaxDamage());
			world.playSound(null, player.getPosition(), TaleCraftSounds.DryFire, SoundCategory.AMBIENT, 3F, 1F);
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}
		if(!tag.hasKey("fireSpeed")) tag.setLong("fireSpeed", world.getTotalWorldTime());
		if(!tag.hasKey("usingDone")) tag.setBoolean("usingDone", true);
		if(isPistol() ? tag.getBoolean("usingDone") : world.getTotalWorldTime() - tag.getLong("fireSpeed") >= fireSpeed()){
			tag.setBoolean("usingDone", false);
			tag.setLong("fireSpeed", world.getTotalWorldTime());
			stack.attemptDamageItem(1, itemRand, (EntityPlayerMP)player);
			fire(world, player);
			if(isPistol())player.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}
	
	protected void fire(World world, EntityPlayer player){
		EntityBullet bullet = new EntityBullet(world, player, getDamage(), range());
		world.playSound(null, player.getPosition(), fireSound(), SoundCategory.AMBIENT, 3F, 1F);
		bullet.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
		world.spawnEntity(bullet);
	}
	
	@Override
		public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft){
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound tag = stack.getTagCompound();
		tag.setBoolean("usingDone", true);
	}
	
	public int getClipInInventory(InventoryPlayer inv){
		for(int i = 0; i < inv.getSizeInventory(); i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			if(stack.getItem() == getClip()) return i;
		}
		return -1; //-1 means the clip doesn't exist
	}
	
	@Override
		public int getMaxItemUseDuration(ItemStack stack){
		return isPistol()? 10000000 : 0;
	}
	
	public SoundEvent reloadSound(){
		return TaleCraftSounds.Reload;
	}
	
	protected abstract TCGunClipItem getClip();
	protected abstract SoundEvent fireSound();
	protected abstract int fireSpeed();
	protected abstract float getDamage();
	
	/**
	 * The range of the gun
	 */
	protected abstract double range();
	
	protected boolean isPistol(){
		return false;
	}

}
