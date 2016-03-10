package de.longor.talecraft.script.wrappers.potion;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.potion.PotionEffect;

public class PotionEffectObjectWrapper implements IObjectWrapper {
	private PotionEffect potionEffect;
	
	public PotionEffectObjectWrapper(PotionEffect potionEffect) {
		this.potionEffect = potionEffect;
	}
	
	public PotionEffect internal() {
		return potionEffect;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
}
