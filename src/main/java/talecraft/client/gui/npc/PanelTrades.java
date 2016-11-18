package talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultButtonModel;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADButton.ButtonModel;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.layout.QADListLayout;
import talecraft.client.gui.qad.model.AbstractButtonModel;
import talecraft.client.gui.qad.model.DefaultTextFieldModel;
import talecraft.entity.NPC.NPCData;
import talecraft.entity.NPC.NPCShop.NPCTrade;

public class PanelTrades extends NPCPanel{
	
	public PanelTrades(NPCData data, int width, int height){
		super(data, width, height);
	}

	@Override
	public void save(NPCData data) {
		
	}
}
