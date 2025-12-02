package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;

public class WoodenButtonBlock extends BlockButtonWood {

    public WoodenButtonBlock(String wood) {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setTranslationKey(wood + "_button");
        setRegistryName(wood + "_button");
        setCreativeTab(AllFromModern.AFM_TAB);
    }

}
