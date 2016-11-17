package talecraft.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import talecraft.TaleCraft;

public class GlobalContextFactory extends ContextFactory {
	@Override
	protected Context makeContext() {
		Context cx = super.makeContext();
		TaleCraft.globalScriptManager.contextCreation(cx);
		return cx;
	}
}