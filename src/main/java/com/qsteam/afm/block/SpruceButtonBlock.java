package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpruceButtonBlock extends BlockButtonWood {

    public SpruceButtonBlock() {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setTranslationKey("spruce_button");
        setRegistryName("spruce_button");
        setCreativeTab(AllFromModern.AFM_TAB);
    }

}
