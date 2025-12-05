package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockBlueIce extends Block {

    public BlockBlueIce() {
        super(Material.ICE, MapColor.ICE);
        setRegistryName("blue_ice");
        setTranslationKey("blue_ice");
        setCreativeTab(AllFromModern.AFM_TAB);
        setResistance(2.8F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public float getSlipperiness(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @Nullable Entity entity) {
        return 0.989F;
    }

    @Override
    public void getDrops(@NotNull NonNullList<ItemStack> drops, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune) {}

    @Override
    public boolean isToolEffective(@NotNull String type, @NotNull IBlockState state) {
        return "pickaxe".equals(type);
    }

}
