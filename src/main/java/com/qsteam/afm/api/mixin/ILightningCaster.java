package com.qsteam.afm.api.mixin;

import net.minecraft.entity.player.EntityPlayer;

public interface ILightningCaster {
    void setCaster(EntityPlayer player);
    EntityPlayer getCaster();
}