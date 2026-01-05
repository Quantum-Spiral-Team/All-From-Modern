package com.qsteam.afm.block.base;

import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockHalfSlabBase extends BlockSlab implements IItemModel {

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockHalfSlabBase(Material material, String name) {
        super(material);
        setRegistryName(name);
        setTranslationKey(name);
        setCreativeTab(AFM_TAB);

        IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
        if(!this.isDouble()) state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        setDefaultState(state);
        this.useNeighborBrightness = true;
    }

    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override public boolean isDouble() {
        return false;
    }
    @Override public IProperty<?> getVariantProperty() {
        return VARIANT;
    }
    @Override public Comparable<?> getTypeForItem(ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
        if(!this.isDouble()) state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if(!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) meta |= 8;
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, VARIANT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ClientProxy.registerBlockModel(this);
    }

    public enum Variant implements IStringSerializable {
        DEFAULT;
        @Override public String getName() {
            return "default";
        }
    }
}