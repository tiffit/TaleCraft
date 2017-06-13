package talecraft.client.gui.invoke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADComponentContainer;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADButton.ButtonModel;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.CommandInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.IInvoke;
import talecraft.invoke.NullInvoke;
import talecraft.items.WandItem;

public class InvokePanelBuilder {
	public static final int INVOKE_TYPE_EDIT_ALLOWALL = -1;
	public static final int INVOKE_TYPE_EDIT_ALLOW_NULL = 1;
	public static final int INVOKE_TYPE_EDIT_ALLOW_BLOCKTRIGGER = 2;
	public static final int INVOKE_TYPE_EDIT_ALLOW_SCRIPTFILE = 4;
	public static final int INVOKE_TYPE_EDIT_ALLOW_SCRIPTEMBEDDED = 8;
	public static final int INVOKE_TYPE_EDIT_ALLOW_COMMAND = 16;

	/**
	 * Maximum Width: 20 + ?
	 **/
	public static final void build(GuiScreen screen, QADComponentContainer container, int ox, int oy, IInvoke invoke, final IInvokeHolder holder, int invokeTypeFlags) {

		if(invokeTypeFlags != 0) {
			QADButton button = new QADButton(ox, oy, 20, "");
			button.setEnabled(true);
			button.setAction(new InvokeSwitchAction(invokeTypeFlags, holder, screen));
			button.setIcon(QADButton.ICON_INVEDIT);
			button.setTooltip("Change Invoke Type");
			container.addComponent(button);
			ox += 20 + 2;
		}

		if(invoke == null || invoke instanceof NullInvoke) {
			container.addComponent(QADFACTORY.createLabel("Null Invoke", ox, oy + 6));
			return;
		}

		if(invoke instanceof CommandInvoke) {
			build_command(container, ox, oy, (CommandInvoke) invoke, holder);
			return;
		}

		if(invoke instanceof BlockTriggerInvoke) {
			build_blocktrigger(container, ox, oy, (BlockTriggerInvoke) invoke, holder);
			return;
		}

		if(invoke instanceof FileScriptInvoke) {
			build_filescript(container, ox, oy, (FileScriptInvoke) invoke, holder);
		}

	}

