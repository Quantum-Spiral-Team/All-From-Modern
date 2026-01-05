package com.qsteam.afm.mixin.pumpkin;

import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    @Inject(
            method = "renderPumpkinOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderPumpkinOverlay(ScaledResolution res, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack helmet = mc.player.inventory.armorItemInSlot(3);

        if (helmet.isEmpty() || helmet.getItem() != Item.getItemFromBlock(RegistryHandler.CARVED_PUMPKIN)) {
            ci.cancel();
        }
    }
}