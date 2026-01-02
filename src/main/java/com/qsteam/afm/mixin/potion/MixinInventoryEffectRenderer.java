package com.qsteam.afm.mixin.potion;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer {

    @Redirect(
            method = "drawActivePotionEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I", ordinal = 0)
    )
    private int drawCorrectedPotionName(FontRenderer fontRenderer, String text, float x, float y, int color, @Local PotionEffect potionEffect) {
        String name = I18n.format(potionEffect.getPotion().getName());

        if (potionEffect.getAmplifier() > 0) {
            name = name + " " + I18n.format("potion.potency." + potionEffect.getAmplifier());
        }

        return fontRenderer.drawStringWithShadow(name, x, y, color);
    }

}