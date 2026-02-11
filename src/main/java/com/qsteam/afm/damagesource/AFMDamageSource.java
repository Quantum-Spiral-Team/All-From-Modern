package com.qsteam.afm.damagesource;

import com.qsteam.afm.entity.projectile.EntityAbstractArrow;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class AFMDamageSource extends DamageSource {

    public AFMDamageSource(String damageType) {
        super(damageType);
    }

    public static DamageSource causeAbstractArrowDamage(EntityAbstractArrow arrow, Entity shooter) {
        return (new EntityDamageSourceIndirect("arrow", arrow, shooter)).setProjectile();
    }

}
