package de.longor.talecraft.client.gui.misc;

import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class GuiConfirmOpenLinkTCOverride extends GuiConfirmOpenLink {
	private String url;
	
	public GuiConfirmOpenLinkTCOverride(GuiYesNoCallback callback, String url, int id, boolean isSave) {
		super(callback, url, id, isSave);
		this.url = url;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.drawCenteredString(this.fontRendererObj, url, this.width / 2, 90, 16777215);
	}
}