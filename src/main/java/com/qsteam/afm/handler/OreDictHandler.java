package com.qsteam.afm.handler;

//import com.qsteam.afm.AllFromModern;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

//import java.util.List;
//import java.util.stream.Collectors;

public class OreDictHandler {

    public static void registerOreDictAFMPlanks() {
        OreDictionary.registerOre("afmPlanks", new ItemStack(Blocks.PLANKS, 1, 0));
        for (ItemStack stack : OreDictionary.getOres("plankWood")) {
            if (stack.getItem() != Item.getItemFromBlock(Blocks.PLANKS)) {
                OreDictionary.registerOre("afmPlanks", stack);
            }
        }
//        List<ItemStack> afmPlanks = OreDictionary.getOres("afmPlanks");
//        List<String> names = afmPlanks.stream()
//                .map(ItemStack::getDisplayName)
//                .collect(Collectors.toList());
//        AllFromModern.LOGGER.info("Registered {} planks: {}", afmPlanks.size(), names);
    }

}
