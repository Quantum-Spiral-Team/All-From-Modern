package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlueIceBlock extends Block {

    public BlueIceBlock() {
        super(Material.ICE, MapColor.ICE);
        setRegistryName("blue_ice");
        setTranslationKey("blue_ice");
        setCreativeTab(AllFromModern.AFM_TAB);
        setResistance(2.8F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        return 0.989F;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        ItemStack heldItem = player.getHeldItemMainhand();
        if (EnchantmentHelper.getEnchantmentLevel(
                Enchantments.SILK_TOUCH, heldItem) > 0) {
            return;
        }
        world.setBlockToAir(pos);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return "pickaxe".equals(type);
    }

}
