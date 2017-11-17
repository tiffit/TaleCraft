package talecraft.items;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraft;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.IInvokeSource;
import talecraft.invoke.Invoke;
import talecraft.invoke.NullInvoke;

// TODO: Finish implementing this item.
public class ScriptItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return EnumActionResult.PASS;

		NBTTagCompound compound = getNBT(stack);

		// get invoke
		IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_use"));

		// make sure to not waste time
		if(invoke == null) return EnumActionResult.PASS;
		if(invoke instanceof NullInvoke) return EnumActionResult.PASS;

		// execute invoke
		Invoke.invoke(invoke, new TempstackvokeSource(world, new BlockPos(hitX, hitY, hitZ), player), null, EnumTriggerState.ON);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		NBTTagCompound compound = getNBT(stack);

		// get invoke
		IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_rclick"));

		// make sure to not waste time
		if(invoke == null) return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(invoke instanceof NullInvoke) return ActionResult.newResult(EnumActionResult.PASS, stack);

		// execute invoke
		Invoke.invoke(invoke, new TempstackvokeSource(world, player.getPosition(), player), null, EnumTriggerState.ON);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		NBTTagCompound compound = getNBT(stack);

		// get invoke
		IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_lclick"));

		// make sure to not waste time
		if(invoke == null) return false;
		if(invoke instanceof NullInvoke) return false;

		// execute invoke
		Invoke.invoke(invoke, new TempstackvokeSource(player.world, player.getPosition(), player), null, EnumTriggerState.ON);

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// Should I allow this?
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		NBTTagCompound compound = getNBT(stack);

		NBTTagList lore = compound.getTagList("lore", NBT.TAG_STRING);
		if(lore.hasNoTags()) return;

		for(int i = 0; i < lore.tagCount(); i++) {
			tooltip.add(lore.getStringTagAt(i));
		}
	}

	private static final NBTTagCompound getNBT(ItemStack stack) {
		NBTTagCompound comp = stack.getTagCompound();

		if(comp == null) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}

		return comp;
	}

	private static class TempstackvokeSource implements IInvokeSource, ICommandSender {
		World world;
		Entity holder;
		BlockPos position;
		Scriptable scriptScope;

		public TempstackvokeSource(World worldIn, BlockPos positionIn, Entity holderIn) {
			this.world = worldIn;
			this.holder = holderIn;
			this.position = positionIn;
		}

		@Override
		public Scriptable getInvokeScriptScope() {
			if(scriptScope == null) {

				// if(holder != null) ... ?

				scriptScope = TaleCraft.globalScriptManager.createNewWorldScope(world);
			}

			return scriptScope;
		}

		@Override
		public ICommandSender getInvokeAsCommandSender() {
			return this;
		}

		@Override
		public BlockPos getInvokePosition() {
			return position;
		}

		@Override
		public World getInvokeWorld() {
			return world;
		}

		@Override
		public void getInvokes(List<IInvoke> invokes) {
			// nope
		}

		@Override
		public String getName() {
			return "ItemStack Invoke Source";
		}

		@Override
		public ITextComponent getDisplayName() {
			return new TextComponentString(getName());
		}

		@Override
		public void sendMessage(ITextComponent message) {
			if(holder != null)
				holder.sendMessage(message);
		}

		@Override
		public boolean canUseCommand(int permLevel, String commandName) {
			return true;
		}

		@Override
		public BlockPos getPosition() {
			return position;
		}

		@Override
		public Vec3d getPositionVector() {
			return new Vec3d(position.getX(), position.getY(), position.getZ());
		}

		@Override
		public World getEntityWorld() {
			return world;
		}

		@Override
		public Entity getCommandSenderEntity() {
			return holder;
		}

		@Override
		public boolean sendCommandFeedback() {
			return true;
		}

		@Override
		public void setCommandStat(Type type, int amount) {
			if(holder != null)
				holder.setCommandStat(type, amount);
		}

		@Override
		public void getInvokeColor(float[] color) {
			color[0] = 0.0f;
			color[1] = 0.0f;
			color[2] = 1.0f;
		}

		@Override
		public MinecraftServer getServer() {
			return FMLCommonHandler.instance().getMinecraftServerInstance();
		}

	}

}
