package com.qsteam.afm.handler;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.init.Blocks;

public class FlammableBlockHandler {

    public static void register() {
        Blocks.FIRE.setFireInfo(AFMBlocks.WOOD, 5, 5);
        Blocks.FIRE.setFireInfo(AFMBlocks.WOOD2, 5, 5);
        Blocks.FIRE.setFireInfo(AFMBlocks.STRIPPED_LOG, 5, 5);
        Blocks.FIRE.setFireInfo(AFMBlocks.STRIPPED_LOG2, 5, 5);
        Blocks.FIRE.setFireInfo(AFMBlocks.STRIPPED_WOOD, 5, 5);
        Blocks.FIRE.setFireInfo(AFMBlocks.STRIPPED_WOOD2, 5, 5);
    }
}
