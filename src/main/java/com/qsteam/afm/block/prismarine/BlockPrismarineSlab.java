package com.qsteam.afm.block.prismarine;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

public abstract class BlockPrismarineSlab extends BlockSlab {

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
    private final BlockPrismarine.EnumType prismarineType;

    public BlockPrismarineSlab(BlockPrismarine.EnumType type) {
        super(Material.ROCK);
        this.prismarineType = type;
        IBlockState state = this.blockState.getBaseState();
        if (!this.isDouble()) {
            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
        setHardness(1.5F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
        setCreativeTab(AllFromModern.AFM_TAB);
        setTranslationKey("prismarine_slab_" + type.getTranslationKey());
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull MapColor getMapColor(@NotNull IBlockState state, @NotNull IBlockAccess worldIn, BlockPos pos) {
        return prismarineType == BlockPrismarine.EnumType.ROUGH ? MapColor.CYAN : MapColor.DIAMOND;
    }

    @Override
    public @NotNull String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override
    public @NotNull IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public @NotNull Comparable<?> getTypeForItem(ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) {
            state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return state;
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        int i = 0;
        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }

    public static class Half extends BlockPrismarineSlab {
        public Half(BlockPrismarine.EnumType type) {
            super(type);
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }

    public static class Double extends BlockPrismarineSlab {
        public Double(BlockPrismarine.EnumType type) {
            super(type);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public enum Variant implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return "default";
        }
    }
}