	private static void build_command(QADComponentContainer container,
			int ox, int oy, CommandInvoke invoke, final IInvokeHolder holder) {

		final QADTextField scriptName = QADFACTORY.createTextField(invoke.getCommand(), ox+1, oy+2, 100-2);
		scriptName.setTooltip("The command to execute.");
		scriptName.setMaxStringLength(32700);
		container.addComponent(scriptName);

		QADButton buttonApply = QADFACTORY.createButton("Apply", ox+100+2, oy, 40, null);
		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound invokeData = new NBTTagCompound();
				String text = scriptName.getText();
				invokeData.setString("type", "CommandInvoke");
				invokeData.setString("command", text);

				holder.sendInvokeUpdate(invokeData);
			}
		});
		buttonApply.setTooltip("Saves the settings.", "There is no auto-save so make","sure to press this button.");
		container.addComponent(buttonApply);

		//		QADButton buttonExecute = QADFACTORY.createButton("E", ox+100+4+40+2, oy, 20, null);
		//		buttonExecute.setAction(new Runnable() {
		//			@Override public void run() {
		//				holder.sendCommand("execute", null);
		//			}
		//		});
		//		buttonExecute.setTooltip("Prompts the server to","execute the command.");
		//		components.add(buttonExecute);

	}

	private static void build_filescript(QADComponentContainer container,
			int ox, int oy, FileScriptInvoke invoke, final IInvokeHolder holder) {

		final QADTextField scriptName = QADFACTORY.createTextField(invoke.getScriptName(), ox+1, oy+2, 100-2);
		scriptName.setTooltip("The file-name of the script to execute.");
		scriptName.setMaxStringLength(128);
		container.addComponent(scriptName);

		QADButton buttonApply = QADFACTORY.createButton("Apply", ox+100+2, oy, 40, null);
		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound commandData = new NBTTagCompound();
				NBTTagCompound invokeData = new NBTTagCompound();
				commandData.setTag("scriptInvoke", invokeData);

				String text = scriptName.getText();
				invokeData.setString("type", "FileScriptInvoke");
				invokeData.setString("scriptFileName", text);

				holder.sendInvokeUpdate(invokeData);
			}
		});
		buttonApply.setTooltip("Saves the settings.", "There is no auto-save, so make","sure to press this button.");
		container.addComponent(buttonApply);


		QADButton buttonReload = QADFACTORY.createButton("R", ox+100+4+40+2, oy, 20, null);
		buttonReload.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("reload", null);
			}
		});
		buttonReload.setTooltip("Prompts the server to","reload the script.");
		container.addComponent(buttonReload);


		QADButton buttonExecute = QADFACTORY.createButton("E", ox+100+4+40+2+20+2, oy, 20, null);
		buttonExecute.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("execute", null);
			}
		});
		buttonExecute.setTooltip("Prompts the server to","execute the script.");
		container.addComponent(buttonExecute);


		QADButton buttonReloadExecute = QADFACTORY.createButton("R+E", ox+100+4+40+2+20+2+20+2, oy, 24, null);
		buttonReloadExecute.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("reloadexecute", null);
			}
		});
		buttonReloadExecute.setTooltip("Prompts the server to reload","and then execute the script.");
		container.addComponent(buttonReloadExecute);

	}



	private static void build_blocktrigger(QADComponentContainer container, int ox, int oy, final BlockTriggerInvoke invoke, final IInvokeHolder holder) {
		container.addComponent(QADFACTORY.createButton("Set Region", ox, oy, 100, new Runnable() {
			@Override public void run() {
				EntityPlayer player = Minecraft.getMinecraft().player;
				int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

				if(bounds == null){
					Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(TextFormatting.RED+"Error: "+TextFormatting.RESET+"Wand selection is invalid.");
					return;
				}

				NBTTagCompound invokeData = new NBTTagCompound();
				invokeData.setString("type", "BlockTriggerInvoke");
				invokeData.setIntArray("bounds", bounds);

				holder.sendInvokeUpdate(invokeData);
			}
		}).setTooltip(
				"Sets the region that is triggered",
				"when this invoke is run."
				));

		container.addComponent(QADFACTORY.createButton(QADButton.ICON_PLAY, ox+100+2, oy, 20, new Runnable() {
			@Override public void run() {
				holder.sendCommand("trigger", null);
			}
		}).setTooltip("Trigger this invoke."));

		container.addComponent(QADFACTORY.createButton("", ox+100+2+20+2, oy, 50)).setModel(new ButtonModel() {
			int ordinal = invoke.getOnOff().getIntValue();

			@Override public void onClick() {
				ordinal++;

				if(ordinal > 1) {
					ordinal = -2;
				}

				NBTTagCompound invokeData = new NBTTagCompound();
				invokeData.setInteger("state", ordinal);
				holder.sendInvokeUpdate(invokeData);
			}

			@Override public String getText() {
				return EnumTriggerState.get(ordinal).name();
			}

			@Override public ResourceLocation getIcon() {
				// IGNORE
				return null;
			}
			@Override public void setText(String newText) {
				// IGNORE
			}
			
			@Override public void setIcon(ResourceLocation newIcon) {
				// IGNORE
			}
		}).setTooltip("The state of the trigger. Default is ON.");
	}

	public static class InvokeSwitchAction implements Runnable {
		int invokeTypeFlags;
		IInvokeHolder holder;
		GuiScreen screen;

		public InvokeSwitchAction(int invokeTypeFlags, IInvokeHolder holder, GuiScreen screen) {
			this.invokeTypeFlags = invokeTypeFlags;
			this.holder = holder;
			this.screen = screen;
		}

		@Override
		public void run() {
			Minecraft.getMinecraft().displayGuiScreen(new InvokeSwitchGui(invokeTypeFlags, holder, screen));
		}
	}

}
