package talecraft.client.gui.qad;

import java.util.Collection;

public interface QADComponentContainer {
	public int getContainerWidth();
	public int getContainerHeight();
	
	public <T extends QADComponent> T addComponent(T c);
	public Collection<QADComponent> getComponents();
	public QADComponent getComponentByName(String name);
	public void removeAllComponents();
	
	/*
		If getLayout returns NULL, this method is a NO-OP.
		Else, the 'layout()'-method of the QADLayoutManager is invoked.
		This method ignores the isLayoutDirty-method.
	*/
	public void forceRebuildLayout();
	
	/*
		The layout should only be rebuilt if this flag returns true.
		Note: The forceRebuildLayout-method does NOT check this flag.
	*/
	public boolean isLayoutDirty();
	
	/*
		Returns the QADLayoutManager for this container.
		If there is none, NULL will be returned.
	*/
	public QADLayoutManager getLayout();
	
	/*
		Changes the layout manager to the given new layout manager.
		This will immediately call the forceRebuildLayout-method.
	*/
	public void setLayout(QADLayoutManager newLayout);
}
