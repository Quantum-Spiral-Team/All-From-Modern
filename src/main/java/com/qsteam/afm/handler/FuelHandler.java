package com.qsteam.afm.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        
        if (item == Item.getItemFromBlock(RegistryHandler.SPRUCE_BUTTON) ||
            item == Item.getItemFromBlock(RegistryHandler.BIRCH_BUTTON )||
            item == Item.getItemFromBlock(RegistryHandler.JUNGLE_BUTTON) ||
            item == Item.getItemFromBlock(RegistryHandler.ACACIA_BUTTON) ||
            item == Item.getItemFromBlock(RegistryHandler.DARK_OAK_BUTTON)) {
            return 100;
        }
        
        return 0;
    }
}
