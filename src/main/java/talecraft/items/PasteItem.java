package talecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import talecraft.TaleCraft;
import talecraft.clipboard.ClipboardItem;
import talecraft.clipboard.ClipboardTagNames;
import talecraft.server.ServerHandler;
import talecraft.server.ServerMirror;
import talecraft.util.BlockRegion;
import talecraft.util.UndoRegion;
import talecraft.util.UndoTask;

public class PasteItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		onItemRightClick(world, player, hand);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		NBTTagCompound settings = TaleCraft.getSettings(player);

		float lenMul = settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook(1);
		plantPos = new Vec3d(
				plantPos.x*lenMul,
				plantPos.y*lenMul,
				plantPos.z*lenMul
				).add(new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ));

		// PLANT!
		String keyString = "player."+player.getGameProfile().getId().toString();
		ClipboardItem item = ServerHandler.getServerMirror(null).getClipboard().get(keyString);

		if(item != null) {

			if(item.getData().hasKey(ClipboardTagNames.$OFFSET, item.getData().getId())) {
				NBTTagCompound offset = item.getData().getCompoundTag(ClipboardTagNames.$OFFSET);
				plantPos = new Vec3d(
						plantPos.x + offset.getFloat("x"),
						plantPos.y + offset.getFloat("y"),
						plantPos.z + offset.getFloat("z")
						);
			}

			float snap = ServerMirror.instance().playerList().getPlayer((EntityPlayerMP) player).settings.getInteger("item.paste.snap");
			if(snap > 1) {
				plantPos = new Vec3d(
						Math.floor(plantPos.x / snap) * snap,
						Math.floor(plantPos.y / snap) * snap,
						Math.floor(plantPos.z / snap) * snap
						);
			}

			if(item.getData().hasKey(ClipboardTagNames.$REGION)) {
				NBTTagCompound regionTag = item.getData().getCompoundTag(ClipboardTagNames.$REGION);
				int width = regionTag.getInteger(ClipboardTagNames.$REGION_WIDTH);
				int height = regionTag.getInteger(ClipboardTagNames.$REGION_HEIGHT);
				int length = regionTag.getInteger(ClipboardTagNames.$REGION_LENGTH);
				BlockPos pos = new BlockPos(plantPos);
				System.out.println("pos: " + pos + "; width: " + width + "; height: " + height + "; length: " + length );
				UndoRegion before = new UndoRegion(new BlockRegion(pos, width, height, length), world);
				ClipboardItem.pasteRegion(item, pos, world, player);
				UndoRegion after = new UndoRegion(new BlockRegion(pos, width, height, length), world);
				UndoTask.TASKS.add(new UndoTask(before, after, "Paste", player.getName()));
			}

			if(item.getData().hasKey(ClipboardTagNames.$ENTITY)) {
				ClipboardItem.pasteEntity(item, plantPos, world, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
