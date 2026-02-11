package com.qsteam.afm.handler;

import com.qsteam.afm.Tags;
import com.qsteam.afm.entity.projectile.EntityTrident;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityHandler {

    private static int id = 0;

    public static void init() {
        registerEntity("trident", EntityTrident.class, 64, 20, true);
    }

    private static void registerEntity(String name, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendPackets) {
        EntityRegistry.registerModEntity(new ResourceLocation(Tags.MOD_ID, name), entityClass, name, id, Tags.MOD_ID, trackingRange, updateFrequency, sendPackets);
        id++;
    }

}
