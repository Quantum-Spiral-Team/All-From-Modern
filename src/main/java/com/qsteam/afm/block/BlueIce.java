package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlueIce extends Block {

    private static final String NAME = "blue_ice";

    public BlueIce(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlueIce() {
        this(Material.ICE, MapColor.ICE);
        setRegistryName(NAME);
        setTranslationKey(NAME);
        setCreativeTab(AllFromModern.AFM_TAB);
        setResistance(2.8F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity)
    {
        return 0.989F;
    }

}
