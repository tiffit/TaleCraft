package de.longor.talecraft.client.commands;

import java.util.List;

import com.google.common.collect.Lists;

import sun.nio.cs.MS1250;
import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.TaleCraftCommands;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.util.GObjectTypeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class FULLDEBUGPRINTCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_fulldebugprint";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ClientProxy.shedule(new Runnable() {
			@Override public void run() {
				fullPrint(TaleCraft.proxy.asClient(), Minecraft.getMinecraft());
			}
		});
	}
	
	private void fullPrint(ClientProxy asClient, Minecraft minecraft) {
		TaleCraft.logger.info("Printing information about everything TC to console NOW ...");
		
		TaleCraft.logger.info("TaleCraft - " + Reference.MOD_VERSION);
		TaleCraft.logger.info("--------------------------------------------------");
		TaleCraft.logger.info("Client Proxy: " + Reference.CLIENT_PROXY);
		TaleCraft.logger.info("Server Proxy: " + Reference.SERVER_PROXY);
		TaleCraft.logger.info("INSTANCE: " + TaleCraft.instance);
		TaleCraft.logger.info("Proxy: " + TaleCraft.proxy);
		TaleCraft.logger.info("Logger: " + TaleCraft.logger);
		TaleCraft.logger.info("Network: " + TaleCraft.network);
		TaleCraft.logger.info("Container: " + TaleCraft.container);
		TaleCraft.logger.info("Script Manager: " + TaleCraft.globalScriptManager);
		TaleCraft.logger.info("Event Handler, FML: " + TaleCraft.fmlEventHandler);
		TaleCraft.logger.info("Event Handler, F  : " + TaleCraft.forgeEventHandler);
		
		TaleCraft.logger.info("--------------------------------------------------");
		
		TaleCraft.logger.info("Printing information about ALL blocks ...");
		
		List<Block> blocks = TaleCraftBlocks.getBlockList();
		for(Block block : blocks) {
			TaleCraft.logger.info("Block -> " + block.getUnlocalizedName() + ", TE?" + block.hasTileEntity(block.getDefaultState()) + ", STATE?" + block.getDefaultState());
		}
		
		TaleCraft.logger.info("--------------------------------------------------");
		
		TaleCraft.logger.info("Printing information about ALL items (including block items)...");
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		List<Item> items = TaleCraftItems.ALL_TC_ITEMS;
		for(Item item : items) {
			TaleCraft.logger.info("Item -> " + item + " / " + item.getUnlocalizedName());
			
			List<ItemStack> stacks = Lists.newArrayList();
			item.getSubItems(item, null, stacks);
			
			for(ItemStack stack : stacks) {
				TaleCraft.logger.info("ItemStack -> " + stack.getUnlocalizedName());
				
				IBakedModel model = mesher.getItemModel(stack);
				TaleCraft.logger.info("ItemStack.Model -> " + model);
			}
		}
		
		TaleCraft.logger.info("--------------------------------------------------");
		
		TaleCraft.logger.info("Printing information about ALL commands (including client commands)...");
		
		List<CommandBase> commands = Lists.newArrayList();
		
		commands.addAll(TaleCraftCommands.getCommandList());
		commands.addAll(TaleCraftClientCommands.getCommandList());
		
		for(CommandBase command : commands) {
			TaleCraft.logger.info("Command -> " + command.getCommandName() + ", " + command.getRequiredPermissionLevel());
		}
		
		TaleCraft.logger.info("--------------------------------------------------");
		
		TaleCraft.logger.info("Statistics:");
		TaleCraft.logger.info("Blocks    : " + blocks.size());
		TaleCraft.logger.info("Items     : " + blocks.size() + " + " + (items.size()-blocks.size()) + " = " + items.size());
		TaleCraft.logger.info("Commands  : " + commands.size());
		
	}
}
