package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.Tags;
import com.qsteam.afm.client.model.ModelTrident;
import com.qsteam.afm.item.ItemTrident;
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
    @Unique private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Inject(method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V",
            at = @At("HEAD"))
    private void afterItemInit(ItemStack itemStackIn, float partialTicks, CallbackInfo ci) {
        if (itemStackIn.getItem() instanceof ItemTrident) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TRIDENT_TEXTURE);

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            MODEL_TRIDENT.render();

            if (itemStackIn.hasEffect()) {
                GlStateManager.color(0.5019608F, 0.2509804F, 0.8F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
                renderEffect(Minecraft.getMinecraft().getTextureManager(), MODEL_TRIDENT::render);
            }
            GlStateManager.popMatrix();
        }
    }

    @Unique
    private static void renderEffect(TextureManager manager, Runnable runnable) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        manager.bindTexture(RES_ITEM_GLINT);
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
