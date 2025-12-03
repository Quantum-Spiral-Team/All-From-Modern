package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWoodenTrapdoor extends BlockTrapDoor {

    public BlockWoodenTrapdoor(String wood) {
        super(Material.WOOD);
        setSoundType(SoundType.WOOD);
        setTranslationKey(wood + "_trapdoor");
        setRegistryName(wood + "_trapdoor");
        setCreativeTab(AllFromModern.AFM_TAB);
        setHardness(3.0F);
        disableStats();
    }

}
