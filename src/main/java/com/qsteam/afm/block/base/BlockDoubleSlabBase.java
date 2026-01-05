package com.qsteam.afm.block.base;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockDoubleSlabBase extends BlockSlab {

    public BlockDoubleSlabBase(Material material, String name) {
        super(material);
        setRegistryName(name);
        this.useNeighborBrightness = true;
    }

    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override public boolean isDouble() {
        return true;
    }
    @Override public IProperty<?> getVariantProperty() {
        return BlockHalfSlabBase.VARIANT;
    }
    @Override public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockHalfSlabBase.Variant.DEFAULT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockHalfSlabBase.VARIANT, BlockHalfSlabBase.Variant.DEFAULT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHalfSlabBase.VARIANT);
    }
}