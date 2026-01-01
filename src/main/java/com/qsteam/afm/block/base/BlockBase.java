package com.qsteam.afm.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockBase extends Block {

    public BlockBase(String name, Material material) {
        super(material);
        setCreativeTab(AFM_TAB);
        setRegistryName(name);
    }

    public BlockBase(String name, Material material, SoundType sound) {
        this(name, material);
        setSoundType(sound);
    }

}
