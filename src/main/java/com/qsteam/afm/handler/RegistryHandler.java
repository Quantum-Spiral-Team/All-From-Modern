package com.qsteam.afm.handler;

import com.qsteam.afm.api.block.IBlockMeta;
import com.qsteam.afm.block.*;
import com.qsteam.afm.block.base.BlockBase;
import com.qsteam.afm.block.base.BlockDoubleSlabBase;
import com.qsteam.afm.block.base.BlockHalfSlabBase;
import com.qsteam.afm.block.base.BlockStairsBase;
import com.qsteam.afm.item.base.ItemArmorBase;
import com.qsteam.afm.item.base.ItemBase;
import com.qsteam.afm.item.base.itemblock.ItemMetaBlockBase;
import com.qsteam.afm.mixin.pumpkin.GuiIngameAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

@SuppressWarnings("ConstantConditions")
@EventBusSubscriber
public class RegistryHandler {

    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Block BLUE_ICE;
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
    public static final Block CARVED_PUMPKIN;
    public static final Block ROUGH_PRISMARINE_STAIRS = getBlockFromRegistry("prismarine_stairs");
    public static final Block DARK_PRISMARINE_STAIRS =  getBlockFromRegistry("dark_prismarine_stairs");
    public static final Block PRISMARINE_BRICKS_STAIRS =  getBlockFromRegistry("prismarine_bricks_stairs");
    public static final Block ROUGH_PRISMARINE_SLAB = getBlockFromRegistry("prismarine_slab");
    public static final Block DARK_PRISMARINE_SLAB =  getBlockFromRegistry("dark_prismarine_slab");
    public static final Block PRISMARINE_BRICKS_SLAB =  getBlockFromRegistry("prismarine_bricks_slab");
    public static final Block STRIPPED_LOG;
    public static final Block STRIPPED_LOG2;
    public static final Block WOOD;
    public static final Block WOOD2;
    public static final Block STRIPPED_WOOD;
    public static final Block STRIPPED_WOOD2;
    public static final Block QUARTZ;
    public static final Block SMOOTH_QUARTZ_STAIRS;

    public static final Item TURTLE_SCUTE;
    public static final Item PHANTOM_MEMBRANE;

    public static final ItemArmor TURTLE_HELMET;

    static {
        BLUE_ICE = new BlockBase("blue_ice", Material.PACKED_ICE, SoundType.GLASS) {
            @Override
            public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
                return 0.989F;
            }

            @Override
            public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
                // no drop without silk touch
            }

            @Override
            public boolean isToolEffective(String type, IBlockState state) {
                return type.equals("pickaxe");
            }
        }.setResistance(2.8F).setHardness(2.8F);

        List<String> woodTypes = Arrays.asList("spruce", "birch", "jungle", "acacia", "dark_oak");
        woodTypes.forEach(BlockWoodenButton::new);
        woodTypes.forEach(BlockWoodenPressurePlate::new);
        woodTypes.forEach(BlockWoodenTrapdoor::new);

        CARVED_PUMPKIN = new BlockCarvedPumpkin();
        STRIPPED_LOG = new BlockStrippedOldLog();
        STRIPPED_LOG2 = new BlockStrippedNewLog();
        WOOD = new BlockOldWood();
        WOOD2 = new BlockNewWood();
        STRIPPED_WOOD = new BlockStrippedOldWood();
        STRIPPED_WOOD2 = new BlockStrippedNewWood();
        QUARTZ = new BlockNewQuartz();
        SMOOTH_QUARTZ_STAIRS = new BlockSmoothQuartzStairs(QUARTZ);

        List<Block> stairsToRegister = new ArrayList<>();
        Object[][] prismarineTypes = {
                {BlockPrismarine.EnumType.ROUGH, "prismarine"},
                {BlockPrismarine.EnumType.BRICKS, "prismarine_bricks"},
                {BlockPrismarine.EnumType.DARK, "dark_prismarine"}
        };

        for (Object[] typeData : prismarineTypes) {
            BlockPrismarine.EnumType type = (BlockPrismarine.EnumType) typeData[0];
            String baseName = (String) typeData[1];
            IBlockState state = Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, type);

            stairsToRegister.add(new BlockStairsBase(baseName + "_stairs", state));
        }

        for (Block stair : stairsToRegister) {
            registerBlock(stair);
        }
        for (Object[] typeData : prismarineTypes) {
            String baseName = (String) typeData[1];
            registerSlab(baseName + "_slab", Material.ROCK);
        }

        TURTLE_SCUTE = new ItemBase("turtle_scute");
        TURTLE_HELMET = new ItemArmorBase("turtle_helmet", ItemArmorBase.TURTLE_SCUTE, EntityEquipmentSlot.HEAD);
        PHANTOM_MEMBRANE = new ItemBase("phantom_membrane");

    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
    }

    public static void registerBlock(Block block, @Nullable String  name) {
        if (name != null && block.getRegistryName() == null) {
            block.setTranslationKey(name);
            block.setRegistryName(name);
        }
        if (block.getRegistryName() == null) throw new NullPointerException("Registry name is null");

        RegistryHandler.BLOCKS.add(block);

        if (block instanceof BlockDoubleSlabBase) return;

        ItemBlock itemBlock;
        if (block instanceof BlockCarvedPumpkin) {
            itemBlock = new ItemBlock(block) {
                @Override
                public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
                    return EntityEquipmentSlot.HEAD;
                }

                @Override
                public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
                    return armorType == EntityEquipmentSlot.HEAD;
                }

                @Override
                @SideOnly(Side.CLIENT)
                public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks) {
                    ((GuiIngameAccessor) Minecraft.getMinecraft().ingameGUI).invokeRenderPumpkinOverlay(resolution);
                }
            };
        } else if (block instanceof IBlockMeta) {
            itemBlock = new ItemMetaBlockBase(block, ((IBlockMeta) block).getVariantProperty());
        } else {
            itemBlock = new  ItemBlock(block);
        }
        itemBlock.setRegistryName(block.getRegistryName());
        RegistryHandler.ITEMS.add(itemBlock);
    }

    public static void registerBlock(Block block) {
        registerBlock(block, null);
    }

    public static void registerSlab(String name, Material material) {
        BlockHalfSlabBase half = new BlockHalfSlabBase(material, name);

        String doubleName = name.replace("_slab", "_double_slab");
        BlockDoubleSlabBase doubleSlab = new BlockDoubleSlabBase(material, doubleName);

        doubleSlab.setTranslationKey(name);

        BLOCKS.add(half);
        BLOCKS.add(doubleSlab);

        ItemSlab itemSlab = new ItemSlab(half, half, doubleSlab);
        itemSlab.setRegistryName(name);
        itemSlab.setCreativeTab(AFM_TAB);

        ITEMS.add(itemSlab);
    }

    public static void registerItem(Item item, @Nullable String name) {
        if (name != null) {
            item.setRegistryName(name);
            item.setTranslationKey(name);
        }
        if (item.getRegistryName() == null) throw new NullPointerException("Registry name is null");

        RegistryHandler.ITEMS.add(item);
    }

    public static void registerItem(Item item) {
        registerItem(item, null);
    }

    private static Block getBlockFromRegistry(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
    }

    private static Item getItemFromRegistry(String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
    }

}