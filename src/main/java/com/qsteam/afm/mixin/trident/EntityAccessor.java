package com.qsteam.afm.mixin.trident;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("fire")
    int getFire();

    @Accessor("fire")
    void setFire(int fire);

}
