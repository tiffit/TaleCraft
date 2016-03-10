package de.longor.talecraft.client.gui.qad;

import java.util.Collection;

public interface QADComponentContainer {
	public <T extends QADComponent> T addComponent(T c);
	public Collection<QADComponent> getComponents();
	public QADComponent getComponentByName(String name);
	public void removeAllComponents();
}
