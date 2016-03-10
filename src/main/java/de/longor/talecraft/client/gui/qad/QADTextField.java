package de.longor.talecraft.client.gui.qad;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

/**
 * This entire class si simply a full-on copy of GuiTextField from the decompiled Minecraft Sourcecode.
 * There is no other way to replicate the behaviour of the GuiTextField then to copy it.
 **/
public class QADTextField extends QADRectangularComponent {
	public static interface TextFieldModel {
		// get text
		public String getText();
		public int getTextLength();
		
		// specific get
		public char getCharAt(int i);
		
		// set text
		public void setText(String text);
		
		// color
		public void setTextColor(int color);
		public int getTextColor();
	}

	//	private final int ID;
	private final FontRenderer fontRendererInstance;
	private Predicate field_175209_y = Predicates.alwaysTrue();
	//  private GuiPageButtonList.GuiResponder field_175210_x;

	public String[] autoCompleteOptions = null;
	public TextChangeListener textChangedListener;
	public int xPosition;
	public int yPosition;
	public int width;
	public int height;

	private TextFieldModel model;
	private int maxStringLength = 64;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	private boolean canLoseFocus = true;
	private boolean isFocused;
	private boolean isEnabled = true;
	private int lineScrollOffset = 0;
	private int cursorPosition = 0;
	private int selectionEnd = 0;
	private int disabledColor = 7368816;
	private boolean visible = true;

	public QADTextField(/*int __id__, */ FontRenderer FNTREN, int xPos, int yPos, int width, int height) {
		// this.ID = __id__;
		this.model = new DefaultTextFieldModel();
		this.fontRendererInstance = FNTREN;
		this.xPosition = xPos;
		this.yPosition = yPos;
		this.width = width;
		this.height = height;
		
		// Tiny ugly bug fix
		this.onMouseClicked(2, height/2, 0);
		this.isFocused = false;
	}

	public QADTextField(int xPos, int yPos, int width, int height) {
		// this.ID = __id__;
		this.model = new DefaultTextFieldModel();
		this.fontRendererInstance = Minecraft.getMinecraft().fontRendererObj;
		this.xPosition = xPos;
		this.yPosition = yPos;
		this.width = width;
		this.height = height;
		
		// Tiny ugly bug fix
		this.onMouseClicked(2, height/2, 0);
		this.isFocused = false;
	}

	public QADTextField(String text) {
		// this.ID = __id__;
		this.model = new DefaultTextFieldModel(text);
		this.fontRendererInstance = Minecraft.getMinecraft().fontRendererObj;
		this.xPosition = 0;
		this.yPosition = 0;
		this.width = 20;
		this.height = 20;
		
		// Tiny ugly bug fix
		this.onMouseClicked(2, height/2, 0);
		this.isFocused = false;
	}

	public QADTextField(TextFieldModel model) {
		// this.ID = __id__;
		this.model = model;
		this.fontRendererInstance = Minecraft.getMinecraft().fontRendererObj;
		this.xPosition = 0;
		this.yPosition = 0;
		this.width = 20;
		this.height = 20;
		
		// Tiny ugly bug fix
		this.onMouseClicked(2, height/2, 0);
		this.isFocused = false;
	}

	public void setModel(TextFieldModel newModel) {
		if(newModel == null)
			throw new IllegalArgumentException("'newModel' must not be null.");
		
		this.model = newModel;
	}

	public TextFieldModel getModel() {
		return this.model;
	}

	@Override
	public int getX() {
		return xPosition;
	}

	@Override
	public int getY() {
		return yPosition;
	}

	@Override
	public void setX(int x) {
		xPosition = x;
	}

	@Override
	public void setY(int y) {
		yPosition = y;
	}

	@Override
	public void setPosition(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
	}

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		// Culling on the Y-Axis
		if(renderer.getOffsetY()+yPosition > renderer.getHeight()) {
			return;
		} else if(renderer.getOffsetY()+yPosition+height < 0) {
			return;
		}
		
