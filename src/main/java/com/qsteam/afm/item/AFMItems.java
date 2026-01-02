package com.qsteam.afm.item;

import com.qsteam.afm.Tags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AFMItems {

    private static Item getItemFromRegistry(String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Tags.MOD_ID, name));
    }

    public static final ItemBlock BLUE_ICE = (ItemBlock) getItemFromRegistry("blue_ice");
    public static final ItemBlock SPRUCE_BUTTON = (ItemBlock) getItemFromRegistry("spruce_button");
    public static final ItemBlock BIRCH_BUTTON = (ItemBlock) getItemFromRegistry("birch_button");
    public static final ItemBlock JUNGLE_BUTTON = (ItemBlock) getItemFromRegistry("jungle_button");
    public static final ItemBlock ACACIA_BUTTON = (ItemBlock) getItemFromRegistry("acacia_button");
    public static final ItemBlock DARK_OAK_BUTTON = (ItemBlock) getItemFromRegistry("dark_oak_button");
    public static final ItemBlock SPRUCE_PRESSURE_PLATE = (ItemBlock) getItemFromRegistry("spruce_pressure_plate");
    public static final ItemBlock BIRCH_PRESSURE_PLATE = (ItemBlock) getItemFromRegistry("birch_pressure_plate");
    public static final ItemBlock JUNGLE_PRESSURE_PLATE = (ItemBlock) getItemFromRegistry("jungle_pressure_plate");
    public static final ItemBlock ACACIA_PRESSURE_PLATE = (ItemBlock) getItemFromRegistry("acacia_pressure_plate");
    public static final ItemBlock DARK_OAK_PRESSURE_PLATE = (ItemBlock) getItemFromRegistry("dark_oak_pressure_plate");
    public static final ItemBlock SPRUCE_TRAPDOOR = (ItemBlock) getItemFromRegistry("spruce_trapdoor");
    public static final ItemBlock BIRCH_TRAPDOOR = (ItemBlock) getItemFromRegistry("birch_trapdoor");
    public static final ItemBlock JUNGLE_TRAPDOOR = (ItemBlock) getItemFromRegistry("jungle_trapdoor");
    public static final ItemBlock ACACIA_TRAPDOOR = (ItemBlock) getItemFromRegistry("acacia_trapdoor");
    public static final ItemBlock DARK_OAK_TRAPDOOR = (ItemBlock) getItemFromRegistry("dark_oak_trapdoor");
    public static final ItemBlock CARVED_PUMPKIN = (ItemBlock) getItemFromRegistry("carved_pumpkin");
    public static final ItemBlock STRIPPED_LOG = (ItemBlock) getItemFromRegistry("stripped_log");
    public static final ItemBlock STRIPPED_LOG2 = (ItemBlock) getItemFromRegistry("stripped_log2");
    public static final ItemBlock WOOD = (ItemBlock) getItemFromRegistry("wood");
    public static final ItemBlock WOOD2 = (ItemBlock) getItemFromRegistry("wood2");
    public static final ItemBlock STRIPPED_WOOD = (ItemBlock) getItemFromRegistry("stripped_wood");
    public static final ItemBlock STRIPPED_WOOD2 = (ItemBlock) getItemFromRegistry("stripped_wood2");


    public static final Item TURTLE_SCUTE = getItemFromRegistry("turtle_scute");
    public static final Item PHANTOM_MEMBRANE = getItemFromRegistry("phantom_membrane");

    public static final ItemArmor TURTLE_HELMET = (ItemArmor) getItemFromRegistry("turtle_helmet");

}
