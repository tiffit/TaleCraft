package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.invoke.NullInvoke;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RedstoneTriggerBlockTileEntity extends TCTileEntity {
	IInvoke triggerInvoke;
	
	public RedstoneTriggerBlockTileEntity() {
		triggerInvoke = BlockTriggerInvoke.ZEROINSTANCE;
	}
	
	@Override
	public void init() {
		// don't do anything
	}
    
    public void readFromNBT_do(NBTTagCompound compound) {
    	triggerInvoke = IInvoke.Serializer.read(compound.getCompoundTag("triggerInvoke"));
    }
    
    public void writeToNBT_do(NBTTagCompound compound) {
    	compound.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
    }
    
	public void invokeFromUpdateTick(EnumTriggerState triggerState) {
		if(this.worldObj.isRemote)
			return;
		
		Invoke.invoke(triggerInvoke, this, null, triggerState);
	}
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			Invoke.invoke(triggerInvoke, this, null, EnumTriggerState.ON);
			return;
		}
		
		if(command.equals("reload")) {
			if(triggerInvoke != null && triggerInvoke instanceof FileScriptInvoke) {
				((FileScriptInvoke)triggerInvoke).reloadScript();
			}
			return;
		}
		
		super.commandReceived(command, data);
	}
	
	@Override
	public String getName() {
		return "RedstoneTrigger@"+pos;
	}
	
	@Override
	public String toString() {
		return "RedstoneTriggerTileEntity:{}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}
	
	public IInvoke getInvoke() {
		return triggerInvoke;
	}

//	@Override
//	public void getInvokesAsDataCompounds(List<NBTTagCompound> invokes) {
//		invokes.add(triggerInvoke);
//	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.75f;
		color[1] = 0.0f;
		color[2] = 0.0f;
	}
	
}
