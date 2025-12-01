package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;

public class WoodButtonBlock extends BlockButtonWood {

    public WoodButtonBlock(String name) {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(AllFromModern.AFM_TAB);
    }

}
