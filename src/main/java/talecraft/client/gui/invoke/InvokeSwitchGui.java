package talecraft.client.gui.invoke;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.CommandInvoke;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.NullInvoke;

public class InvokeSwitchGui extends QADGuiScreen {
	int invokeTypeFlags;
	IInvokeHolder holder;
	GuiScreen screen;

	public InvokeSwitchGui(int invokeTypeFlags, IInvokeHolder holder, GuiScreen screen) {
		this.invokeTypeFlags = invokeTypeFlags;
		this.holder = holder;
		this.screen = screen;
	}

	@Override
	public void buildGui() {
		int xOff = 2;
		int yOff = 16;

		addComponent(QADFACTORY.createLabel("Select Invoke Type...", 4, 4));

		{
			addComponent(QADFACTORY.createButton(TextFormatting.ITALIC+"Cancel", xOff, yOff, 120, new Runnable() {
				@Override public void run() {
					mc.displayGuiScreen(screen);
				}
			}).setTooltip("Do not change the invoke type and","return the the previous screen."));
			yOff += 20;
			yOff += 4;
		}

		if((invokeTypeFlags & InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_NULL) != 0) {
			addComponent(QADFACTORY.createButton("None", xOff, yOff, 120, new Runnable() {
				@Override public void run() {
					holder.switchInvokeType(NullInvoke.TYPE);
					mc.displayGuiScreen(null);
				}
			}).setTooltip("No Invoke"));
			yOff += 20;
		}

		if((invokeTypeFlags & InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_BLOCKTRIGGER) != 0) {
			addComponent(QADFACTORY.createButton("Block Trigger", xOff, yOff, 120, new Runnable() {
				@Override public void run() {
					holder.switchInvokeType(BlockTriggerInvoke.TYPE);
					mc.displayGuiScreen(null);
				}
			}).setTooltip("Invoke that triggers all","blocks in a given region."));
			yOff += 20;
		}

		if((invokeTypeFlags & InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_COMMAND) != 0) {
			addComponent(QADFACTORY.createButton("Command", xOff, yOff, 120, new Runnable() {
				@Override public void run() {
					holder.switchInvokeType(CommandInvoke.TYPE);
					mc.displayGuiScreen(null);
				}
			}).setTooltip("Invoke that executes a command."));
			yOff += 20;
		}

		if((invokeTypeFlags & InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_SCRIPTFILE) != 0) {
			addComponent(QADFACTORY.createButton("Script File", xOff, yOff, 120, new Runnable() {
				@Override public void run() {
					holder.switchInvokeType(FileScriptInvoke.TYPE);
					mc.displayGuiScreen(null);
				}
			}).setTooltip("Invoke that executes a script."));
			yOff += 20;
		}

	}

}
