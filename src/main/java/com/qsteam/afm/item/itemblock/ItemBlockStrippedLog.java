package com.qsteam.afm.item.itemblock;

import com.qsteam.afm.AllFromModern;
import com.qsteam.afm.block.BlockStrippedLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBlockStrippedLog extends ItemMultiTexture {

    private static final String[] VARIANTS = new String[BlockPlanks.EnumType.values().length];

    static {
        int i = 0;
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            VARIANTS[i] = type.getName();
            i++;
        }
    }

    public ItemBlockStrippedLog(Block block) {
        super(block, block, VARIANTS);
        setHasSubtypes(true);
        setCreativeTab(AllFromModern.AFM_TAB);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "_" + BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getName();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            this.block.getSubBlocks(tab, items);
        }
    }

}