package com.qsteam.afm.block.base;

import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.jetbrains.annotations.Nullable;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockBase extends Block {

    public BlockBase(String name, Material material, @Nullable SoundType sound) {
        super(material);
        setCreativeTab(AFM_TAB);
        if (sound != null) setSoundType(sound);

        RegistryHandler.registerBlock(this,  name);
    }

    public BlockBase(String name, Material material) {
        this(name ,material,null);
    }

}
