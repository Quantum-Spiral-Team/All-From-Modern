package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.api.mixin.ILightningCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLightningBolt.class)
public abstract class MixinEntityLightningBolt implements ILightningCaster {

    @Unique
    private EntityPlayer tridentCaster;

    @Override
    public void setCaster(EntityPlayer player) {
        this.tridentCaster = player;
    }

    @Override
    public EntityPlayer getCaster() {
        return this.tridentCaster;
    }

}
