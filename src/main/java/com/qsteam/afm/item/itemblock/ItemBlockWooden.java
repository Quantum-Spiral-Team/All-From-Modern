package com.qsteam.afm.item.itemblock;

import com.qsteam.afm.block.BlockNewWood;
import com.qsteam.afm.block.BlockStrippedNewLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemBlockWooden extends ItemMultiTexture {

    public ItemBlockWooden(Block block) {
        super(block, block, ItemBlockWooden::getVariantName);
        setHasSubtypes(true);
        setCreativeTab(AFM_TAB);
    }

    private static String getVariantName(ItemStack stack) {
        int meta = stack.getMetadata();
        Block block = ((ItemBlock)stack.getItem()).getBlock();

        int enumIndex;
        if (block instanceof BlockStrippedNewLog ||  block instanceof BlockNewWood) {
            enumIndex = meta + 4;
        } else {
            enumIndex = meta;
        }

        return BlockPlanks.EnumType.byMetadata(enumIndex).getName();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }

}