package com.qsteam.afm.mixin.pumpkin;

import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerSnowmanHead.class)
public abstract class MixinLayerSnowmanHead {

    @Redirect(method = "doRenderLayer*", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
    private ItemStack useCarvedPumpkin(net.minecraft.block.Block block, int count) {
        return new ItemStack(RegistryHandler.CARVED_PUMPKIN, 1);
    }

}
