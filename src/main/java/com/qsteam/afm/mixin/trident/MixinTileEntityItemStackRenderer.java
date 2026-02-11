package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.AllFromModern;
import com.qsteam.afm.Tags;
import com.qsteam.afm.client.model.ModelTrident;
import com.qsteam.afm.item.ItemTrident;
import com.qsteam.afm.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityItemStackRenderer.class)
public class MixinTileEntityItemStackRenderer {

    @Unique private static final ModelTrident MODEL_TRIDENT = new ModelTrident();
    @Unique private static final ResourceLocation TRIDENT_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/entity/trident.png");

    @Inject(
            method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void afterItemInit(ItemStack itemStackIn, float partialTicks, CallbackInfo ci) {
        if (itemStackIn.getItem() instanceof ItemTrident) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TRIDENT_TEXTURE);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            MODEL_TRIDENT.render();

            if (itemStackIn.hasEffect()) {
                renderEffect(Minecraft.getMinecraft().getTextureManager(), MODEL_TRIDENT::render);
            }
            GlStateManager.popMatrix();
            ci.cancel();
        }
    }

    @Unique
    private static void renderEffect(TextureManager manager, Runnable runnable) {
        GlStateManager.color(0.5019608F, 0.2509804F, 0.8F);
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        manager.bindTexture((RenderHelper.ENCHANTED_ITEM_GLINT_RES));
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1,1,1);
        float f = (Minecraft.getSystemTime() % 3000L) / 3000.0F / 1;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        runnable.run();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1,1,1);
        float f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0F / 1;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        runnable.run();
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        manager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

}
