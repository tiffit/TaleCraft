package talecraft.script.wrappers.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class PlayerObjectWrapper extends EntityObjectWrapper{

	private EntityPlayer player;
	
	public PlayerObjectWrapper(EntityPlayer player){
		super(player);
		this.player = player;
	}
	
	public void sendMessage(String message){
		player.sendMessage(new TextComponentString(message));
	}
	
	@Override
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
