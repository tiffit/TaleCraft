package de.longor.talecraft.script.wrappers.potion;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.potion.Potion;

public class PotionObjectWrapper implements IObjectWrapper {
	private Potion potion;

	public PotionObjectWrapper(Potion potion) {
		this.potion = potion;
	}

	@Override
	public Potion internal() {
		return potion;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public int getId() {
		return Potion.getIdFromPotion(potion);
	}

	public String getName() {
		return potion.getName();
	}

}
