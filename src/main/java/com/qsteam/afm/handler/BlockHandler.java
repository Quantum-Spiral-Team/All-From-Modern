package com.qsteam.afm.handler;

import com.qsteam.afm.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockHandler {

    private static final String[] WOOD_TYPES = {"spruce", "birch", "jungle", "acacia", "dark_oak"};
    private static final List<Block> BLOCKS;

    static {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new BlockBlueIce());
        addWoodenBlocks(blocks, BlockWoodenButton::new);
        addWoodenBlocks(blocks, BlockWoodenPressurePlate::new);
        addWoodenBlocks(blocks, BlockWoodenTrapdoor::new);
        blocks.add(new BlockHorizontalBase("carved_pumpkin", Material.WOOD)
                .setHardness(1.0F));
        BLOCKS = blocks;
    }

    private static void addWoodenBlocks(List<Block> blocks, Function<String, Block> factory) {
        for (String wood : WOOD_TYPES) {
            blocks.add(factory.apply(wood));
        }
    }

    public static void registerBlockWithoutItem(Block block) {
        ForgeRegistries.BLOCKS.register(block);
    }

    private static void registerBlock(Block block) {
        registerBlockWithoutItem(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    private static void renderItemBlock(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                Item.getItemFromBlock(block), 0, 
                new ModelResourceLocation(block.getRegistryName(), "inventory")
        );
    }

    @SideOnly(Side.CLIENT)
    public static void render() {
        BLOCKS.forEach(BlockHandler::renderItemBlock);
    }

    public static void register() {
        BLOCKS.forEach(BlockHandler::registerBlock);
    }

}
