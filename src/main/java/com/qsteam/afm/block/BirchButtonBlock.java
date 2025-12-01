package com.qsteam.afm.block;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BirchButtonBlock extends BlockButtonWood {

    private static final String NAME = "birch_button";

    public BirchButtonBlock() {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setTranslationKey(NAME);
        setRegistryName(NAME);
        setCreativeTab(AllFromModern.AFM_TAB);
    }

    @Override
    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos) {
        super.playClickSound(player, worldIn, pos);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos) {
        super.playReleaseSound(worldIn, pos);
    }

}
