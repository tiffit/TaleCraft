package de.longor.talecraft.client;

import org.lwjgl.input.Keyboard;

import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.nbt.GuiNBTEditor;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import tiffit.talecraft.packet.InGameScripterRequestPacket;

public class ClientKeyboardHandler {
	private final ClientProxy proxy;
	private final Minecraft mc;
	private static final String category = "key.categories." + Reference.MOD_ID;
	
	private KeyBinding mapSettingsBinding;
	private KeyBinding buildModeBinding;
	private KeyBinding visualizationBinding;
	private KeyBinding nbt;
	private KeyBinding scriptEditor;

	public ClientKeyboardHandler(ClientProxy clientProxy) {
		proxy = clientProxy;
		mc = Minecraft.getMinecraft();

		mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, category);
		buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, category);
		visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, category);
		nbt = new KeyBinding("key.nbt", Keyboard.KEY_N, category);
		scriptEditor = new KeyBinding("key.scriptEditor", Keyboard.KEY_P, category);

		// register all keybindings
		ClientRegistry.registerKeyBinding(mapSettingsBinding);
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
		ClientRegistry.registerKeyBinding(nbt);
		ClientRegistry.registerKeyBinding(scriptEditor);
	}

	public void on_key(KeyInputEvent event) {
		//opens the NBT editor
		if(nbt.isPressed() && nbt.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("test", 1);
			tag.setBoolean("boolean", true);
			FMLCommonHandler.instance().showGuiScreen(new GuiNBTEditor(tag));
			return;
		}
		
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			proxy.getRenderer().setVisualizationMode(proxy.getRenderer().getVisualizationMode()+1);
		}
		
		if(scriptEditor.isPressed() && scriptEditor.isKeyDown() && proxy.isBuildMode()) {
			TaleCraft.network.sendToServer(new InGameScripterRequestPacket());
		}

		// this toggles between buildmode and adventuremode
		if(buildModeBinding.isPressed() && buildModeBinding.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()) {
			TaleCraft.logger.info("Switching GameMode using the buildmode-key.");
			mc.thePlayer.sendChatMessage("/gamemode " + (proxy.isBuildMode() ? "2" : "1"));

			// these bunch of lines delete all display lists,
			// thus forcing the renderer to reupload the world to the GPU
			// (this process only takes several milliseconds)
			TaleCraft.timedExecutor.executeLater(new Runnable() {
				@Override public void run() {
					mc.addScheduledTask(new Runnable(){
						@Override
						public void run() {
							mc.renderGlobal.deleteAllDisplayLists();
						}
					});
				}
			}, 250);
		}

		if(
				mapSettingsBinding.isPressed() &&
				mapSettingsBinding.isKeyDown() &&
				proxy.isBuildMode() &&
				mc.thePlayer != null &&
				mc.theWorld != null
				) {
			// XXX: Disabled functionality.
			// mc.displayGuiScreen(new GuiMapControl());
		}

	}

}
