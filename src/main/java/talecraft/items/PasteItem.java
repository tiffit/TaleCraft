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

public class PasteItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		onItemRightClick(stack, world, player, hand);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		NBTTagCompound settings = TaleCraft.getSettings(player);

		float lenMul = settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook(1);
		plantPos = new Vec3d(
				plantPos.xCoord*lenMul,
				plantPos.yCoord*lenMul,
				plantPos.zCoord*lenMul
				).add(new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ));

		// PLANT!
		String keyString = "player."+player.getGameProfile().getId().toString();
		ClipboardItem item = ServerHandler.getServerMirror(null).getClipboard().get(keyString);

		if(item != null) {

			if(item.getData().hasKey(ClipboardTagNames.$OFFSET, item.getData().getId())) {
				NBTTagCompound offset = item.getData().getCompoundTag(ClipboardTagNames.$OFFSET);
				plantPos = new Vec3d(
						plantPos.xCoord + offset.getFloat("x"),
						plantPos.yCoord + offset.getFloat("y"),
						plantPos.zCoord + offset.getFloat("z")
						);
			}

			float snap = ServerMirror.instance().playerList().getPlayer((EntityPlayerMP) player).settings.getInteger("item.paste.snap");
			if(snap > 1) {
				plantPos = new Vec3d(
						Math.floor(plantPos.xCoord / snap) * snap,
						Math.floor(plantPos.yCoord / snap) * snap,
						Math.floor(plantPos.zCoord / snap) * snap
						);
			}

			if(item.getData().hasKey(ClipboardTagNames.$REGION)) {
				ClipboardItem.pasteRegion(item, new BlockPos(plantPos), world, player);
			}

			if(item.getData().hasKey(ClipboardTagNames.$ENTITY)) {
				ClipboardItem.pasteEntity(item, plantPos, world, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
