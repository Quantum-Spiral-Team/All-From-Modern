package com.qsteam.afm.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockBlueIce extends Block {

    public BlockBlueIce() {
        super(Material.ICE);
        setRegistryName("blue_ice");
        setTranslationKey("blue_ice");
        setResistance(2.8F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
        setCreativeTab(AFM_TAB);
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return 0.989F;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {}

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return "pickaxe".equals(type);
    }

}
