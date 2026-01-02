package com.qsteam.afm.handler;

import com.qsteam.afm.block.AFMBlocks;
import com.qsteam.afm.item.AFMItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        
        if (item == AFMItems.SPRUCE_BUTTON ||
            item == AFMItems.BIRCH_BUTTON ||
            item == AFMItems.JUNGLE_BUTTON ||
            item == AFMItems.ACACIA_BUTTON ||
            item == AFMItems.DARK_OAK_BUTTON) {
            return 100;
        }
        
        return 0;
    }
}
