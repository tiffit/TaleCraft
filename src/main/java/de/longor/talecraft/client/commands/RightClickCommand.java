package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class RightClickCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_click";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "<x> <y> <z>";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 3) {
			return;
		}
		
		final int min = -6000000;
		final int max = +6000000;
		
		final double baseX = sender.getPositionVector().xCoord;
		final double baseY = sender.getPositionVector().yCoord;
		final double baseZ = sender.getPositionVector().zCoord;
		
		// <x> <y> <z>
		final double xCoord = this.parseDouble(baseX, args[0], min, max, true);
		final double yCoord = this.parseDouble(baseY, args[1],   0, 255, true);
		final double zCoord = this.parseDouble(baseZ, args[2], min, max, true);
		
		final BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
		
		ClientProxy.shedule(new Runnable() {
			@Override public void run() {
				WorldClient world = Minecraft.getMinecraft().theWorld;
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				
				block.onBlockActivated(world, pos, state, player, EnumFacing.UP, 0, 0, 0);
			}
		});
	}
}
