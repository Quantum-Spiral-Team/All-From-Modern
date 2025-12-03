package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

    public BlockBase(String name, Material material) {
        super(material);
        setCreativeTab(AllFromModern.AFM_TAB);
        setRegistryName(name);
        setTranslationKey(name);
    }

}
