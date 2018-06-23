package talecraft.client.gui.misc;

import net.minecraft.client.gui.GuiScreen;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;

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

	@Override
	public void buildGui() {
		Qquestion = new QADLabel(question, 0, 0);
		QyesButton = new QADButton(0, 0, 60, "Yes");
		QnoButton = new QADButton(0, 0, 60, "No");

		QyesButton.setAction(new Runnable() {
			@Override
			public void run() {
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

	@Override
	public void layoutGui() {

		int centerH = getWidth() / 2;
		int centerV = getHeight() / 2;

		Qquestion.setX(centerH - fontRenderer.getStringWidth(question)/2);
		Qquestion.setY(centerV - 30);

		QyesButton.setX(centerH - QyesButton.getButtonWidth() - 10);
		QyesButton.setY(centerV + 5);

		QnoButton.setX(centerH + 10);
		QnoButton.setY(centerV + 5);
	}

}
