package com.qsteam.afm.block;

import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockSmoothQuartzStairs extends BlockStairs {

    public BlockSmoothQuartzStairs(Block parent) {
        super(parent.getDefaultState().withProperty(BlockNewQuartz.VARIANT, BlockNewQuartz.EnumType.SMOOTH));
        setRegistryName("smooth_quartz_stairs");
        setTranslationKey("smooth_quartz_stairs");
        setCreativeTab(AFM_TAB);
        useNeighborBrightness = true;
        
        RegistryHandler.registerBlock(this);
    }

}
