package tiffit.talecraft.script.wrappers;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import tiffit.talecraft.util.WorkbenchManager;

public class WorkbenchObjectWrapper implements IObjectWrapper {

	private WorkbenchManager manager;
	
	public WorkbenchObjectWrapper(WorkbenchManager manager) {
		this.manager = manager;
	}

	@Override
	public Object internal() {
		return manager;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public void removeRecipe(int index){
		manager.remove(index);
	}

}
