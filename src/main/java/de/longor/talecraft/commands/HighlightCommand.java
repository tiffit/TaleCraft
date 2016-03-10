package de.longor.talecraft.commands;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.CommandArgumentParser;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class HighlightCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_highlight";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "< ? >";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CommandArgumentParser parser = new CommandArgumentParser(args);
		parser.commandSenderPosition = sender.getPositionVector();
		
		String action = parser.consume_string("Couldn't parse highlight action!");
		
		if(action.equals("clear")) {
			// TODO: clear highlights!
			sender.addChatMessage(new ChatComponentText("ERROR: highlight clearing not yet implemented."));
			return;
		}
		
		if(action.equals("entity")) {
			// highlight a entity/multiple entities
			double duration = parser.consume_double("Couldn't parse duration!", 0.0000000001d, 10d);
			String selector = parser.consume_string("Couldn't parse entity selector!");
			
			List<EntityPlayerSP> entities = PlayerSelector.matchEntities(sender, selector, EntityPlayerSP.class);
			
			// TODO: highlight entity/entities!
			
			sender.addChatMessage(new ChatComponentText("ERROR: 'entity' highlighting not yet implemented."));
			return;
		}
		
		if(action.equals("block")) {
			// highlight a block
			
			double duration = parser.consume_double("Couldn't parse duration!", 0.0000000001d, 10d);
			BlockPos blockPos = parser.consume_blockpos("Couldn't parse block position!");
			
			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "highlight.block");
			pktdata.setInteger("pos.x", blockPos.getX());
			pktdata.setInteger("pos.y", blockPos.getY());
			pktdata.setInteger("pos.z", blockPos.getZ());
			pktdata.setDouble("duration", duration);
			TaleCraft.network.sendToAll(new StringNBTCommand("client.render.renderable.push", pktdata));
			return;
		}
		
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"entity","block","clear"});
    	}
    	
    	return null;
    }
	
}
