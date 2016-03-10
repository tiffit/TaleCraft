package de.longor.talecraft.blocks;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TCTileEntity extends TileEntity implements IInvokeSource, ICommandSender, TCIBlockCommandReceiver {
	private boolean isTileEntityInitialized = false;
	private Scriptable nativeObject;
	
	public void setPos(BlockPos pos) {
		super.setPos(pos);
		
		if(!isTileEntityInitialized) {
			nativeObject = TaleCraft.globalScriptManager.createNewBlockScope(this.worldObj, this.pos);
			this.init();
			isTileEntityInitialized = true;
		}
	}
	
	public void init() {
		
	}
	
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    	readFromNBT_do(compound);
    }

    public void writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	writeToNBT_do(compound);
    }
    
    public abstract void readFromNBT_do(NBTTagCompound comp);
    public abstract void writeToNBT_do(NBTTagCompound comp);
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		// empty
		
		if(command.equals("re_init")) {
			isTileEntityInitialized = false;
			setPos(pos);
			return;
		}
		
		TaleCraft.logger.info("Unknown block command '" + command + "' @ " + getPos());
	}
	
	@Override
	public final Scriptable getInvokeScriptScope() {
		return nativeObject;
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}
	
	@Override
	public void addChatMessage(IChatComponent message) {
		// nope!
	}
	
	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
		return true;
	}
	
	@Override
	public BlockPos getInvokePosition() {
		return this.pos;
	}
	
	@Override
	public BlockPos getPosition() {
		return this.pos;
	}
	
	@Override
	public Vec3 getPositionVector() {
		return new Vec3(this.pos.getX(),this.pos.getY(),this.pos.getZ());
	}
	
	@Override
	public World getInvokeWorld() {
		return this.worldObj;
	}
	
	@Override
	public World getEntityWorld() {
		return this.worldObj;
	}
	
	@Override
	public Entity getCommandSenderEntity() {
		return null; // nope, can't implement!
	}
	
	@Override
	public boolean sendCommandFeedback() {
		return false; // nope
	}
	
	@Override
	public void setCommandStat(Type type, int amount) {
		// nope
	}
	
	@Override
	public ICommandSender getInvokeAsCommandSender() {
		return this;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 2048; // 128 blocks!
    }
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return TaleCraft.instance.proxy.isBuildMode() ? (pass == 0) : false;
    }
	
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }
    
    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt) {
    	NBTTagCompound comp = pkt.getNbtCompound();
    	readFromNBT_do(comp);
    }
    
	@Override
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
    }
	
}
