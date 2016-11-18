package talecraft.entity.NPC;

import talecraft.client.entity.npc.models.ModifiedModelSkeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.player.EntityPlayer;

public enum EnumNPCModel {

	Player(new ModelPlayer(0f, false), 0.6F, 1.8F, "Steve"),
	Zombie(new ModelZombie(), 0.6F, 1.95F, "Zombie"),
	Villager(new ModelVillager(0f), 0.6F, 1.95F, "Villager_Default"),
	Skeleton(new ModifiedModelSkeleton(), 0.6F, 1.99F, "Skeleton"),
	Creeper(new ModelCreeper(), 0.6F, 1.7F, "Creeper"),
	Pig(new ModelPig(0f), 0.9F, 0.9F, "Pig"),
	Cow(new ModelCow(), 0.9F, 1.4F, "Cow");
	
	private ModelBase model;
	private String defaultSkin;
	public final float width;
	public final float height;
	
	private EnumNPCModel(ModelBase model, float width, float height, String defaultSkin){
		this.width = width;
		this.height = height;
		model.isChild = false;
		EntityPlayer e;
		model.isRiding = false;
		this.model = model;
		this.defaultSkin = defaultSkin;
	}
	
	public ModelBase getModel(){
		return model;
	}
	
	public EnumNPCSkin getDefaultSkin(){
		return EnumNPCSkin.valueOf(defaultSkin);
	}
	
	public EnumNPCModel rotate(){
		int ordinal = this.ordinal();
		ordinal++;
		if(ordinal >= values().length) ordinal = 0;
		return EnumNPCModel.values()[ordinal];
	}
	
}
