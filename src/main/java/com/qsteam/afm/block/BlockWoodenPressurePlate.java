package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWoodenPressurePlate extends BlockPressurePlate {

    public BlockWoodenPressurePlate(String wood) {
        super(Material.WOOD, Sensitivity.EVERYTHING);
        setSoundType(SoundType.WOOD);
        setHardness(0.5F);
        setTranslationKey(wood + "_pressure_plate");
        setRegistryName(wood + "_pressure_plate");
        setCreativeTab(AllFromModern.AFM_TAB);
    }

}
