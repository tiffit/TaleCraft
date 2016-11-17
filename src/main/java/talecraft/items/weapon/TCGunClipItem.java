package talecraft.items.weapon;

public abstract class TCGunClipItem extends TCWeaponItem{
	
	public TCGunClipItem(){
		this.setMaxStackSize(64);
	}
	
	public abstract int clipSize();
	
	public abstract void onFire();

}
