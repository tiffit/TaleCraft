package de.longor.talecraft.client.render;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.render.temporables.BlockHighlight;
import de.longor.talecraft.client.render.temporables.BlockPosTemporable;
import de.longor.talecraft.client.render.temporables.BoxTemporable;
import de.longor.talecraft.client.render.temporables.LineToBoxTemporable;
import de.longor.talecraft.util.NBTHelper;
import net.minecraft.command.PlayerSelector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class PushRenderableFactory {
	
	public static final ITemporaryRenderable parsePushRenderableFromNBT(NBTTagCompound data) {
		String type = data.getString("type");
		
		if(type == null || type.isEmpty()) {
			return null;
		}
		
		// TaleCraft.logger.info("New Push-Renderable: " + type + " :" + data.toString());
		
		if(type.equals("pos-marker")) {
			int[] pos = data.getIntArray("pos");
			int color = data.getInteger("color");
			long deletionTimepoint = System.currentTimeMillis() + 250L;
			
			if(pos.length != 3)
				return null;
			
			BlockPosTemporable ren = new BlockPosTemporable();
			ren.pos = pos;
			ren.color = color;
			ren.deletionTimepoint = deletionTimepoint;
			return ren;
		}
		
		if(type.equals("line-to-box")) {
			int[] src = data.getIntArray("src");
			int[] box = data.getIntArray("box");
			long deletionTimepoint = System.currentTimeMillis() + 100L;
			
			if(src.length != 3)
				return null;
			if(box.length != 6)
				return null;
			
			LineToBoxTemporable ren = new LineToBoxTemporable();
			ren.box = box;
			ren.src = src;
			ren.deletionTimepoint = deletionTimepoint;
			
			// TaleCraft.logger.info("Parsed: " + ren);
			return ren;
		}
		
		if(type.equals("box")) {
			int[] box = data.getIntArray("box");
			
			if(box.length != 6)
				return null;
			
			BoxTemporable ren = new BoxTemporable();
			ren.box = box;
			ren.color = data.hasKey("color") ? data.getInteger("color") : 0xFFFF7F00;
			
			TaleCraft.logger.info("Parsed: " + ren);
			return ren;
		}
		
		if(type.equals("selector")) {
			String selector = data.getString("selector").trim();
			
			// Is this even a selector?
			if(!selector.startsWith("@")) {
				return null;
			}
			
			// Get and then cut off the selector operator.
			char selectorChar = selector.charAt(1);
			selector = selector.substring(2).trim();
			
			// Cut off the [ ] pair.
			if(selector.charAt(0) == '[')
				selector = selector.substring(1).trim();
			if(selector.charAt(selector.length()-1) == ']')
				selector = selector.substring(0,selector.length()-1).trim();
			
			// No selector, stop processing.
			if(selector.isEmpty()) {
				TaleCraft.logger.info("Attempted parsing a selector without filters: " + data.getString("selector"));
				return null;
			}
			
			String[] arguments = null;
			
			if(selector.indexOf(',') != -1) {
				arguments = selector.split(",");
			} else {
				arguments = new String[]{selector};
			}
			
			// Splitting the arguments into key/value pairs is easy...
			// but how am I supposed to get geometric shapes from these?
			// Take a look at the PlayerSelector class.
			// TODO: Implement entity selector parsing for visualization!
			
			TaleCraft.logger.info("Parsed: " + Arrays.toString(arguments));
		}
		
		if(type.equals("highlight.block")) {
			double duration = data.getDouble("duration");
			int posX = data.getInteger("pos.x");
			int posY = data.getInteger("pos.y");
			int posZ = data.getInteger("pos.z");
			BlockPos pos = new BlockPos(posX, posY, posZ);
			return new BlockHighlight(pos, duration);
		}
		
		TaleCraft.logger.error("Unknown push renderable: " + type);
		return null;
	}
	
}
