package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SwitchShaderCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_switchShader";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<player> <shader>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(Boolean.TRUE.booleanValue()) {
			throw new CommandException("This command is not yet implemented. :(");
		}
		
		if(args.length != 2) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 2 needed.");
		}
		
		List<EntityPlayerMP> players = PlayerSelector.matchEntities(sender, args[0], EntityPlayerMP.class);
		String shaderName = args[1];
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("shaderName", shaderName);
		StringNBTCommand pkt = new StringNBTCommand("switchShader", nbt);
		
		for(EntityPlayerMP entityPlayerMP : players) {
			TaleCraft.network.sendTo(pkt, entityPlayerMP);
		}
		
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	return null;
    }
	
}
