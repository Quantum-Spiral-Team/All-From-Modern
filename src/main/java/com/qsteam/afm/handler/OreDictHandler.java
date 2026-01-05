package com.qsteam.afm.handler;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHandler {

    public static void registerOreDictAFMPlanks() {

        OreDictionary.registerOre("afmPlanks", new ItemStack(Blocks.PLANKS, 1, 0));
        Item vanillaPlanks = Item.getItemFromBlock(Blocks.PLANKS);
        for (ItemStack stack : OreDictionary.getOres("plankWood")) {
            if (stack.getItem() != vanillaPlanks) {
                OreDictionary.registerOre("afmPlanks", stack);
            }
        }

        OreDictionary.registerOre("blockQuartz", new ItemStack(RegistryHandler.QUARTZ, 1, 0));
        OreDictionary.registerOre("blockQuartz", new ItemStack(RegistryHandler.QUARTZ, 1, 1));
    }
}
