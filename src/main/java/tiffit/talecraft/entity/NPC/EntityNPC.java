package tiffit.talecraft.entity.NPC;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.google.common.base.Predicates;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import tiffit.talecraft.packet.NPCScriptUpdatePacket;

public class EntityNPC extends EntityCreature implements IEntityAdditionalSpawnData, IInvokeSource{

	private EntityPlayer targetPlayer;
	private NPCData data;
	private NBTTagCompound scriptdata;
	private String interactInvoke;
	private String updateInvoke;
	private String deathInvoke;
	private Scriptable scope;

	public static enum NPCType{
		Passive, Neutral, Aggressive;
	}
	
	public EntityNPC(World world) {
		super(world);
		interactInvoke = "";
		updateInvoke = "";
		deathInvoke = "";
		scriptdata = new NBTTagCompound();
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIAttackMelee(this, data.getSpeed(), true));
		this.tasks.addTask(2, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	
	public boolean moveToPos(double x, double y, double z, float speed){
		return getNavigator().tryMoveToXYZ(x, y, z, speed);
	}
	
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(data.getSpeed());
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }
	
	@Override
	public EntityLivingBase getAttackTarget() {
		if(data.getType() == NPCType.Passive) return null;
		else if(data.getType() == NPCType.Aggressive){
			return super.getAttackTarget();
		}else if(attackingPlayer != null){
				return attackingPlayer;
		}
		return null;
	}
	
	@Override
	public void entityInit(){
		super.entityInit();
		data = new NPCData();
		enablePersistence();
        this.stepHeight = 1f;
	}
	
	public NBTTagCompound getScriptData(){
		return scriptdata;
	}
	
	public void setScriptData(NBTTagCompound tag){
		scriptdata = tag;
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
		return data.getInvStack(slot);
	}
	
	@Override
	public float getAIMoveSpeed() {
		return (float) data.getSpeed();
	}
	
	public NPCData getNPCData(){
		return data;
	}
	
	public void setScriptInteractName(String name){
		interactInvoke = name;
	}
	
	public void setScriptUpdateName(String name){
		updateInvoke = name;
	}
	
	public void setScriptDeathName(String name){
		deathInvoke = name;
	}
	
	public void setNPCData(NBTTagCompound tag){
		if(worldObj.isRemote) return;
		data = NPCData.fromNBT(tag);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(data.getSpeed());
		for(Entity ent : this.worldObj.getEntities(EntityPlayerMP.class, Predicates.notNull())){
			EntityPlayerMP player = (EntityPlayerMP) ent;
			player.connection.sendPacket(new S16PacketEntityLook(this.getEntityId(), (byte) this.rotationYaw, (byte) this.rotationPitch, this.onGround));
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
		if(server){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(interactInvoke);
			if(!scriptInvoke.getScriptName().isEmpty()){
				scope = TaleCraft.globalScriptManager.createNewNPCScope(this, stack, player);
				Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
			}
			String message = data.getMessage();
			if(!message.isEmpty()){
				message = message.replace("%player%", player.getName());
				if(data.shouldIncludeNameInMessage()) message = data.getName() + ": " + message;
				player.addChatMessage(new TextComponentString(message));
			}
		}
	}
	
	public boolean attackEntityAsMob(Entity ent){
		if(ent instanceof EntityPlayerMP && data.getType() != NPCType.Passive){
			EntityPlayerMP player = (EntityPlayerMP) ent;
			player.attackEntityFrom(DamageSource.causeMobDamage(this), data.getDamage());
			this.setLastAttacker(ent);
			return true;
		}
		this.setLastAttacker(ent);
		return false;
	}
	
	private void handleEditorInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand, boolean server){
		if(server){
			TaleCraft.network.sendTo(new NPCScriptUpdatePacket(this.getEntityId(), interactInvoke, updateInvoke, deathInvoke), (EntityPlayerMP) player);
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
		if(data.isMovable())super.applyEntityCollision(entity);
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag){
		super.readEntityFromNBT(tag);
		data = NPCData.fromNBT(tag.getCompoundTag("npcdata"));
		scriptdata = tag.getCompoundTag("scriptdata");
		updateInvoke = tag.getString("updateInvokeStr");
		interactInvoke = tag.getString("interactInvokeStr");
		deathInvoke = tag.getString("deathInvokeStr");
	}
	
	//Prevents the NPC from dropping whatever they are holding and wearing
	@Override
	protected boolean canDropLoot() {
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag){
		super.writeEntityToNBT(tag);
		tag.setTag("npcdata", data.toNBT());
		tag.setTag("scriptdata", scriptdata);
		tag.setString("updateInvokeStr", updateInvoke);
		tag.setString("interactInvokeStr", interactInvoke);
		tag.setString("deathInvokeStr", deathInvoke);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(!this.worldObj.isRemote){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(updateInvoke);
			if(!scriptInvoke.getScriptName().isEmpty()){
				scope = TaleCraft.globalScriptManager.createNewNPCScope(this);
				Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
			}
		}
		EntityPlayer lookPlayer = lookAtPlayer(5, 2);
		if(data.doEyesFollow() && lookPlayer != null){
			this.getLookHelper().setLookPositionWithEntity(lookPlayer, 30.0F, 30.0F);
		}else{
			this.rotationYaw = data.getYaw();
			this.setRotationYawHead(data.getYaw());
			this.rotationPitch = data.getPitch();
		}
	}

	private EntityPlayer lookAtPlayer(int range, int rangeY){
		List<Entity> closeEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - range, this.posY - rangeY, this.posZ - range, this.posX + range, this.posY + rangeY, this.posZ + range));
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

	@Override
	public Scriptable getInvokeScriptScope() {
		return scope;
	}

	@Override
	public ICommandSender getInvokeAsCommandSender() {
		return this;
	}

	@Override
	public BlockPos getInvokePosition() {
		return this.getPosition();
	}

	@Override
	public World getInvokeWorld() {
		return this.getEntityWorld();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(new FileScriptInvoke(interactInvoke));
		invokes.add(new FileScriptInvoke(updateInvoke));
		invokes.add(new FileScriptInvoke(deathInvoke));
	}
	
	public boolean getCanSpawnHere(){
		return false;
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 0.5f;
		color[2] = 0.0f;
	}
	
	@Override
	public void setDead() {
		if(deathInvoke != null && deathInvoke.length() > 0){
			scope = TaleCraft.globalScriptManager.createNewNPCScope(this);
			Invoke.invoke(new FileScriptInvoke(deathInvoke), this, null, EnumTriggerState.IGNORE);
		}
		super.setDead();
	}

}