package com.qsteam.afm.block.prismarine;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

public class BlockPrismarineStairs extends BlockStairs {

    private final BlockPrismarine.EnumType variant;

    public BlockPrismarineStairs(IBlockState modelState, BlockPrismarine.EnumType variant) {
        super(modelState);
        this.variant = variant;
        setHardness(1.5F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
        setCreativeTab(AllFromModern.AFM_TAB);
        setTranslationKey("prismarine_stairs_" + variant.getTranslationKey());
        setLightOpacity(0);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + ".name");
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull MapColor getMapColor(@NotNull IBlockState state, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return variant == BlockPrismarine.EnumType.ROUGH ? MapColor.CYAN : MapColor.DIAMOND;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, HALF, SHAPE);
    }

}
