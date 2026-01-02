package com.qsteam.afm.handler;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        
        if (item == Item.getItemFromBlock(AFMBlocks.SPRUCE_BUTTON) ||
            item == Item.getItemFromBlock(AFMBlocks.BIRCH_BUTTON) ||
            item == Item.getItemFromBlock(AFMBlocks.JUNGLE_BUTTON) ||
            item == Item.getItemFromBlock(AFMBlocks.ACACIA_BUTTON) ||
            item == Item.getItemFromBlock(AFMBlocks.DARK_OAK_BUTTON)) {
            return 100;
        }
        
        return 0;
    }
}
