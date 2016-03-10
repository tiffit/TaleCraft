package de.longor.talecraft.server;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;


public class PlayerMirror {
	public EntityPlayerMP player;
	public NBTTagCompound settings;
	public boolean trackInvokes;
	
	public PlayerMirror(EntityPlayerMP player) {
		this.player = player;
		this.settings = new NBTTagCompound();
	}
	
	public void construct(NBTTagCompound data) {
		
	}
	
	public void updateSettings(NBTTagCompound data) {
		settings.merge(data);
		trackInvokes = settings.getBoolean("invoke.tracker");
	}
	
	public void trackInvoke(IInvokeSource source, IInvoke invoke) {
		if(!trackInvokes) return;
		
		float[] colorS = new float[3];
		float[] colorT = new float[3];
		
		source.getInvokeColor(colorS);
		invoke.getColor(colorT);
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setFloat("isr", colorS[0]);
		tagCompound.setFloat("isg", colorS[1]);
		tagCompound.setFloat("isb", colorS[2]);
		tagCompound.setFloat("itr", colorT[0]);
		tagCompound.setFloat("itg", colorT[1]);
		tagCompound.setFloat("itb", colorT[2]);
		TaleCraft.network.sendTo(new StringNBTCommand("client.debug.track.invoke", tagCompound), player);
	}
	
}
