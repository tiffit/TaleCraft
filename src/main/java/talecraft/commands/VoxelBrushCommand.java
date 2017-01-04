package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import talecraft.items.VoxelatorItem;
import talecraft.util.GObjectTypeHelper;

public class VoxelBrushCommand extends CommandBase {

	@Override
	public String getName() {
		return "tc_voxelbrush";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "?";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Entity entity = sender.getCommandSenderEntity();

		if(entity == null)
			throw new CommandException("This command can only be run by a player.", entity);

		if(!(entity instanceof EntityPlayerMP))
			throw new CommandException("This command can only be run by a player.", entity);

		EntityPlayerMP player = (EntityPlayerMP) entity;

		ItemStack stack = player.getHeldItemMainhand();

		if(stack == null)
			throw new CommandException("No voxelbrush equipped in main hand.", player);

		if(!(stack.getItem() instanceof VoxelatorItem))
			throw new CommandException("No voxelbrush equipped in main hand.", player, stack);

		if(args.length == 0)
			throw new CommandException("Syntax error: No arguments given.", args, args.length);

		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound itemStackNbtTagCompound = stack.getTagCompound();

		if(!itemStackNbtTagCompound.hasKey("vbData")) {
			NBTTagCompound vbData = new NBTTagCompound();
			itemStackNbtTagCompound.setTag("vbData", vbData);
		}

		NBTTagCompound vbData = itemStackNbtTagCompound.getCompoundTag("vbData");

		String arg0 = args[0];

		if(arg0.equalsIgnoreCase("action")) {
			exec_action(sender, args, vbData);
		}

		if(arg0.equalsIgnoreCase("shape")) {
			exec_shape(sender, args, vbData);
		}//"shape"
	}

	private void exec_action(ICommandSender sender, String[] args, NBTTagCompound vbData) throws CommandException {
		NBTTagCompound actionTag = vbData.getCompoundTag("action");

		if(args[1].equals("replace")) {
			String[] a = GObjectTypeHelper.findBlockState_retAStr(args[2]);

			if(a == null) {
				throw new CommandException("Block type not found: " + args[2]);
			}

			String blockID = a[0];
			String blockMeta = a[3];

			// TODO: Change the code that handles the block type information.

			actionTag.setString("type", "replace");
			actionTag.setString("blockID", blockID);
			actionTag.setString("blockMeta", blockMeta);
		}

		if(args[1].equals("maskreplace")) {
			if(args.length != 4) {
				throw new SyntaxErrorException("Wrong amaount of parameters: //maskreplace <block> <mask>");
			}

			String[] aR = GObjectTypeHelper.findBlockState_retAStr(args[2]);
			String[] aM = GObjectTypeHelper.findBlockState_retAStr(args[3]);

			if(aR == null) {
				throw new CommandException("Block type for replace not found: " + args[2]);
			}

			if(aM == null) {
				throw new CommandException("Block type for mask not found: " + args[3]);
			}

			String blockID = aR[0];
			String blockMeta = aR[3];

			String maskBlockID = aM[0];
			String maskBlockMeta = aM[3];

			// TODO: Change the code that handles the block type information.

			actionTag.setString("type", "maskreplace");
			actionTag.setString("blockID", blockID);
			actionTag.setString("blockMeta", blockMeta);
			actionTag.setString("mask_blockID", maskBlockID);
			actionTag.setString("mask_blockMeta", maskBlockMeta);
		}

		if(args[1].equals("grassify")) {
			actionTag.setString("type", "grassify");
		}

		if(args[1].equals("erase")) {
			actionTag.setString("type", "erase");
		}
	}

