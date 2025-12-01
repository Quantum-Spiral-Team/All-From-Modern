package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlueIceBlock extends Block {

    private static final String NAME = "blue_ice";

    public BlueIceBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlueIceBlock() {
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

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem().isEnchantable(heldItem)) {
            for (int i = 0; i < heldItem.getEnchantmentTagList().tagCount(); i++) {
                if (heldItem.getEnchantmentTagList().getCompoundTagAt(i).getInteger("id") == 33) {
                    return;
                }
            }
        }
        world.setBlockToAir(pos);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state)
    {
        if ("pickaxe".equals(type)) return true;
        else return false;
    }

}
