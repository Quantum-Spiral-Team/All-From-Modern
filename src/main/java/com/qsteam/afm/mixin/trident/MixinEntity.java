package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.api.mixin.ILightningCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Redirect(
            method = "onStruckByLightning(Lnet/minecraft/entity/effect/EntityLightningBolt;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"
            )
    )
    private boolean redirectAttackWithBolt(Entity instance, DamageSource source, float amount, EntityLightningBolt lightningBolt) {
        EntityPlayer caster = ((ILightningCaster) lightningBolt).getCaster();
        DamageSource customSource;
        if (caster != null) {
            customSource = new EntityDamageSource("lightningBolt", caster)
                    .setDamageBypassesArmor()
                    .setFireDamage();
        } else {
            customSource = DamageSource.LIGHTNING_BOLT;
        }

        return instance.attackEntityFrom(customSource, amount);
    }

}