	private void exec_shape(ICommandSender sender, String[] args, NBTTagCompound vbData) throws CommandException {
		NBTTagCompound shapeTag = vbData.getCompoundTag("shape");

		if(args[1].equals("offset")) {
			int ox = CommandBase.parseInt(args[2], -64, 64);
			int oy = CommandBase.parseInt(args[3], -64, 64);
			int oz = CommandBase.parseInt(args[4], -64, 64);
			shapeTag.setInteger("offsetX", ox);
			shapeTag.setInteger("offsetY", oy);
			shapeTag.setInteger("offsetZ", oz);
		}

		if(args[1].equals("box")) {
			shapeTag.setString("type", "box");

			if(args.length == 3) {
				int e = CommandBase.parseInt(args[2], 0, 64);
				shapeTag.setInteger("width", e);
				shapeTag.setInteger("height", e);
				shapeTag.setInteger("length", e);
			}

			if(args.length == 4) {
				int e = CommandBase.parseInt(args[2], 0, 64);
				int ey = CommandBase.parseInt(args[3], 0, 64);
				shapeTag.setInteger("width", e);
				shapeTag.setInteger("height", ey);
				shapeTag.setInteger("length", e);
			}

			if(args.length == 5) {
				int ex = CommandBase.parseInt(args[2], 0, 64);
				int ey = CommandBase.parseInt(args[3], 0, 64);
				int ez = CommandBase.parseInt(args[4], 0, 64);
				shapeTag.setInteger("width", ex);
				shapeTag.setInteger("height", ey);
				shapeTag.setInteger("length", ez);
			}
		}//"box"

		if(args[1].equals("cylinder")) {
			shapeTag.setString("type", "cylinder");

			if(args.length == 4) {
				int ey = CommandBase.parseInt(args[2], 0, 64);
				shapeTag.setInteger("height", ey);

				String str = args[3];

				if(str.startsWith("d")) {
					str = str.substring(1);
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, 1, 128) / 2d);
				} else {
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, .5, 64));
				}
			}
		}//"cylinder"

		if(args[1].equals("hollowcylinder")) {
			shapeTag.setString("type", "hollowcylinder");
			shapeTag.setDouble("hollow", 1);

			// TODO: Option to change cylinder caps on/off

			if(args.length >= 4) {
				int ey = CommandBase.parseInt(args[2], 0, 64);
				shapeTag.setInteger("height", ey);

				String str = args[3];
				double rad = 0;

				if(str.startsWith("d")) {
					str = str.substring(1);
					shapeTag.setDouble("radius", rad = CommandBase.parseDouble(str, 1, 128) / 2d);
				} else {
					shapeTag.setDouble("radius", rad = CommandBase.parseDouble(str, .5, 64));
				}

				if(args.length >= 5) {
					String hstr = args[4];
					shapeTag.setDouble("hollow", CommandBase.parseDouble(hstr, 1, rad - 2));
				}
			}
		}//"cylinder"

		if(args[1].equals("sphere")) {
			shapeTag.setString("type", "sphere");

			if(args.length > 2) {
				String str = args[2];

				if(str.startsWith("d")) {
					str = str.substring(1);
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, 1, 128) / 2d);
				} else {
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, .5, 64));
				}
			}
		}//"sphere"

		if(args[1].equals("hollowsphere")) {
			shapeTag.setString("type", "hollowsphere");
			shapeTag.setDouble("hollow", 1);

			if(args.length > 2) {
				String str = args[2];

				if(str.startsWith("d")) {
					str = str.substring(1);
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, 1, 128) / 2d);
				} else {
					shapeTag.setDouble("radius", CommandBase.parseDouble(str, .5, 64));
				}

				if(args.length > 3) {
					String hstr = args[3];
					shapeTag.setDouble("hollow", CommandBase.parseDouble(hstr, 1, 3));
				}
			}
		}//"hollowsphere"
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"shape", "action"});
		}

		if(args.length >= 1) {
			if(args[0].equals("shape") && args.length <= 2 ) {
				return getListOfStringsMatchingLastWord(args, new String[] {"box", "cylinder", "sphere"});
			}

			// func_175762_a(args, Block.blockRegistry.getKeys())

			if(args.length == 2 && args[0].equals("action")) {
				return getListOfStringsMatchingLastWord(args, new String[] {"replace", "maskreplace", "grassify", "erase"});
			}

			if(args.length >= 2 && args[0].equals("action") && args[1].equals("replace")) {
				return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}

			if(args.length >= 2 && args[0].equals("action") && args[1].equals("maskreplace")) {
				return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}

			if(args.length >= 5 && args[0].equals("action") && args[1].equals("maskreplace")) {
				return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}
		}

		return Collections.emptyList();
	}

}
