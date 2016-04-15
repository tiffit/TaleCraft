package tiffit.talecraft.entity.NPC;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicates;

import de.longor.talecraft.TaleCraftItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntity.S16PacketEntityLook;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityNPC extends EntityLiving implements IEntityAdditionalSpawnData{

	private NPCData data;
	
	public EntityNPC(World world) {
		super(world);
	}
	
	@Override
	public void entityInit(){
		super.entityInit();
		data = new NPCData();
	}

	@Override
    public Iterable<ItemStack> getArmorInventoryList(){
        return Arrays.<ItemStack>asList(new ItemStack[4]);
    }

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return null;
	}
	
	public NPCData getNPCData(){
		return data;
	}
	
	public void setNPCData(NBTTagCompound tag){
		if(worldObj.isRemote) return;
		data = NPCData.fromNBT(tag);
		for(Entity ent : this.worldObj.getEntities(EntityPlayerMP.class, Predicates.notNull())){
			EntityPlayerMP player = (EntityPlayerMP) ent;
			player.playerNetServerHandler.sendPacket(new S16PacketEntityLook(this.getEntityId(), (byte) this.rotationYaw, (byte) this.rotationPitch, this.onGround));
		}
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand){
		if(stack == null || stack.getItem() != TaleCraftItems.npceditor){
			handleRegularInteraction(player, vec, stack, hand, !player.worldObj.isRemote);
			return EnumActionResult.SUCCESS;
		}
		handleEditorInteraction(player, vec, stack, hand, !player.worldObj.isRemote);
		return EnumActionResult.SUCCESS;
	}

	private void handleRegularInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand, boolean server){
		this.collideWithEntity(null);
		if(server){
			String message = data.getMessage();
			message = message.replace("%player%", player.getName());
			if(data.shouldIncludeNameInMessage()) message = data.getName() + ": " + message;
			player.addChatMessage(new TextComponentString(message));
		}
	}
	
	private void handleEditorInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand, boolean server){
		if(!server){
			Minecraft.getMinecraft().displayGuiScreen(new NPCEditorGui(data, getUniqueID()));
		}
	}
	
	@Override
    public boolean isEntityInvulnerable(DamageSource source){
        return data.isInvulnerable() && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }
	
	@Override
	public String getName(){
		return data.getName();
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
	}
	
	@Override
    protected void collideWithEntity(Entity entity){
		if(data.isMovable())entity.applyEntityCollision(this);
    }
	
	@Override
	public void applyEntityCollision(Entity entity){
		if(data.isMovable()){
			super.applyEntityCollision(entity);
		}
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag){
		super.readEntityFromNBT(tag);
		data = NPCData.fromNBT(tag.getCompoundTag("npcdata"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag){
		super.writeEntityToNBT(tag);
		tag.setTag("npcdata", data.toNBT());
	}
	
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		EntityPlayer lookPlayer = lookAtPlayer();
		if(data.doEyesFollow() && lookPlayer != null){
			this.getLookHelper().setLookPositionWithEntity(lookPlayer, 30.0F, 30.0F);
		}else{
			this.rotationYaw = data.getYaw();
			this.setRotationYawHead(data.getYaw());
			this.rotationPitch = data.getPitch();
		}
	}

	private EntityPlayer lookAtPlayer(){
		List<Entity> closeEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 5, this.posY - 2, this.posZ - 5, this.posX + 5, this.posY + 2, this.posZ + 5));
		for(Entity ent : closeEntities){
			if(ent instanceof EntityPlayer){
				return (EntityPlayer) ent;
			}
		}
		return null;
	}
	
	@Override
	public void writeSpawnData(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		readEntityFromNBT(tag);
	}

}
