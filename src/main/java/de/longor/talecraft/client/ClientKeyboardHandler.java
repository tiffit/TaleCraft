package de.longor.talecraft.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.proxy.ClientProxy;

public class ClientKeyboardHandler {
	private final ClientProxy proxy;
	private final Minecraft mc;
	
	private KeyBinding mapSettingsBinding;
	private KeyBinding buildModeBinding;
	private KeyBinding visualizationBinding;
	
	public ClientKeyboardHandler(ClientProxy clientProxy) {
		proxy = clientProxy;
		mc = Minecraft.getMinecraft();
		
		mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, "key.categories.misc");
		buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, "key.categories.misc");
		visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, "key.categories.misc");
		
		// register all keybindings
		ClientRegistry.registerKeyBinding(mapSettingsBinding);
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
	}

	public void on_key(KeyInputEvent event) {
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			proxy.getRenderer().setVisualizationMode(proxy.getRenderer().getVisualizationMode()+1);
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
