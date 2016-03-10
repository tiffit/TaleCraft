package de.longor.talecraft.invoke;

public interface IScriptInvoke extends IInvoke {
	public String getScriptName();
	public String getScript();
	public void reloadScript();
}
