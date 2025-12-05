package com.qsteam.afm.handler;

import com.qsteam.afm.block.*;
import com.qsteam.afm.block.prismarine.BlockPrismarineSlab;
import com.qsteam.afm.block.prismarine.BlockPrismarineStairs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BlockHandler {

    private static final List<String> WOOD_TYPES = Collections.unmodifiableList(
        java.util.Arrays.asList("spruce", "birch", "jungle", "acacia", "dark_oak"));
    private static final List<Block> BLOCKS;

    private static final List<SlabPair> SLAB_PAIRS = new ArrayList<>();

    static {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new BlockBlueIce());
        addWoodenBlocks(blocks, BlockWoodenButton::new);
        addWoodenBlocks(blocks, BlockWoodenPressurePlate::new);
        addWoodenBlocks(blocks, BlockWoodenTrapdoor::new);
        blocks.add(new BlockCarvedPumpkin());
        for (BlockPrismarine.EnumType type : BlockPrismarine.EnumType.values()) {
            blocks.add(new BlockPrismarineStairs(Blocks.PRISMARINE.getStateFromMeta(type.getMetadata()), type)
                .setRegistryName(type.getName() + "_stairs"));
        }
        for (BlockPrismarine.EnumType type : BlockPrismarine.EnumType.values()) {
            BlockPrismarineSlab.Half half = new BlockPrismarineSlab.Half(type);
            BlockPrismarineSlab.Double doubleSlab = new BlockPrismarineSlab.Double(type);
            half.setRegistryName(type.getName() + "_slab");
            doubleSlab.setRegistryName(type.getName() + "_double_slab");
            SLAB_PAIRS.add(new SlabPair(half, doubleSlab));
            blocks.add(half);
            blocks.add(doubleSlab);
        }
        BLOCKS = Collections.unmodifiableList(blocks);
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
        if (block instanceof BlockPrismarineSlab.Half) {
            for (SlabPair pair : SLAB_PAIRS) {
                if (pair.half == block) {
                    ForgeRegistries.ITEMS.register(new ItemSlab(pair.half, pair.half, pair.doubleSlab).setRegistryName(block.getRegistryName()));
                    break;
                }
            }
        } else if (!(block instanceof BlockPrismarineSlab.Double)) {
            ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    private static class SlabPair {
        final BlockPrismarineSlab.Half half;
        final BlockPrismarineSlab.Double doubleSlab;

        SlabPair(BlockPrismarineSlab.Half half, BlockPrismarineSlab.Double doubleSlab) {
            this.half = half;
            this.doubleSlab = doubleSlab;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void renderItemBlock(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                Item.getItemFromBlock(block), 0, 
                new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory")
        );
    }

    @SideOnly(Side.CLIENT)
    public static void render() {
        BLOCKS.forEach(block -> {
            if (!(block instanceof BlockPrismarineSlab.Double)) {
                renderItemBlock(block);
            }
        });
    }

    public static void register() {
        BLOCKS.forEach(BlockHandler::registerBlock);
    }

}
