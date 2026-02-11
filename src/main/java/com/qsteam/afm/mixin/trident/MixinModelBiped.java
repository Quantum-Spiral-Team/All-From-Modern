package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.handler.EnumHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    private void onSetRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) entityIn;
            ItemStack itemstack = entity.getHeldItemMainhand();
            ItemStack itemstack1 = entity.getHeldItemOffhand();

            ModelBiped model = (ModelBiped) (Object) this;

            if (!itemstack.isEmpty() && itemstack.getItemUseAction() == EnumHandler.SPEAR
                    && entity.getItemInUseCount() > 0) {
                if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
                    model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5F
                            - ((float) Math.PI * 0.6F);
                    model.bipedRightArm.rotateAngleY = 0.0F;
                } else {
                    model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI * 0.6F);
                    model.bipedLeftArm.rotateAngleY = 0.0F;
                }
            }

            if (!itemstack1.isEmpty() && itemstack1.getItemUseAction() == EnumHandler.SPEAR
                    && entity.getItemInUseCount() > 0) {
                if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
                    model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI * 0.6F);
                    model.bipedLeftArm.rotateAngleY = 0.0F;
                } else {
                    model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5F
                            - ((float) Math.PI * 0.6F);
                    model.bipedRightArm.rotateAngleY = 0.0F;
                }
            }
        }
    }
}
