package com.qsteam.afm.handler;

import net.minecraft.init.Blocks;

public class FlammableBlockHandler {

    public static void register() {
        Blocks.FIRE.setFireInfo(RegistryHandler.WOOD, 5, 5);
        Blocks.FIRE.setFireInfo(RegistryHandler.WOOD2, 5, 5);
        Blocks.FIRE.setFireInfo(RegistryHandler.STRIPPED_LOG, 5, 5);
        Blocks.FIRE.setFireInfo(RegistryHandler.STRIPPED_LOG2, 5, 5);
        Blocks.FIRE.setFireInfo(RegistryHandler.STRIPPED_WOOD, 5, 5);
        Blocks.FIRE.setFireInfo(RegistryHandler.STRIPPED_WOOD2, 5, 5);
    }
}