		if (this.getVisible())
		{
			final int left = xPosition;
			final int right = xPosition + width;
			final int top = yPosition - 1;
			final int bottom = yPosition + height + 1;
			
			if (this.getEnableBackgroundDrawing()) {
				renderer.drawRectangle(left, top, right, bottom, isFocused ? 0xFFFFF055 : -6250336);
				renderer.drawRectangle(left+1, top+1, right-1, bottom-1, -16777216);
				
//				renderer.drawRectangle(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -6250336);
//				renderer.drawRectangle(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, -16777216);
			}
			
			// renderer.pushScissor(left+1, top, right-left-1, bottom-top);
			
			int textColor = this.isEnabled ? this.model.getTextColor() : this.disabledColor;
			int cursorOffset = this.cursorPosition - this.lineScrollOffset;
			int selectionOffset = this.selectionEnd - this.lineScrollOffset;
			
			final String originalText = model.getText();
			String s = fontRendererInstance.trimStringToWidth(originalText.substring(lineScrollOffset), getWidth());
			
			boolean cursorOffsetInText = cursorOffset >= 0 && cursorOffset <= s.length();
			boolean cursorVisible = this.isFocused && this.cursorCounter / 6 % 2 == 0 && cursorOffsetInText;
			int textDrawRegionLeft = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
			int textDrawRegionRight = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
			int textDrawPos = textDrawRegionLeft;
			
			if (selectionOffset > s.length())
			{
				selectionOffset = s.length();
			}
			
			if (s.length() > 0)
			{
				String leftOfCursorText = cursorOffsetInText ? s.substring(0, cursorOffset) : s;
				textDrawPos = renderer.drawStringWithShadow(leftOfCursorText, textDrawRegionLeft, textDrawRegionRight, textColor);
			}
			
			// XXX: Fix this stupid hack!
			// cursorOffset -= renderer.getOffsetX()!=0 ? 4 : 0;
			
			boolean curserNotAtEnd = this.cursorPosition < originalText.length();
			boolean textLongerThanMax = originalText.length() >= this.getMaxStringLength();
			
			boolean flag2 = curserNotAtEnd || textLongerThanMax;
			int cursorPos = textDrawPos;
			
			if (!cursorOffsetInText) {
				cursorPos = cursorOffset > 0 ? textDrawRegionLeft + this.width : 1;
			} else if (flag2) {
				cursorPos = textDrawPos - 3;
				textDrawPos -= 1;
			} else {
				cursorPos -= -1;
			}
			
			if (s.length() > 0 && cursorOffsetInText && cursorOffset < s.length())
			{
				String rightOfCursor = s.substring(cursorOffset);
				textDrawPos = renderer.drawStringWithShadow(rightOfCursor, textDrawPos, textDrawRegionRight, textColor);
			}
			
			if (cursorVisible)
			{
				if (flag2)
				{
					renderer.drawRectangle(cursorPos, textDrawRegionRight-1, cursorPos+1, textDrawRegionRight+1+this.fontRendererInstance.FONT_HEIGHT, -3092272);
				}
				else
				{
					renderer.drawStringWithShadow("_", cursorPos, textDrawRegionRight, textColor);
				}
			}

			if (selectionOffset != cursorOffset)
			{
				int l1 = textDrawRegionLeft + this.fontRendererInstance.getStringWidth(s.substring(0, selectionOffset));
				this.drawCursorVertical(cursorPos, textDrawRegionRight - 1, l1 - 1, textDrawRegionRight + 1 + this.fontRendererInstance.FONT_HEIGHT, renderer);
			}

			if(autoCompleteOptions != null && isFocused) {
				int lh = (fontRendererInstance.FONT_HEIGHT+2);
				int lw = 0;
				int h = autoCompleteOptions.length * lh;
				int count = 0;

				{
					for(String string : autoCompleteOptions)
						lw = Math.max(lw, fontRendererInstance.getStringWidth(string));
				}

				int yOff = this.yPosition + this.height + 2;
				for(String str : autoCompleteOptions) {
					renderer.drawRectangle(
							this.xPosition - 1,
							yOff + lh - 1,
							this.xPosition + lw + 1,
							yOff - 1,
							(count % 2) != 0 ? 0x90102010 : 0x90113311
							);

					fontRendererInstance.drawString(str, xPosition, yOff+1, 0xFF555555);
					yOff += lh;
					count++;
				}

			}
			
			// renderer.popScissor();
		}
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		localMouseX += xPosition;
		localMouseY += yPosition;

