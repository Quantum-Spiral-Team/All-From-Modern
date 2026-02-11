package com.qsteam.afm.client.render;

import com.qsteam.afm.client.model.ModelTrident;
import com.qsteam.afm.entity.projectile.EntityTrident;
import com.qsteam.afm.util.RenderHelper;
import com.qsteam.afm.util.math.AFMMathHelper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTrident extends Render<EntityTrident> {

    private final ModelTrident modelTrident = new ModelTrident();

    public RenderTrident(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) y, (float) z);

        GlStateManager.rotate(
                entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F,
                1.0F, 0.0F);
        GlStateManager.rotate(
                entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0F,
                0.0F, 0.0F, 1.0F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.modelTrident.render();

        if (entity.isEnchanted()) {
            RenderHelper.renderEnchantedGlint(this, entity, this.modelTrident::render, partialTicks);
        }

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();

        if (entity.isNoClip()) {
            this.renderLoyaltyBeam(entity, x, y, z, partialTicks);
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();
    }

    protected void renderLoyaltyBeam(EntityTrident trident, double x, double y, double z, float partialTicks) {
        Entity owner = trident.getShooter();

        if (owner == null || owner == trident)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        double yawRadians = AFMMathHelper.lerp(owner.prevRotationYaw, owner.rotationYaw, (partialTicks * 0.5F))
                * ((float) Math.PI / 180F);
        double cosYaw = Math.cos(yawRadians);
        double sinYaw = Math.sin(yawRadians);

        double ownerX = AFMMathHelper.lerp(owner.prevPosX, owner.posX, partialTicks);
        double ownerY = AFMMathHelper.lerp(owner.prevPosY + (double) owner.getEyeHeight() * 0.8,
                owner.posY + (double) owner.getEyeHeight() * 0.8, partialTicks);
        double ownerZ = AFMMathHelper.lerp(owner.prevPosZ, owner.posZ, partialTicks);

        double combinedCosSin = cosYaw - sinYaw;
        double combinedSinCos = sinYaw + cosYaw;

        double tridentX = AFMMathHelper.lerp(trident.prevPosX, trident.posX, partialTicks);
        double tridentY = AFMMathHelper.lerp(trident.prevPosY, trident.posY, partialTicks);
        double tridentZ = AFMMathHelper.lerp(trident.prevPosZ, trident.posZ, partialTicks);

        double diffX = ownerX - tridentX;
        double diffY = ownerY - tridentY;
        double diffZ = ownerZ - tridentZ;

        double distance = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
        int entityTicks = trident.getEntityId() + trident.ticksExisted;
        double animationOffset = (entityTicks + partialTicks) * -0.1;
        double thickness = Math.min(0.5D, distance / 30.0D);

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 255.0F, 255.0F);

        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        int segments = 37;
        int colorShift = 7 - entityTicks % 7;

        for (int i = 0; i <= segments; i++) {
            double progress = (double) i / (double) segments;
            float colorPulse = 1.0F - ((i + colorShift) % 7) / 7.0F;
            double curve = progress * 2.0D - 1.0D;
            curve = (1.0D - curve * curve) * thickness;

            double swing = Math.sin(progress * Math.PI * 8.0D + animationOffset);
            double vertexX = x + diffX * progress + swing * combinedCosSin * curve;
            double vertexY = y + diffY * progress + swing * 0.02D + (0.1D + curve);
            double vertexZ = z + diffZ * progress + swing * combinedSinCos * curve;

            float r = 0.87F * colorPulse + 0.3F * (1.0F - colorPulse);
            float g = 0.91F * colorPulse + 0.6F * (1.0F - colorPulse);
            float b = 0.85F * colorPulse + 0.5F * (1.0F - colorPulse);

            bufferbuilder.pos(vertexX, vertexY, vertexZ).color(r, g, b, 1.0F).endVertex();
            bufferbuilder.pos(vertexX + 0.1D * curve, vertexY + 0.1D * curve, vertexZ).color(r, g, b, 1.0F).endVertex();

            if (i > trident.returningTicks * 2)
                break;
        }
        tessellator.draw();

        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= segments; i++) {
            double progress = (double) i / (double) segments;
            float colorPulse = 1.0F - ((i + colorShift) % 7) / 7.0F;
            double curve = progress * 2.0D - 1.0D;
            curve = (1.0D - curve * curve) * thickness;

            double swing = Math.sin(progress * Math.PI * 8.0D + animationOffset);
            double vertexX = x + diffX * progress + swing * combinedCosSin * curve;
            double vertexY = y + diffY * progress + swing * 0.01D + (0.1D + curve);
            double vertexZ = z + diffZ * progress + swing * combinedSinCos * curve;

            float r = 0.87F * colorPulse + 0.3F * (1.0F - colorPulse);
            float g = 0.91F * colorPulse + 0.6F * (1.0F - colorPulse);
            float b = 0.85F * colorPulse + 0.5F * (1.0F - colorPulse);

            bufferbuilder.pos(vertexX, vertexY, vertexZ).color(r, g, b, 1.0F).endVertex();
            bufferbuilder.pos(vertexX + 0.1D * curve, vertexY, vertexZ + 0.1D * curve).color(r, g, b, 1.0F).endVertex();

            if (i > trident.returningTicks * 2) break;
        }
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return ModelTrident.TRIDENT_TEXTURE;
    }

}