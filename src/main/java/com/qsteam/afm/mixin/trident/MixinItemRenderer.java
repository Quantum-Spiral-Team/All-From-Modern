package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.handler.EnumHandler;
import com.qsteam.afm.item.ItemTrident;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"
            ))
    private void onRenderItemSide(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand,
                                  float swingProgress, ItemStack stack, float equippedProgress, CallbackInfo ci) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemTrident && stack.getItemUseAction() == EnumHandler.SPEAR
            && player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand) {
            int side = player.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;

            GlStateManager.translate(side * 0.56F, -0.52F + equippedProgress * -0.6F, -0.72F);
            GlStateManager.translate(side * -0.2F, 0.2F, 0.1F);
            GlStateManager.rotate(-55.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(side * 35.3F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(side * -9.785F, 0.0F, 0.0F, 1.0F);

            float useTicks = (float)stack.getMaxItemUseDuration() - ((float)player.getItemInUseCount() - partialTicks + 1.0F);
            float pullProgress = Math.min(useTicks / 10.0F, 1.0F);

            if (pullProgress > 0.1F) {
                float shake = MathHelper.sin((useTicks - 0.1F) * 1.3F) * (pullProgress - 0.1F);
                GlStateManager.translate(0.0F, shake * 0.004F, 0.0F);
            }

            GlStateManager.translate(0.0F, 0.0F, pullProgress * 0.2F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F + pullProgress * 0.2F);

            GlStateManager.rotate(side * 45.0F, 0.0F, -1.0F, 0.0F);
        }
    }
}
