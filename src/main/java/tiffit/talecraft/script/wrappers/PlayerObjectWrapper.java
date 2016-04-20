package tiffit.talecraft.script.wrappers;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.entity.EntityObjectWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class PlayerObjectWrapper implements IObjectWrapper {

	private EntityPlayer player;
	
	public PlayerObjectWrapper(EntityPlayer player){
		this.player = player;
	}
	
	@Override
	public Object internal() {
		return player;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public void sendMessage(String message){
		player.addChatComponentMessage(new TextComponentString(message));
	}
	
	public String getName(){
		return player.getDisplayNameString();
	}
	
	public PlayerInventoryObjectWrapper getInventory(){
		return new PlayerInventoryObjectWrapper(player.inventory);
	}
	
	public EntityObjectWrapper getAsEntity(){
		return new EntityObjectWrapper(player);
	}
	
	public double getX(){
		return player.posX;
	}
	
	public double getY(){
		return player.posY;
	}

	public double getZ(){
		return player.posZ;
	}
	
	
}
