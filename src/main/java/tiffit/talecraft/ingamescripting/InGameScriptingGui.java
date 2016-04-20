package tiffit.talecraft.ingamescripting;

import java.util.List;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkinType;

public class InGameScriptingGui extends QADGuiScreen {

	List<InGameScriptTransport> scripts;
	QADScrollPanel panel;
	InGameScriptTransport selected;
	
	public InGameScriptingGui(List<InGameScriptTransport> scripts){
		this.scripts = scripts;
	}
	
	public void buildGui() {
		panel = new QADScrollPanel();
		panel.setPosition(0, 0);
		panel.setSize(200, 200);
		this.addComponent(panel);

		final int rowHeight = 20;
		panel.setViewportHeight((scripts.size()+1) * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;

		int yOff = 1;
		for(final InGameScriptTransport script : scripts) {
			QADButton component = QADFACTORY.createButton(script.name, 2, yOff, 200 - 8, null);
			component.simplified = true;
			component.textAlignment = 0;
			component.setAction( new Runnable() {
				@Override public void run() {
					selected = script;
					removeAllComponents();
					buildEditor();
				}
			});
			panel.addComponent(component);
			yOff += rowHeight;
		}
		QADButton component = QADFACTORY.createButton("New Script", 2, yOff, 200 - 8, null);
		component.simplified = true;
		component.textAlignment = 0;
		component.setAction( new Runnable() {
			@Override public void run() {
//				selected = script;
//				removeAllComponents();
//				buildEditor();
			}
		});
		panel.addComponent(component);
		yOff += rowHeight;
	}
	
	private void buildEditor(){
		addComponent(new QADLabel("In-Game Script Editor", 2, 2));
		addComponent(new QADScriptField(fontRendererObj, 10, 10, 300, 200, selected.content));
	}
	
	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}
	
	
	
}