		boolean flag = localMouseX >= this.xPosition && localMouseX < this.xPosition + this.width && localMouseY >= this.yPosition && localMouseY < this.yPosition + this.height;

		if (this.canLoseFocus)
		{
			this.setFocused(flag);
		}

		if (this.isFocused && flag && mouseButton == 0)
		{
			int l = localMouseX - this.xPosition;

			if (this.enableBackgroundDrawing)
			{
				l -= 4;
			}

			final String text = model.getText();
			String s = this.fontRendererInstance.trimStringToWidth(text.substring(this.lineScrollOffset), this.getWidth());
			this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, l).length() + this.lineScrollOffset);
		}
	}

	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {

	}

	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	@Override
	public void onKeyTyped(char p_146201_1_, int p_146201_2_) {
		if (!this.isFocused)
		{
			return;
		}
		else if (GuiScreen.isKeyComboCtrlA(p_146201_2_))
		{
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
			return;
		}
		else if (GuiScreen.isKeyComboCtrlC(p_146201_2_))
		{
			GuiScreen.setClipboardString(this.getSelectedText());
			return;
		}
		else if (GuiScreen.isKeyComboCtrlV(p_146201_2_))
		{
			if (this.isEnabled)
			{
				this.writeText(GuiScreen.getClipboardString());
			}

			return;
		}
		else if (GuiScreen.isKeyComboCtrlX(p_146201_2_))
		{
			GuiScreen.setClipboardString(this.getSelectedText());

			if (this.isEnabled)
			{
				this.writeText("");
			}

			return;
		}
		else
		{
			switch (p_146201_2_)
			{
			case 14:
				if (GuiScreen.isCtrlKeyDown())
				{
					if (this.isEnabled)
					{
						this.deleteWords(-1);
					}
				}
				else if (this.isEnabled)
				{
					this.deleteFromCursor(-1);
				}

				return;
			case 199:
				if (GuiScreen.isShiftKeyDown())
				{
					this.setSelectionPos(0);
				}
				else
				{
					this.setCursorPositionZero();
				}

				return;
			case 203:
				if (GuiScreen.isShiftKeyDown())
				{
					if (GuiScreen.isCtrlKeyDown())
					{
						this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
					}
					else
					{
						this.setSelectionPos(this.getSelectionEnd() - 1);
					}
				}
				else if (GuiScreen.isCtrlKeyDown())
				{
					this.setCursorPosition(this.getNthWordFromCursor(-1));
				}
				else
				{
					this.moveCursorBy(-1);
				}

				return;
			case 205:
				if (GuiScreen.isShiftKeyDown())
				{
					if (GuiScreen.isCtrlKeyDown())
					{
						this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
					}
					else
					{
						this.setSelectionPos(this.getSelectionEnd() + 1);
					}
				}
				else if (GuiScreen.isCtrlKeyDown())
				{
					this.setCursorPosition(this.getNthWordFromCursor(1));
				}
				else
				{
					this.moveCursorBy(1);
				}

				return;
			case 207:
				if (GuiScreen.isShiftKeyDown())
				{
					this.setSelectionPos(model.getText().length());
				}
				else
				{
					this.setCursorPositionEnd();
				}

				return;
			case 211:
				if (GuiScreen.isCtrlKeyDown())
				{
					if (this.isEnabled)
					{
						this.deleteWords(1);
					}
				}
				else if (this.isEnabled)
				{
					this.deleteFromCursor(1);
				}

				return;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_))
				{
					if (this.isEnabled)
					{
						this.writeText(Character.toString(p_146201_1_));
					}

					return;
				}
				else
				{
					return;
				}
			}
		}
	}

	@Override
	public void onTickUpdate() {
		updateCursorCounter();
	}






	public void updateCursorCounter()
	{
		++this.cursorCounter;
	}

	public void setText(String newText)
	{
		if (this.field_175209_y.apply(newText))
		{
			if (newText.length() > this.maxStringLength)
			{
				model.setText(newText.substring(0, this.maxStringLength));
			}
			else
			{
				model.setText(newText);
			}

			this.setCursorPositionEnd();
		}
	}

	public String getText() {
		return model.getText();
	}

	public String getSelectedText() {
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		return model.getText().substring(i, j);
	}

	public void writeText(String newText) {
		String s1 = "";
		String s2 = ChatAllowedCharacters.filterAllowedCharacters(newText);
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		int k = this.maxStringLength - model.getTextLength() - (i - j);
		boolean flag = false;

		if (model.getTextLength() > 0)
		{
			s1 = s1 + model.getText().substring(0, i);
		}

		int l;

		if (k < s2.length())
		{
			s1 = s1 + s2.substring(0, k);
			l = k;
		}
		else
		{
			s1 = s1 + s2;
			l = s2.length();
		}

		if (model.getTextLength() > 0 && j < model.getTextLength())
		{
			s1 = s1 + model.getText().substring(j);
		}

		if (this.field_175209_y.apply(s1))
		{
			model.setText(s1);
			this.moveCursorBy(i - this.selectionEnd + l);

			//            if (this.field_175210_x != null)
				//            {
				//                this.field_175210_x.func_175319_a(this.ID, this.text);
				//            }
		}

		if(textChangedListener != null)
			textChangedListener.call(this, model.getText());
	}

	public void deleteWords(int p_146177_1_)
	{
		if (model.getTextLength() != 0)
		{
			if (this.selectionEnd != this.cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
			}
		}
	}

	public void deleteFromCursor(int p_146175_1_)
	{
		if (model.getTextLength() != 0)
		{
			if (this.selectionEnd != this.cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				boolean flag = p_146175_1_ < 0;
				int j = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
				int k = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
				String s = "";

				if (j >= 0)
				{
					s = model.getText().substring(0, j);
				}

				if (k < model.getTextLength())
				{
					s = s + model.getText().substring(k);
				}

				model.setText(s);

				if (flag)
				{
					this.moveCursorBy(p_146175_1_);
				}

				//                if (this.field_175210_x != null)
					//                {
					//                    this.field_175210_x.func_175319_a(this.ID, this.text);
					//                }
			}
		}

		if(textChangedListener != null)
			textChangedListener.call(this, model.getText());
	}

	public int getNthWordFromCursor(int p_146187_1_)
	{
		return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
	}

	public int getNthWordFromPos(int p_146183_1_, int p_146183_2_)
	{
		return this.getNthWord_do(p_146183_1_, p_146183_2_, true);
	}

	public int getNthWord_do(int p_146197_1_, int p_146197_2_, boolean p_146197_3_)
	{
		int k = p_146197_2_;
		boolean flag1 = p_146197_1_ < 0;
		int l = Math.abs(p_146197_1_);

		for (int i1 = 0; i1 < l; ++i1)
		{
			if (flag1)
			{
				while (p_146197_3_ && k > 0 && model.getCharAt(k - 1) == 32)
				{
					--k;
				}

				while (k > 0 && model.getCharAt(k - 1) != 32)
				{
					--k;
				}
			}
			else
			{
				int j1 = model.getTextLength();
				k = model.getText().indexOf(32, k);

				if (k == -1)
				{
					k = j1;
				}
				else
				{
					while (p_146197_3_ && k < j1 && model.getCharAt(k) == 32)
					{
						++k;
					}
				}
			}
		}

		return k;
	}

	public void setSelectionPos(int p_146199_1_)
	{
		int j = model.getTextLength();

		if (p_146199_1_ > j)
		{
			p_146199_1_ = j;
		}

		if (p_146199_1_ < 0)
		{
			p_146199_1_ = 0;
		}

		this.selectionEnd = p_146199_1_;

		if (this.fontRendererInstance != null)
		{
			if (this.lineScrollOffset > j)
			{
				this.lineScrollOffset = j;
			}

			int k = this.getWidth();
			String s = this.fontRendererInstance.trimStringToWidth(model.getText().substring(this.lineScrollOffset), k);
			int l = s.length() + this.lineScrollOffset;

			if (p_146199_1_ == this.lineScrollOffset)
			{
				this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(model.getText(), k, true).length();
			}

			if (p_146199_1_ > l)
			{
				this.lineScrollOffset += p_146199_1_ - l;
			}
			else if (p_146199_1_ <= this.lineScrollOffset)
			{
				this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
			}

			this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, j);
		}
	}

	public void setCanLoseFocus(boolean p_146205_1_)
	{
		this.canLoseFocus = p_146205_1_;
	}

	public boolean getVisible()
	{
		return this.visible;
	}

	public void setVisible(boolean p_146189_1_)
	{
		this.visible = p_146189_1_;
	}

	public void moveCursorBy(int p_146182_1_)
	{
		this.setCursorPosition(this.selectionEnd + p_146182_1_);
	}

	public void setCursorPosition(int p_146190_1_)
	{
		this.cursorPosition = p_146190_1_;
		int j = model.getTextLength();
		this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, j);
		this.setSelectionPos(this.cursorPosition);
	}

	public void setCursorPositionZero()
	{
		this.setCursorPosition(0);
	}

	public void setCursorPositionEnd()
	{
		this.setCursorPosition(model.getTextLength());
	}

	private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_, VCUIRenderer renderer)
	{
		int i1;

		if (p_146188_1_ < p_146188_3_)
		{
			i1 = p_146188_1_;
			p_146188_1_ = p_146188_3_;
			p_146188_3_ = i1;
		}

		if (p_146188_2_ < p_146188_4_)
		{
			i1 = p_146188_2_;
			p_146188_2_ = p_146188_4_;
			p_146188_4_ = i1;
		}

		if (p_146188_3_ > this.xPosition + this.width)
		{
			p_146188_3_ = this.xPosition + this.width;
		}

		if (p_146188_1_ > this.xPosition + this.width)
		{
			p_146188_1_ = this.xPosition + this.width;
		}

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(5387);
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.setTranslation(renderer.getOffsetX(), renderer.getOffsetY(), renderer.getZLevel());
		worldrenderer.pos((double)p_146188_1_, (double)p_146188_4_, 0.0D).endVertex();
		worldrenderer.pos((double)p_146188_3_, (double)p_146188_4_, 0.0D).endVertex();
		worldrenderer.pos((double)p_146188_3_, (double)p_146188_2_, 0.0D).endVertex();
		worldrenderer.pos((double)p_146188_1_, (double)p_146188_2_, 0.0D).endVertex();
		tessellator.draw();
		worldrenderer.setTranslation(0,0,0);
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	public void setMaxStringLength(int p_146203_1_)
	{
		this.maxStringLength = p_146203_1_;

		if (model.getTextLength() > p_146203_1_)
		{
			model.setText(model.getText().substring(0, p_146203_1_));
		}
	}

	public int getMaxStringLength()
	{
		return this.maxStringLength;
	}

	public int getCursorPosition()
	{
		return this.cursorPosition;
	}

	public boolean getEnableBackgroundDrawing()
	{
		return this.enableBackgroundDrawing;
	}

	public void setEnableBackgroundDrawing(boolean flag)
	{
		this.enableBackgroundDrawing = flag;
	}

	public void setTextColor(int color) {
		this.model.setTextColor(color);
	}

	public void setDisabledTextColour(int color)
	{
		this.disabledColor = color;
	}

	public void setFocused(boolean flag)
	{
		if (flag && !this.isFocused)
		{
			this.cursorCounter = 0;
		}

		this.isFocused = flag;
	}

	public boolean isFocused()
	{
		return this.isFocused;
	}

	public void setEnabled(boolean flag)
	{
		this.isEnabled = flag;
	}

	public int getSelectionEnd()
	{
		return this.selectionEnd;
	}

	public int getWidth()
	{
		return this.width;
	}

	public static interface TextChangeListener {
		public void call(QADTextField qadTextField, String text);
	}

	public int asInteger(int defaultValue) {
		try {
			return Integer.parseInt(this.model.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public float asFloat(float defaultValue) {
		try {
			return Float.parseFloat(this.model.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - xPosition;
		int localMouseY = mouseY - yPosition;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
	}

	public List<String> getTooltip(int mouseX, int mouseY) {
		return getTooltip();
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

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.INPUT;
	}
	
	@Override
	public boolean transferFocus() {
		if(isFocused) {
			isFocused = false;
			return false;
		} else {
			isFocused = true;
			return true;
		}
	}

	@Override
	public void removeFocus() {
		isFocused = false;
	}

}
