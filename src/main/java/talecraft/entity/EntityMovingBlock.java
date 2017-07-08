package talecraft.entity;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import talecraft.TaleCraft;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.IInvoke;
import talecraft.invoke.IInvokeSource;
import talecraft.invoke.Invoke;
import talecraft.network.packets.MovingBlockDataUpdatePacket;

public class EntityMovingBlock extends EntityFallingBlock implements IEntityAdditionalSpawnData, IInvokeSource {

	private boolean invisible;
	private boolean pushable;
	private boolean collision;
	private boolean no_gravity;
	private float mount_y_offset;
	private String onTick = "", onCollide = "", onInteract = "", onDeath = "";
	
	private Scriptable scope;
	
	public EntityMovingBlock(World worldIn) {
		super(worldIn);
		onCreate();
	}
	
	public void updateData(NBTTagCompound data){
		readEntityFromNBT(data);
		if(!world.isRemote)TaleCraft.network.sendToDimension(new MovingBlockDataUpdatePacket(this.getEntityId(), data), getEntityWorld().provider.getDimension());
	}
	
	private void onCreate(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Block", "minecraft:stone");
		readEntityFromNBT(tag);
	}
	
	@Override
	public double getMountedYOffset() {
		return super.getMountedYOffset() + mount_y_offset;
	}
	
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return invisible ? false : super.isInRangeToRenderDist(distance);
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {
		if(!no_gravity)super.fall(distance, damageMultiplier);
	}
	
	@Override
	public void setPosition(double x, double y, double z){
		if(getBlock() != null){
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			AxisAlignedBB aabb = getBlock().getBoundingBox(getEntityWorld(), getPosition());
			this.width = (float) (aabb.maxX - aabb.minX);
			this.height = (float) (aabb.maxY - aabb.minY);
		}
		super.setPosition(x, y, z);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		collision = compound.getBoolean("collision");
		invisible = compound.getBoolean("invisible");
		pushable = compound.getBoolean("pushable");
		no_gravity = compound.getBoolean("no_gravity");
		onTick = compound.getString("onTick");
		onCollide = compound.getString("onCollide");
		onInteract = compound.getString("onInteract");
		onDeath = compound.getString("onDeath");
		mount_y_offset = compound.getFloat("mount_y_offset");
		setInvisible(invisible);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("collision", collision);
		compound.setBoolean("invisible", invisible);
		compound.setBoolean("pushable", pushable);
		compound.setBoolean("no_gravity", no_gravity);
		compound.setString("onTick", onTick);
		compound.setString("onCollide", onCollide);
		compound.setString("onInteract", onInteract);
		compound.setString("onDeath", onDeath);
		compound.setFloat("mount_y_offset", mount_y_offset);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		if(!collision)return null;
		return getEntityBoundingBox();
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return collision;
	}
	
	@Override
	public boolean canBePushed() {
		return pushable;
	}
	
	@Override
	public boolean isInvisible() {
		return invisible;
	}
	
	@Override
	public boolean isInvisibleToPlayer(EntityPlayer player) {
		return invisible;
	}
	
	@Override
	public String getName() {
		return "Moving Block [" + getBlock().getBlock().getRegistryName() + "]";
	}
	
	@Override
	public boolean hasCustomName() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		if(onTick != null && !onTick.equals("") && !getEntityWorld().isRemote){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(onTick);
			scope = TaleCraft.globalScriptManager.createNewMovingBlock(this);
			Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
		}
		vanillaUpdateMod();
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if(onInteract != null && !onInteract.equals("") && !getEntityWorld().isRemote){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(onInteract);
			scope = TaleCraft.globalScriptManager.createNewMovingBlock(this, player);
			Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}
	
	@Override
	public void applyEntityCollision(Entity entity) {
		if(onCollide != null && !onCollide.equals("") && !getEntityWorld().isRemote){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(onCollide);
			scope = TaleCraft.globalScriptManager.createNewMovingBlock(this, entity);
			Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
		}
		super.applyEntityCollision(entity);
	}
	
	@Override
	public void setDead() {
		if(onDeath != null && !onDeath.equals("") && !getEntityWorld().isRemote){
			FileScriptInvoke scriptInvoke = new FileScriptInvoke(onDeath);
			scope = TaleCraft.globalScriptManager.createNewMovingBlock(this);
			Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
		}
		super.setDead();
	}
	
	private void vanillaUpdateMod(){
		if (getBlock().getMaterial() == Material.AIR){
			this.setDead();
		}
		else{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			if (!this.hasNoGravity())
			{
				this.motionY -= 0.03999999910593033D;
			}
			if(onGround || no_gravity)this.motionY = 0;
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;

			if (!this.getEntityWorld().isRemote){
				if (this.onGround){
					if (this.getEntityWorld().isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
					if (BlockFalling.canFallThrough(this.getEntityWorld().getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) && !no_gravity){
						this.onGround = false;
						return;
					}
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;
				}
			}
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		readEntityFromNBT(ByteBufUtils.readTag(additionalData));
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
		return getPosition();
	}

	@Override
	public World getInvokeWorld() {
		return this.getEntityWorld();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(new FileScriptInvoke(this.onTick));
		invokes.add(new FileScriptInvoke(this.onCollide));
		invokes.add(new FileScriptInvoke(this.onInteract));
		invokes.add(new FileScriptInvoke(this.onDeath));
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 0.5f;
		color[2] = 0.0f;
	}
	
	public static class EntityMovingBlockRenderFactory implements IRenderFactory <EntityFallingBlock>{
		
		@Override
		public Render<EntityFallingBlock> createRenderFor(RenderManager manager) {
			return new RenderFallingBlock(manager);
		}
		
	}

}
