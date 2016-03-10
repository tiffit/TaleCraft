package de.longor.talecraft.client;

import java.util.UUID;

import org.lwjgl.Sys;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.client.gui.misc.GuiConfirmOpenLinkTCOverride;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor.RemoteEntityDataLink;
import de.longor.talecraft.client.network.PlayerDataMergeMessageHandler;
import de.longor.talecraft.client.network.StringNBTCommandMessageHandler;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.PushRenderableFactory;
import de.longor.talecraft.items.CopyItem;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientNetworkHandler {
	private final ClientProxy proxy;
	
	public ClientNetworkHandler(ClientProxy clientProxy) {
		proxy = clientProxy;
	}
	
	public void init() {
		SimpleNetworkWrapper net = TaleCraft.instance.network;
		net.registerMessage(new StringNBTCommandMessageHandler(), StringNBTCommand.class, 0x01, Side.CLIENT);
		net.registerMessage(new PlayerDataMergeMessageHandler(), PlayerNBTDataMerge.class, 0x02, Side.CLIENT);
	}
	
	public void handleClientCommand(final String command, final NBTTagCompound data) {
		
		if(command.equals("client.network.join")) {
			TaleCraft.logger.info("Sending TaleCraft data to server...");
			
			String tccommand = "join acknowledged";
			NBTTagCompound tcdata = new NBTTagCompound();
			TaleCraft.network.sendToServer(new StringNBTCommand(tccommand, tcdata));
			
			proxy.settings.send();
			return;
		}
		
		if(command.equals("client.debug.track.invoke")) {
			proxy.getInvokeTracker().trackInvoke(data);
			return;
		}
		
		if(command.equals("client.gui.editor.entity")) {
			final UUID uuid = UUID.fromString(data.getString("entityUUID"));
			final NBTTagCompound entity = data.getCompoundTag("entityData");
			
			// Open the GUI in the next tick.
			proxy.sheduleClientTickTask(new Runnable() {
				@Override public void run() {
					RemoteEntityDataLink dataLink = new RemoteEntityDataLink() {
						UUID entityUUID = uuid;
						@Override public void updateData(NBTTagCompound entityData) {
							NBTTagCompound compound = new NBTTagCompound();
							compound.setString("entityUUID", entityUUID.toString());
							compound.setTag("entityData", entityData);
							
							String cmd = "server.data.entity.merge";
							StringNBTCommand command = new StringNBTCommand(cmd, compound);
							TaleCraft.network.sendToServer(command);
						}
					};
					proxy.mc.displayGuiScreen(new GuiEntityEditor(entity, dataLink));
				}
			});
			return;
		}
		
		if(command.equals("client.gui.openurl")) {
			final String url = data.getString("url");
			
			// This is possibly a stupid idea...
			if(data.getBoolean("force")) {
                Sys.openURL(url);
				return;
			}
			
			// Open the GUI in the next tick.
			proxy.sheduleClientTickTask(new Runnable() {
				@Override public void run() {
					GuiConfirmOpenLink gui = new GuiConfirmOpenLinkTCOverride(new GuiYesNoCallback() {
						@Override public void confirmClicked(boolean result, int id) {
					        if (id == 31102009) {
					            if (result) {
					                Sys.openURL(url);
					            }
					            proxy.mc.displayGuiScreen(null);
					        }
						}
					}, url, 31102009, true);
					
					proxy.mc.displayGuiScreen(gui);
				}
			});
			return;
		}
		
		
		
		if(command.equals("item.copy.trigger")) {
			proxy.sheduleClientTickTask(new Runnable() {
				@Override
				public void run() {
					CopyItem copy = TaleCraftItems.copy;
					ItemStack stack = new ItemStack(copy);
					copy.onItemRightClick(stack, proxy.mc.theWorld, proxy.mc.thePlayer);
				}
			});
		}
		
		if(command.equals("client.render.renderable.push")) {
			ITemporaryRenderable renderable = PushRenderableFactory.parsePushRenderableFromNBT(data);
			if(renderable != null && proxy.isBuildMode()) {
				proxy.getRenderer().addTemporaryRenderer(renderable);
			}
			return;
		}
		
		if(command.equals("client.render.renderable.clear")) {
			proxy.getRenderer().clearTemporaryRenderers();
			return;
		}
		
//		if(command.equals("switchShader") && Boolean.FALSE.booleanValue()) {
//			final String sh = data.getString("shaderName");
//			clientTickQeue.offer(new Runnable() {
//				String shader = sh;
//				@Override
//				public void run() {
//					System.out.println("SWITCH : " + shader);
//
//					Field[] fields = mc.entityRenderer.getClass().getDeclaredFields();
//					Field shaderResourceLocations = null;
//					for(Field field : fields) {
//						System.out.println("entityRenderer."+field.getName() + " : " + field.getType());
//					}
//				}
//			});
//			return;
//		}
		
		TaleCraft.logger.info("Received Command -> " + command + ", with data: " + data);
		// XXX: Implement more Server->Client commands.
		
	}
	
	public static final String makeBlockDataMergeCommand(BlockPos position) {
		return "server.data.block.merge:"+position.getX() + " " + position.getY() + " " + position.getZ();
	}
	
	public static final String makeBlockCommand(BlockPos position) {
		return "server.data.block.command:"+position.getX() + " " + position.getY() + " " + position.getZ();
	}
	
}
