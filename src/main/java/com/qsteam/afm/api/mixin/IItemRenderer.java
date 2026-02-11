package com.qsteam.afm.api.mixin;

import net.minecraft.client.renderer.texture.TextureManager;

public interface IItemRenderer {
    static void renderEffect(TextureManager manager, Runnable runnable, int ticks) {}
}
