package com.qsteam.afm.block;

import com.qsteam.afm.api.block.IBlockMeta;
import com.qsteam.afm.block.base.BlockBase;
import com.qsteam.afm.handler.RegistryHandler;
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
import net.minecraft.util.ResourceLocation;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

@SuppressWarnings("deprecation")
public class BlockOldWood extends Block implements IBlockMeta {

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class,
            type -> type.getMetadata() < 4);

    public BlockOldWood() {
        super(Material.WOOD);
        setRegistryName("wood");
        setTranslationKey("wood_block");
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AFM_TAB);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));

        RegistryHandler.registerBlock(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            if (type.getMetadata() < 4) {
                items.add(new ItemStack(this, 1, type.getMetadata()));
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
}