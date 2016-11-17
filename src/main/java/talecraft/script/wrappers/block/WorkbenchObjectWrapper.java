package talecraft.script.wrappers.block;

import java.util.List;

import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.util.WorkbenchManager;

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
