package com.qsteam.afm.block;

import com.qsteam.afm.api.block.IBlockMeta;
import com.qsteam.afm.block.base.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

@SuppressWarnings("deprecation")
public class BlockNewQuartz extends BlockBase implements IBlockMeta {

    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockNewQuartz() {
        super("quartz_block", Material.ROCK, SoundType.STONE);
        setHardness(0.8F);
        setResistance(0.8F);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.SMOOTH));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumType type : EnumType.values()) {
            items.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public PropertyEnum<?> getVariantProperty() {
        return VARIANT;
    }

    public enum EnumType implements IStringSerializable {
        SMOOTH(0, "smooth"),
        BRICKS(1, "bricks");

        private final int meta;
        private final String name;

        EnumType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata() {
            return this.meta;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= values().length) meta = 0;
            return values()[meta];
        }
    }
}