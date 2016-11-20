package talecraft.entity.NPC;

public enum EnumNPCModel {

	Player(0.6F, 1.8F, "Steve"),
	Zombie(0.6F, 1.95F, "Zombie"),
	Villager(0.6F, 1.95F, "Villager_Default"),
	Skeleton(0.6F, 1.99F, "Skeleton"),
	Creeper(0.6F, 1.7F, "Creeper"),
	Pig(0.9F, 0.9F, "Pig"),
	Cow(0.9F, 1.4F, "Cow");
	
	private String defaultSkin;
	public final float width;
	public final float height;
	
	private EnumNPCModel(float width, float height, String defaultSkin){
		this.width = width;
		this.height = height;
		this.defaultSkin = defaultSkin;
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
