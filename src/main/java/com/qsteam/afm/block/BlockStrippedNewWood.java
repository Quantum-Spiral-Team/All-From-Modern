package com.qsteam.afm.block;

import com.qsteam.afm.api.block.IBlockMeta;
import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.handler.RegistryHandler;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

@SuppressWarnings("deprecation")
public class BlockStrippedNewWood extends Block implements IBlockMeta, IItemModel {

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class,
            type -> type.getMetadata() >= 4);

    public BlockStrippedNewWood() {
        super(Material.WOOD);
        setSoundType(SoundType.WOOD);
        setRegistryName("stripped_wood2");
        setTranslationKey("stripped_wood");
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AFM_TAB);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA));

        RegistryHandler.registerBlock(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata() - 4;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta + 4));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            if (type.getMetadata() >= 4) {
                items.add(new ItemStack(this, 1, type.getMetadata() - 4));
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public PropertyEnum<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public void registerModels() {
        ClientProxy.registerBlockModel(this);
    }
}