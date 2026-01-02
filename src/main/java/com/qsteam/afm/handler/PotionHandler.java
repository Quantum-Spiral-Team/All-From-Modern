package com.qsteam.afm.handler;

import com.qsteam.afm.potion.PotionSlowFalling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@EventBusSubscriber
public class PotionHandler {

    public static final Potion SLOW_FALLING = new PotionSlowFalling();
    public static final PotionType SLOW_FALLING_TYPE = new PotionType("slow_falling", new PotionEffect(SLOW_FALLING, 3600));
    public static final PotionType LONG_SLOW_FALLING_TYPE = new PotionType("slow_falling", new PotionEffect(SLOW_FALLING, 9600));

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(SLOW_FALLING);
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().registerAll(
                SLOW_FALLING_TYPE.setRegistryName(Objects.requireNonNull(SLOW_FALLING.getRegistryName())),
                LONG_SLOW_FALLING_TYPE.setRegistryName("long_" + SLOW_FALLING.getRegistryName())
        );
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if (entity.isPotionActive(SLOW_FALLING)) {
            entity.fallDistance = 0.0F;

            if (entity.motionY <= 0.0D) {
                entity.motionY = Math.max(entity.motionY, -0.1D);
            }
        }
    }

}
