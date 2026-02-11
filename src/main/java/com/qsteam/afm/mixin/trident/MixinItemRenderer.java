package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.handler.EnumHandler;
import com.qsteam.afm.item.ItemTrident;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Redirect(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"))
    private EnumAction redirectGetItemUseAction(ItemStack stack) {
        EnumAction action = stack.getItemUseAction();
        // Redirect to NONE for the switch statement to avoid AIOOBE
        if (action == EnumHandler.SPEAR) {
            return EnumAction.NONE;
        }
        return action;
    }

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"), cancellable = true)
    private void onRenderItemSide(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand,
            float swingProgress, ItemStack stack, float equipProgress, CallbackInfo ci) {
        if (stack.getItem() instanceof ItemTrident && player.isHandActive() && player.getActiveItemStack() == stack
                && player.getActiveHand() == hand) {
            boolean flag = hand == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
            boolean isRightHand = enumhandside == EnumHandSide.RIGHT;

            int i = isRightHand ? 1 : -1;

            float f = (float) stack.getMaxItemUseDuration()
                    - ((float) player.getItemInUseCount() - partialTicks + 1.0F);
            float f1 = Math.min(f / 10.0F, 1.0F);

            if (f1 > 0.1F) {
                float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
                float f3 = f1 - 0.1F;
                float f4 = f2 * f3;
                GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
            }

            GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.04F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
            GlStateManager.rotate((float) i * 45.0F, 0.0F, 1.0F, 0.0F);
        }
    }
}
