package de.longor.talecraft.client.gui.qad;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase.Height;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.client.render.renderers.EXTFontRenderer;

//TODO: Replace data logic with a model.
public class QADSlider extends QADRectangularComponent {
	
	public static interface SliderModel<T> {
		public void setValue(T value);
		public T getValue();
		public String getValueAsText();
		
		public void setSliderValue(float sliderValue);
		public float getSliderValue();
	}
	
	protected static final ResourceLocation buttonTextures = new ResourceLocation("minecraft:textures/gui/widgets.png");
	private int xPos;
	private int yPos;
	private int width = 100;
	private int height = 20;
	private boolean enabled = true;
	private boolean hovered = false;
	private boolean focused = false;
	private SliderModel model;
	private Runnable action;
	
	public QADSlider(SliderModel model) {
		if(model == null)
			throw new IllegalArgumentException("'model' must not be null!");
		
		this.model = model;
	}
	
	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public void setX(int x) {
		xPos = x;
	}

	@Override
	public void setY(int y) {
		yPos = y;
	}

	@Override
	public void setPosition(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	// Returns a value 0..1
	public float getSliderValue() {
		return model.getSliderValue();
	}
	
	public void setSliderValue(float newValue) {
		model.setSliderValue(newValue);
	}
    
	public void setSliderAction(Runnable action) {
		this.action = action;
	}
	
    protected int getHoverState(boolean mouseOver) {
        byte b0 = 1;

        if (!this.enabled)
        {
            b0 = 0;
        }
        else if (mouseOver)
        {
            b0 = 2;
        }

        return b0;
    }
    
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		
        {
            EXTFontRenderer fontrenderer = renderer.getFontRenderer();
            renderer.bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
            int k = 0;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            
            if(width < 200) {
            	renderer.drawTexturedModalRectangle(this.xPos, this.yPos, 0, 46 + k * 20, this.width / 2, this.height);
            	renderer.drawTexturedModalRectangle(this.xPos + this.width / 2, this.yPos, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            } else {
            	renderer.drawRectangle(this.xPos, this.yPos, this.xPos+this.width, this.yPos+this.height, 0x7F000000);
            	renderer.drawLineRectangle(this.xPos, this.yPos, this.xPos+this.width, this.yPos+this.height, 0xFFFFFFFF);
            }
            
            // this.mouseDragged(mc, mouseX, mouseY);
            
            // TODO: Draw slider right here!
            int sliderPos = (int) (model.getSliderValue() * width);
            sliderPos = MathHelper.clamp_int(sliderPos, 2, width-3);
            
            renderer.drawVerticalLine(xPos + sliderPos-1, yPos+2, yPos+height-3, 0xFF7F7F7F);
            renderer.drawVerticalLine(xPos + sliderPos  , yPos+1, yPos+height-2, 0xFFFFFFFF);
            renderer.drawVerticalLine(xPos + sliderPos+1, yPos+2, yPos+height-3, 0xFF7F7F7F);
            
            int fontColor = 14737632;
            
            if (!this.enabled)
            {
                fontColor = 10526880;
            }
            else if (this.hovered)
            {
                fontColor = 16777120;
            }
            
            renderer.drawCenteredString(model.getValueAsText(), xPos + width / 2, yPos + (height - 8) / 2, fontColor, true);
        }
		
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		boolean mouseOver = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
		if(!mouseOver) return;
		updateSlider(localMouseX);
	}
	
	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		boolean mouseOver = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
		if(!mouseOver) return;
		updateSlider(localMouseX);
		playPressSound();
	}

	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		boolean mouseOver = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
		if(!mouseOver) return;
		updateSlider(localMouseX);
	}
	
    private void updateSlider(int localMouseX) {
    	float newSliderValue = (float)localMouseX / (width-1);
    	float oldSliderValue = model.getSliderValue();
    	
    	if(newSliderValue != oldSliderValue) {
    		model.setSliderValue(newSliderValue);
    		
    		if(action != null) {
    			action.run();
    		}
    	}
	}

	public void playPressSound() {
    	SoundHandler soundHandlerIn = Minecraft.getMinecraft().getSoundHandler();
    	ResourceLocation pressSound = new ResourceLocation("gui.button.press");
    	float pitch = enabled ? 1f : 0.5f;
        soundHandlerIn.playSound(PositionedSoundRecord.create(pressSound, pitch));
    }

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {
		// ignore
	}

	@Override
	public void onTickUpdate() {
		// ignore
	}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - xPos;
		int localMouseY = mouseY - yPos;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public boolean canResize() {
		return true;
	}

	@Override
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}

	@Override
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
	}

	public void setModel(SliderModel newModel) {
		this.model = newModel;
	}

	public SliderModel getModel() {
		return this.model;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.INPUT;
	}
	
	@Override
	public boolean transferFocus() {
		if(focused) {
			focused = false;
			return false;
		} else {
			focused = true;
			return true;
		}
	}

	@Override
	public boolean isFocused() {
		return focused;
	}

	@Override
	public void removeFocus() {
		focused = false;
	}
	
}
