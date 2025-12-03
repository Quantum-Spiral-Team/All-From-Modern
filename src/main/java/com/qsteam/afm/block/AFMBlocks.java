package com.qsteam.afm.block;

import com.qsteam.afm.Tags;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AFMBlocks {

    private static Block getBlockFromRegistry(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Tags.MOD_ID, name));
    }

    public static final Block BLUE_ICE = getBlockFromRegistry("blue_ice");
    public static final Block SPRUCE_BUTTON = getBlockFromRegistry("spruce_button");
    public static final Block BIRCH_BUTTON = getBlockFromRegistry("birch_button");
    public static final Block JUNGLE_BUTTON = getBlockFromRegistry("jungle_button");
    public static final Block ACACIA_BUTTON = getBlockFromRegistry("acacia_button");
    public static final Block DARK_OAK_BUTTON = getBlockFromRegistry("dark_oak_button");
    public static final Block SPRUCE_PRESSURE_PLATE = getBlockFromRegistry("spruce_pressure_plate");
    public static final Block BIRCH_PRESSURE_PLATE = getBlockFromRegistry("birch_pressure_plate");
    public static final Block JUNGLE_PRESSURE_PLATE = getBlockFromRegistry("jungle_pressure_plate");
    public static final Block ACACIA_PRESSURE_PLATE = getBlockFromRegistry("acacia_pressure_plate");
    public static final Block DARK_OAK_PRESSURE_PLATE = getBlockFromRegistry("dark_oak_pressure_plate");
    public static final Block SPRUCE_TRAPDOOR = getBlockFromRegistry("spruce_trapdoor");
    public static final Block BIRCH_TRAPDOOR = getBlockFromRegistry("birch_trapdoor");
    public static final Block JUNGLE_TRAPDOOR = getBlockFromRegistry("jungle_trapdoor");
    public static final Block ACACIA_TRAPDOOR = getBlockFromRegistry("acacia_trapdoor");
    public static final Block DARK_OAK_TRAPDOOR = getBlockFromRegistry("dark_oak_trapdoor");
    public static final Block CARVED_PUMPKIN = getBlockFromRegistry("carved_pumpkin");

}