package com.qsteam.afm.block;

import com.qsteam.afm.Tags;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Blocks {

    private static Block getBlockFromRegistry(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Tags.MOD_ID, name));
    }

    public static final Block BLUE_ICE = getBlockFromRegistry("blue_ice");
    public static final Block SPRUCE_BUTTON = getBlockFromRegistry("spruce_button");
    public static final Block BIRCH_BUTTON = getBlockFromRegistry("birch_button");
    public static final Block JUNGLE_BUTTON = getBlockFromRegistry("jungle_button");
    public static final Block ACACIA_BUTTON = getBlockFromRegistry("acacia_button");
    public static final Block DARK_OAK_BUTTON = getBlockFromRegistry("darl_oak_button");

}
