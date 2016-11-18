package talecraft.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import talecraft.Reference;
import talecraft.TaleCraft;
import talecraft.client.gui.nbt.GuiNBTEditor;
import talecraft.items.weapon.TCGunClipItem;
import talecraft.items.weapon.TCGunItem;
import talecraft.network.packets.GunReloadPacket;
import talecraft.network.packets.TriggerItemPacket;
import talecraft.proxy.ClientProxy;

public class ClientKeyboardHandler {
	private final ClientProxy proxy;
	private final Minecraft mc;
	private static final String category = "key.categories." + Reference.MOD_ID;
	
	private KeyBinding mapSettingsBinding;
	private KeyBinding buildModeBinding;
	private KeyBinding visualizationBinding;
	private KeyBinding nbt;
	private KeyBinding reload;
	private KeyBinding triggerItem;
	
	public ClientKeyboardHandler(ClientProxy clientProxy) {
		proxy = clientProxy;
		mc = Minecraft.getMinecraft();

		mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, category);
		buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, category);
		visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, category);
		nbt = new KeyBinding("key.nbt", Keyboard.KEY_N, category);
		reload = new KeyBinding("key.reload", Keyboard.KEY_R, category);
		triggerItem = new KeyBinding("key.triggeritem", Keyboard.KEY_V, category);

		// register all keybindings
		ClientRegistry.registerKeyBinding(mapSettingsBinding);
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
		ClientRegistry.registerKeyBinding(nbt);
		ClientRegistry.registerKeyBinding(reload);
		ClientRegistry.registerKeyBinding(triggerItem);
	}

	public void on_key(KeyInputEvent event) {
		//opens the NBT editor
		if(nbt.isPressed() && nbt.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()){
			InventoryPlayer player = mc.thePlayer.inventory;
			if(player.getCurrentItem() != null) FMLCommonHandler.instance().showGuiScreen(new GuiNBTEditor(player.getCurrentItem().getTagCompound()));
			else mc.thePlayer.addChatMessage(new TextComponentString(TextFormatting.RED + "You must be holding something to use the NBT Editor"));
			return;
		}
		if(triggerItem.isPressed() && triggerItem.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()){
			TaleCraft.network.sendToServer(new TriggerItemPacket(mc.thePlayer.getUniqueID()));
			return;
		}
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			proxy.getRenderer().setVisualizationMode(proxy.getRenderer().getVisualizationMode().next());
		}
		if(reload.isPressed() && reload.isKeyDown()) {
			ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
			if(stack != null && stack.getItem() instanceof TCGunItem){
				TCGunItem gun = (TCGunItem) stack.getItem();
				int index = gun.getClipInInventory(mc.thePlayer.inventory);
				if(index != -1){
					// XXX: Why is clip unused?
					TCGunClipItem clip = (TCGunClipItem) mc.thePlayer.inventory.getStackInSlot(index).getItem();
					if(stack.getItemDamage() > 0){
						mc.theWorld.playSound(mc.thePlayer, mc.thePlayer.getPosition(), gun.reloadSound(), SoundCategory.AMBIENT, 5F, 1F);
						TaleCraft.network.sendToServer(new GunReloadPacket(mc.thePlayer.getUniqueID()));
					}
				}
			}
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
