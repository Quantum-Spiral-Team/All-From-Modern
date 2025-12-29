package com.qsteam.afm.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

@SuppressWarnings("deprecation")
public class BlockStrippedNewLog extends BlockLog {

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class,
            type -> type.getMetadata() >= 4);

    public BlockStrippedNewLog() {
        super();
        setRegistryName("stripped_log2");
        setTranslationKey("stripped_log2");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(AFM_TAB);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) { return false; }
    @Override public boolean isWood(IBlockAccess world, BlockPos pos) { return false; }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(VARIANT).getMapColor();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // Мы добавляем 4, потому что в метаданных этого блока 0 - это Акация (которая в EnumType имеет ID 4)
        IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta & 3) + 4));

        switch (meta & 12) {
            case 0: state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y); break;
            case 4: state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X); break;
            case 8: state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z); break;
            default: state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(VARIANT).getMetadata() - 4; // Вычитаем 4, чтобы уместить в мета 0-3

        switch (state.getValue(LOG_AXIS)) {
            case X: i |= 4; break;
            case Z: i |= 8; break;
            case NONE: i |= 12; break;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, LOG_AXIS);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata() - 4;
    }
}