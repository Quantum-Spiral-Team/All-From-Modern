package com.qsteam.afm.mixin.golems;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerSnowmanHead.class)
public class MixinLayerSnowmanHead {

    @Redirect(method = "doRenderLayer*", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
    private ItemStack useCarvedPumpkin(net.minecraft.block.Block block, int count) {
        return new ItemStack(AFMBlocks.CARVED_PUMPKIN, 1);
    }

}
