package de.longor.talecraft.client.gui.misc;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;

public class GuiYesNoQuestion extends QADGuiScreen {
	String question;
	Runnable yesAction;
	
	QADLabel Qquestion;
	QADButton QyesButton;
	QADButton QnoButton;
	
	public GuiYesNoQuestion(GuiScreen behind, String question, Runnable yesAction) {
		super.setBehind(behind);
		this.question = question;
		this.yesAction = yesAction;
	}
	
	public void buildGui() {
		Qquestion = new QADLabel(question, 0, 0);
		QyesButton = new QADButton(0, 0, 60, "Yes");
		QnoButton = new QADButton(0, 0, 60, "No");
		
		QyesButton.setAction(new Runnable() {
			@Override
			public void run() {
				mc.displayGuiScreen(getBehind());
				yesAction.run();
			}
		});
		QnoButton.setAction(new Runnable() {
			@Override public void run() {
				mc.displayGuiScreen(getBehind());
			}
		});
		
		addComponent(Qquestion);
		addComponent(QyesButton);
		addComponent(QnoButton);
	}
	
	public void layoutGui() {
		
		int centerH = width / 2;
		int centerV = height / 2;
		
		Qquestion.setX(centerH - fontRendererObj.getStringWidth(question));
		Qquestion.setY(centerV - 30);
		
		QyesButton.setX(centerH - QyesButton.getButtonWidth()/2 - 4);
		QyesButton.setY(centerV + 5);
		
		QnoButton.setX(centerH + QnoButton.getButtonWidth()/2 + 4);
		QnoButton.setY(centerV + 5);
	}
	
}
