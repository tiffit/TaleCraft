package de.longor.talecraft.script;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;

public class ConsoleOutput implements IObjectWrapper {
	
	public ConsoleOutput() {/*empty stub*/}
	
	public void println(String string) {
		TaleCraft.logger.info(string);
	}
	
	public void println(Object object) {
		TaleCraft.logger.info(object);
	}
	
	public void println(Throwable e) {
		TaleCraft.logger.info(e);
	}
	
	public void println(IObjectWrapper object) {
		TaleCraft.logger.info(object.getClass().getSimpleName() + "@" + object.hashCode() + " : " + object.getOwnPropertyNames());
	}
	
	@Override
	public Object internal() {
		return null;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
}
