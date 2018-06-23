package talecraft.tileentity;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import talecraft.TaleCraft;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.network.packets.ForceF1Packet;

public class CameraBlockTileEntity extends TCTileEntity {
	private List<CameraPos> cpos;
	// private boolean mute;
	private static final double moveSpeed = 0.01;

	public CameraBlockTileEntity() {
		cpos = Lists.newArrayList();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		super.commandReceived(command, data);
		
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 0.8f;
	}
	
	public CameraPos addPos(EntityPlayer player){
		CameraPos pos = new CameraPos(player);
		cpos.add(pos);
		return pos;
	}

	@Override
	public String getName() {
		return "CameraBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		cpos.clear();
		for(int i = 0; i < comp.getInteger("size"); i++){
			cpos.add(CameraPos.fromNBT(comp.getCompoundTag("pos_" + i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setInteger("size", cpos.size());
		for(int i = 0; i < cpos.size(); i++){
			comp.setTag("pos_" + i, cpos.get(i).toNBT());
		}
		return comp;
	}

	public void trigger(EnumTriggerState triggerState){
		if(triggerState != EnumTriggerState.OFF && !world.isRemote){
			CameraThread thread = new CameraThread();
			thread.attemptStart();
		}
	}
	
	public static class CameraPos extends Vec3d{
		public final float yaw;
		public final float pitch;
		
		public CameraPos(EntityPlayer player){
			this(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		}
		
		public CameraPos(double x, double y, double z, float yaw, float pitch){
			super(x, y, z);
			this.yaw = yaw;
			this.pitch = pitch;
		}
		
		public void teleportTo(EntityPlayer player){
			if(!(player instanceof EntityPlayerMP)) return;
			EntityPlayerMP mp = (EntityPlayerMP) player;
			mp.connection.setPlayerLocation(x, y, z, calcOrientation(), pitch);
		}
		
		private float calcOrientation(){
			float orientation = yaw % 360;
			if (orientation <= 0){
				orientation += 360;
			}
			return orientation;
		}
		
		public NBTTagCompound toNBT(){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setDouble("x", x);
			tag.setDouble("y", y);
			tag.setDouble("z", z);
			tag.setFloat("yaw", yaw);
			tag.setFloat("pitch", pitch);
			return tag;
		}
		
		public static CameraPos fromNBT(NBTTagCompound tag){
			double x = tag.getDouble("x");
			double y = tag.getDouble("y");
			double z = tag.getDouble("z");
			float yaw = tag.getFloat("yaw");
			float pitch = tag.getFloat("pitch");
			return new CameraPos(x, y, z, yaw, pitch);
		}
		
		@Override
		public String toString(){
			DecimalFormat format = ItemStack.DECIMALFORMAT;
			return "[x=" + format.format(x) + ", y=" + format.format(y) + ", z=" + format.format(z) + " | yaw=" + format.format(yaw) + ", pitch=" + format.format(pitch) + "]";
		}
	}
	
	public class CameraThread extends Thread{
		
		private EntityPlayer player;
		private CameraPos original;
		private List<CameraPos> destinationPos;
		private int posIndex = 0;
		private int current = 0;
		
		public void attemptStart(){
			String str = null;
			if(cpos == null) str = "ArrayList is null! This is a bug!";
			else if(cpos.size() <= 0) str = "Camera block has no positions to run!";
			if(str == null){ //This is to prioritize
				EntityPlayer player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), -1, false);
				if(player == null){
					str = "No player to execute upon!";
				}else{
					this.player = player;
				}
			}
			if(str == null){
				start();
			}else{
				System.out.println("Error with camera block @" + getPos() + ". Error: " + str);
			}
		}
		
		@Override
		public void run(){
			original = new CameraPos(player);
			cpos.get(0).teleportTo(player);
			destinationPos = calcDestPos(cpos.get(1));
			posIndex = 1;
			current = 0;
			TaleCraft.network.sendTo(new ForceF1Packet(true), (EntityPlayerMP) player);
			while(posIndex < cpos.size()){
				if(player == null) break;
				if(current >= destinationPos.size()){
					current = 0;
					posIndex++;
					if(posIndex >= cpos.size()){
						break;
					}
					destinationPos = calcDestPos(cpos.get(posIndex));
				}
				CameraPos curPos = destinationPos.get(current);
				curPos.teleportTo(player);
				current++;
				try {
					sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			original.teleportTo(player);
			TaleCraft.network.sendTo(new ForceF1Packet(false), (EntityPlayerMP) player);
		}
		
		private List<CameraPos> calcDestPos(CameraPos pos){
			double xDiff =  pos.x - player.posX;
			double yDiff = pos.y - player.posY;
			double zDiff = pos.z - player.posZ;
			double yawDiff = (double) calcYaw(pos.yaw, player.rotationYaw);
			double pitchDiff = pos.pitch - player.rotationPitch;
			double distance = calcDistance(pos);
			double stops = distance/moveSpeed;
			
			List<Double> xStops = calcStops(pos, xDiff, stops, player.posX);
			List<Double> yStops = calcStops(pos, yDiff, stops, player.posY);
			List<Double> zStops = calcStops(pos, zDiff, stops, player.posZ);
			List<Double> yawStops = calcStops(pos, yawDiff, stops, player.rotationYaw);
			List<Double> pitchStops = calcStops(pos, pitchDiff, stops, player.rotationPitch);
			List<CameraPos> destPoints = Lists.newArrayList();
			for(int i = 0; i < stops; i++){
				destPoints.add(new CameraPos(xStops.get(i), yStops.get(i), zStops.get(i), yawStops.get(i).floatValue(), (float) pitchStops.get(i).floatValue()));
			}
			return destPoints;
		}
		

		
		private float calcYaw(float posYaw, float playYaw){
			float around = 360.0F - playYaw + posYaw;
			float backwards = posYaw - playYaw;
			float absBack = Math.abs(backwards);
			if(around == Math.min(around, absBack)) return around;
			return backwards;
		}
		
		private List<Double> calcStops(CameraPos pos, double diff, final double stopAmount, double current){
			List<Double> stops = Lists.newArrayList();
			final double intervals = diff/stopAmount;
			for(int i = 0; i < stopAmount; i++){
				current += intervals;
				stops.add(current);
			}
			return stops;
		}
		
		private double calcDistance(CameraPos pos){
			double xDiff = player.posX - pos.x;
			double yDiff = player.posY - pos.y;
			double zDiff = player.posZ - pos.z;
			double arg1 = xDiff*xDiff;
			double arg2 = yDiff*yDiff;
			double arg3 = zDiff*zDiff;
			return Math.sqrt(arg1 + arg2 + arg3);
		}
		
	}
	
}
