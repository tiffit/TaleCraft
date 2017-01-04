package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import talecraft.TaleCraft;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.util.CommandArgumentParser;

public class VisualizeCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_vz";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "< ? >";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandArgumentParser parser = new CommandArgumentParser(args);

		String action = parser.consume_string("Could not parse action.");

		// CLEAR ALL TEMPORABLES
		if(action.equals("clear")) {
			TaleCraft.network.sendToAll(new StringNBTCommandPacket("client.render.renderable.clear"));
			return;
		}

		// CREATE SHAPE TEMPORABLE
		if(action.equals("sh")) {
			execute_sh(sender, args, parser);
			return;
		}

		// CREATE ENTITY SELECTOR TEMPORABLE
		// Currently disabled.
		//		if(action.equals("es")) {
		//			execute_es(sender, args, parser);
		//			return;
		//		}

	}

	private void execute_sh(ICommandSender sender, String[] args, CommandArgumentParser parser) throws CommandException {
		String shape = parser.consume_string("Could not parse shape.");
		String color = parser.consume_string("Could not parse color.");

		// Everything starting with 'c_' means 'centered on command-sender'.
		// Everything else is normal.

		if(shape.equals("chunk")) {
			BlockPos pos = sender.getPosition();
			int chunkX = MathHelper.floor(pos.getX() / 16f);
			int chunkZ = MathHelper.floor(pos.getZ() / 16f);
			chunkX *= 16;
			chunkZ *= 16;

			for(int i = 0; i < 16; i++)
			{
				int chunkY = i * 16;
				int[] box = new int[]{
						chunkX, chunkY, chunkZ,
						chunkX+16, chunkY+16, chunkZ+16
				};

				NBTTagCompound pktdata = new NBTTagCompound();
				pktdata.setString("type", "box");
				pktdata.setIntArray("box", box);
				pktdata.setInteger("color", parseColor(color));
				TaleCraft.network.sendToAll(new StringNBTCommandPacket("client.render.renderable.push", pktdata));
			}


			return;
		}

		if(shape.equals("c_bx")) {
			int sw = 1;//x-size
			int sh = 1;//y-size
			int sl = 1;//z-size

			if(parser.remaining() == 1) {
				int i = parser.consume_int("Could not parse extent.", 0, 1024);
				sw = sh = sl = i;
			}

			if(parser.remaining() == 2) {
				int horizontal = parser.consume_int("Could not parse horizontal extent.", 0, 1024);
				int vertical = parser.consume_int("Could not parse vertical extent.", 0, 1024);
				sw = sl = horizontal;
				sh = vertical;
			}

			if(parser.remaining() == 3) {
				sw = parser.consume_int("Could not parse x-extent.", 0, 1024);
				sh = parser.consume_int("Could not parse y-extent.", 0, 1024);
				sl = parser.consume_int("Could not parse z-extent.", 0, 1024);
			}

			BlockPos center = sender.getPosition();
			int x = center.getX();
			int y = center.getY();
			int z = center.getZ();

			int[] box = new int[]{
					x - sw, y - sh, z - sl,
					x + sw, y + sh, z + sl
			};

			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "box");
			pktdata.setIntArray("box", box);
			pktdata.setInteger("color", parseColor(color));
			TaleCraft.network.sendToAll(new StringNBTCommandPacket("client.render.renderable.push", pktdata));
		}

	}

	public void execute_es(ICommandSender sender, String[] args, CommandArgumentParser parser) throws CommandException {
		String selector = parser.consume_string("Could not parse selector.");
		String color = parser.consume_string("Could not parse color.");

		NBTTagCompound pktdata = new NBTTagCompound();
		pktdata.setString("type", "selector");
		pktdata.setString("selector", selector);
		pktdata.setInteger("positionX", sender.getPosition().getX());
		pktdata.setInteger("positionY", sender.getPosition().getY());
		pktdata.setInteger("positionZ", sender.getPosition().getZ());
		pktdata.setInteger("color", parseColor(color));
		TaleCraft.network.sendToAll(new StringNBTCommandPacket("client.render.renderable.push", pktdata));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length <= 1) {
			return getListOfStringsMatchingLastWord(args, "clear", "sh", "es");
		}

		if(args[0].equals("sh") && args.length == 2) {
			return getListOfStringsMatchingLastWord(args, "c_bx", "chunk");
		}

		if(args[0].equals("sh") && args.length == 3) {
			return getListOfStringsMatchingLastWord(args, "white", "black", "red", "green", "blue", "yellow", "orange", "purple");
		}

		return Collections.emptyList();
	}

}
