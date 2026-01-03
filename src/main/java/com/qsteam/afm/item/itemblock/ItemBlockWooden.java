package com.qsteam.afm.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemBlockWooden extends ItemMultiTexture {

    public ItemBlockWooden(Block block) {
        super(block, block, stack -> BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getName());
        setHasSubtypes(true);
        setCreativeTab(AFM_TAB);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }

}