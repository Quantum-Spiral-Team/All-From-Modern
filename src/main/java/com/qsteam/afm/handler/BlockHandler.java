package com.qsteam.afm.handler;

import com.qsteam.afm.AllFromModern;
import com.qsteam.afm.block.*;
import com.qsteam.afm.block.BlockPrismarineSlab;
import com.qsteam.afm.block.BlockPrismarineStairs;
import com.qsteam.afm.item.itemblock.ItemBlockWooden;
import com.qsteam.afm.mixin.pumpkin.GuiIngameAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class BlockHandler {

    private static final List<String> WOOD_TYPES = Collections.unmodifiableList(
        Arrays.asList("spruce", "birch", "jungle", "acacia", "dark_oak"));
    private static final List<Block> BLOCKS;
    private static final List<SlabPair> SLAB_PAIRS = new ArrayList<>();
    private static final Map<Block, ItemBlock> META_BLOCKS;

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

        Map<Block, ItemBlock> metaBlocks = new LinkedHashMap<>();
        BlockStrippedOldLog strippedOldLog = new BlockStrippedOldLog();
        metaBlocks.put(strippedOldLog, new ItemBlockWooden(strippedOldLog));
        BlockStrippedNewLog strippedNewLog = new BlockStrippedNewLog();
        metaBlocks.put(strippedNewLog, new ItemBlockWooden(strippedNewLog));
        BlockOldWood oldWood = new BlockOldWood();
        metaBlocks.put(oldWood, new ItemBlockWooden(oldWood));
        BlockNewWood blockNewWood = new BlockNewWood();
        metaBlocks.put(blockNewWood, new ItemBlockWooden(blockNewWood));
        BlockStrippedOldWood strippedOldWood = new BlockStrippedOldWood();
        metaBlocks.put(strippedOldWood, new ItemBlockWooden(strippedOldWood));
        BlockStrippedNewWood strippedNewWood = new BlockStrippedNewWood();
        metaBlocks.put(strippedNewWood, new ItemBlockWooden(strippedNewWood));
        META_BLOCKS = metaBlocks;
    }

    private static void addWoodenBlocks(List<Block> blocks, Function<String, Block> factory) {
        for (String wood : WOOD_TYPES) {
            blocks.add(factory.apply(wood));
        }
    }

    private static void registerBlockWithoutItem(Block block) {
        ForgeRegistries.BLOCKS.register(block);
    }

    private static void registerBlock(Block block) {
        registerBlockWithoutItem(block);
        if (block instanceof BlockCarvedPumpkin) {
            ForgeRegistries.ITEMS.register(new ItemBlock(block) {
                @Override
                public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
                    return armorType == EntityEquipmentSlot.HEAD;
                }

                @Override
                @SideOnly(Side.CLIENT)
                public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks) {
                    ((GuiIngameAccessor) Minecraft.getMinecraft().ingameGUI).invokeRenderPumpkinOverlay(resolution);
                }
            }.setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        } else if (block instanceof BlockPrismarineSlab.Half) {
            for (SlabPair pair : SLAB_PAIRS) {
                if (pair.half == block) {
                    ForgeRegistries.ITEMS.register(new ItemSlab(pair.half, pair.half, pair.doubleSlab)
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName())));
                    break;
                }
            }
        } else if (!(block instanceof BlockPrismarineSlab.Double)) {
            ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    private static void registerMetaBlock(Block block, ItemBlock itemBlock) {
        registerBlockWithoutItem(block);
        ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName())));
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
    public static void render() {
        BLOCKS.forEach(block -> {
            if (!(block instanceof BlockPrismarineSlab.Double)) {
                Item item = Item.getItemFromBlock(block);
                if (item != Items.AIR) {
                    ModelLoader.setCustomModelResourceLocation(item, 0,
                            new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory"));
                }
            }
        });

        META_BLOCKS.forEach((block, item) -> {
            if (block instanceof BlockStrippedOldLog || block instanceof BlockOldWood || block instanceof BlockStrippedOldWood) {
                String[] types = {"oak", "spruce", "birch", "jungle"};
                for (int i = 0; i < types.length; i++) {
                    ModelLoader.setCustomModelResourceLocation(item, i,
                            new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), (block instanceof BlockLog ? "axis=y,variant=" : "variant=") + types[i]));
                }
            }
            else if (block instanceof BlockStrippedNewLog || block instanceof BlockNewWood || block instanceof BlockStrippedNewWood) {
                String[] types = {"acacia", "dark_oak"};
                for (int i = 0; i < types.length; i++) {
                    ModelLoader.setCustomModelResourceLocation(item, i,
                            new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), (block instanceof BlockLog ? "axis=y,variant=" : "variant=") + types[i]));
                }
            }
        });
    }

    public static void register() {
        BLOCKS.forEach(BlockHandler::registerBlock);
        META_BLOCKS.forEach(BlockHandler::registerMetaBlock);
    }

}
