package talecraft.script.wrappers.potion;

import java.util.List;

import net.minecraft.potion.Potion;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;

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
