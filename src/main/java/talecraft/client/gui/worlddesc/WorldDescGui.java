package talecraft.client.gui.worlddesc;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

public class WorldDescGui extends GuiScreen {
	
	List<String> lines;
	String world_title;
	
	public WorldDescGui(List<String> lines, String world_title) {
		this.lines = lines;
		this.world_title = world_title;
	}
	
	@Override
	public void initGui() {
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		drawCenteredString(fontRenderer, world_title, width / 2, 3, 0x00decc);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			for (ColoredString cstring : getColoredStrings(line, i + 1)) {
				fontRenderer.drawStringWithShadow(cstring.str, cstring.x, cstring.y,
						cstring.color);
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(new NewWorldSelector());
		}
	}
	
	private ColoredString[] getColoredStrings(String normal, int yLenght) {
		List<ColoredString> coloredStrings = Lists.newArrayList();
		if (!normal.contains("<")) {
			coloredStrings.add(new ColoredString(0, 3 + 10 * yLenght, 0xffffff,
					normal));
			return getAsArray(coloredStrings);
		}
		String[] colorsStart = normal.split("<");
		for (int i = 0; i < colorsStart.length; i++) {
			int offset = 0;
			if (i > 0) {
				for (int j = 0; j < i; j++) {
					offset += fontRenderer.getStringWidth(coloredStrings.get(j).str);
				}
				String[] endSplit = colorsStart[i].split(">");
				try {
					int color = Integer.parseInt(endSplit[0]);
					if (endSplit.length > 1)
						coloredStrings.add(new ColoredString(offset, 3 + 10 * yLenght,
								color, endSplit[1]));
				} catch (NumberFormatException e) {
					coloredStrings.clear();
					coloredStrings.add(new ColoredString(0, 3 + 10 * yLenght, 16711680,
							"Error: \"" + endSplit[0] + "\" is not a number!"));
					return getAsArray(coloredStrings);
				}
			} else {
				coloredStrings.add(new ColoredString(0, 3 + 10 * yLenght, 0xffffff,
						colorsStart[0]));
			}
			
		}
		return getAsArray(coloredStrings);
	}
	
	private ColoredString[] getAsArray(List<ColoredString> list) {
		ColoredString[] cstrings = new ColoredString[list.size()];
		for (int i = 0; i < list.size(); i++) {
			cstrings[i] = list.get(i);
		}
		return cstrings;
	}
	
	private class ColoredString {
		int x;
		int y;
		int color;
		String str;
		
		public ColoredString(int x, int y, int color, String str) {
			this.x = x;
			this.y = y;
			this.color = color;
			this.str = str;
		}
	}
	
}
