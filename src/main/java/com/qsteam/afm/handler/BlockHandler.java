package com.qsteam.afm.handler;

import com.qsteam.afm.AllFromModern;
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
import java.util.Arrays;
import java.util.List;

public class BlockHandler {

    private static final String[] WOOD_TYPES = {"spruce", "birch", "jungle", "acacia", "dark_oak"};
    private static final List<Block> BLOCKS;

    static {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new BlockBlueIce());
        Arrays.stream(WOOD_TYPES).forEach(wood -> {
            blocks.add(new BlockWoodenButton(wood));
            blocks.add(new BlockWoodenPressurePlate(wood));
            blocks.add(new BlockWoodenTrapdoor(wood));
        });
        blocks.add(new BlockHorizontalBase("carved_pumpkin", Material.WOOD)
                .setHardness(1.0F));
        BLOCKS = blocks;
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
