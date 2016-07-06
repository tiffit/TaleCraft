package de.longor.talecraft.client;

import org.lwjgl.input.Keyboard;

import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.nbt.GuiNBTEditor;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class ClientKeyboardHandler {
	private final ClientProxy proxy;
	private final Minecraft mc;
	private static final String category = "key.categories." + Reference.MOD_ID;
	
	private KeyBinding mapSettingsBinding;
	private KeyBinding buildModeBinding;
	private KeyBinding visualizationBinding;
	private KeyBinding nbt;

	public ClientKeyboardHandler(ClientProxy clientProxy) {
		proxy = clientProxy;
		mc = Minecraft.getMinecraft();

		mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, category);
		buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, category);
		visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, category);
		nbt = new KeyBinding("key.nbt", Keyboard.KEY_N, category);

		// register all keybindings
		ClientRegistry.registerKeyBinding(mapSettingsBinding);
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
		ClientRegistry.registerKeyBinding(nbt);
		
	}

	public void on_key(KeyInputEvent event) {
		//opens the NBT editor
		if(nbt.isPressed() && nbt.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()){
			InventoryPlayer player = mc.thePlayer.inventory;
			if(player.getCurrentItem() != null) FMLCommonHandler.instance().showGuiScreen(new GuiNBTEditor(player.getCurrentItem().getTagCompound()));
			else mc.thePlayer.addChatMessage(new TextComponentString(TextFormatting.RED + "You must be holding something to use the NBT Editor"));
			return;
		}
		
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			proxy.getRenderer().setVisualizationMode(proxy.getRenderer().getVisualizationMode().next());
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
