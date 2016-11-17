package talecraft.script.wrappers.item;

import java.util.List;

import net.minecraft.item.Item;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;

public class ItemObjectWrapper implements IObjectWrapper {
	private Item item;

	public ItemObjectWrapper(Item item) {
		this.item = item;
	}

	@Override
	public Item internal() {
		return item;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getUnlocalizedName() {
		return item.getUnlocalizedName();
	}

}
