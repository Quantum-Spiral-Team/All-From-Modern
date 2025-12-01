package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BirchButtonBlock extends BlockButtonWood {

    public BirchButtonBlock() {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setTranslationKey("birch_button");
        setRegistryName("birch_button");
        setCreativeTab(AllFromModern.AFM_TAB);
    }

}
