package de.longor.talecraft.client;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.sun.prism.paint.Color;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;

public class InvokeTracker {
	List<TrackedInvoke> trackedInvokes;
	
	public InvokeTracker() {
		trackedInvokes = Lists.newArrayList();
	}
	
	public void trackInvoke(NBTTagCompound data) {
		if(!ClientProxy.settings.getBoolean("invoke.tracker"))return;
		
		TrackedInvoke invoke = new TrackedInvoke();
		invoke.isr = data.getFloat("isr");
		invoke.isg = data.getFloat("isg");
		invoke.isb = data.getFloat("isb");
		invoke.itr = data.getFloat("itr");
		invoke.itg = data.getFloat("itg");
		invoke.itb = data.getFloat("itb");
		invoke.encodeColors();
		trackedInvokes.add(invoke);
	}
	
	public void display(Minecraft mc, EntityPlayerSP player, WorldClient theWorld, ClientProxy clientProxy) {
		if(!ClientProxy.settings.getBoolean("invoke.tracker")) {
			trackedInvokes.clear();
			return;
		}
		
		// Don't let it go over 32 invokes!
		while(trackedInvokes.size() > 255) {
			trackedInvokes.remove(0);
		}
		
		// Now display!
		
        // Finally, draw the whole thing!
		final int BASEY = clientProxy.getInfoBar().getLastMaxY();
		final int BAWI = mc.displayWidth;
		final int BAHE = mc.fontRendererObj.FONT_HEIGHT;
		final int BAYM = BASEY+BAHE;
		final int BAYI = BASEY;
		
		mc.ingameGUI.drawRect(0, BAYI, BAWI, BAYM, 0xAA000000);
        
		int wi = 8;
		int he = BAHE-1;
		int he2 = he/2;
		int incrX = 0;
		
		for(TrackedInvoke invoke : Lists.reverse(new ArrayList<TrackedInvoke>(trackedInvokes))) {
			mc.ingameGUI.drawRect(incrX, BAYI+1, incrX+wi, BAYM-he2, invoke.is);
			mc.ingameGUI.drawRect(incrX, BAYI+1+he2, incrX+wi, BAYM-1, invoke.it);
			incrX += wi + 1;
			
			if(incrX > BAWI) break;
		}
		
	}
	
	private class TrackedInvoke {
		float isr,isg,isb;
		float itr,itg,itb;
		
		int is;
		int it;
		
		public void encodeColors() {
			is = 0;
			is |= ((int)(isr*255) & 0xFF) << 16;
			is |= ((int)(isg*255) & 0xFF) << 8;
			is |= ((int)(isb*255) & 0xFF);
			is |= 0xFF000000;
			
			it = 0;
			it |= ((int)(itr*255) & 0xFF) << 16;
			it |= ((int)(itg*255) & 0xFF) << 8;
			it |= ((int)(itb*255)) & 0xFF;
			it |= 0xFF000000;
		}
	}
	
}
