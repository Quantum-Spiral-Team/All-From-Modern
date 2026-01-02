package com.qsteam.afm.handler;

import com.qsteam.afm.potion.PotionSlowFalling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class PotionHandler {

    public static final Potion SLOW_FALLING = new PotionSlowFalling();
    public static final PotionType SLOW_FALLING_TYPE = new PotionType("slow_falling", new PotionEffect(SLOW_FALLING, 3600)).setRegistryName("slow_falling");
    public static final PotionType LONG_SLOW_FALLING_TYPE = new PotionType("slow_falling", new PotionEffect(SLOW_FALLING, 9600)).setRegistryName("long_slow_falling");

    public static final PotionType TURTLE_MASTER_TYPE = new PotionType("turtle_master", new PotionEffect[] {
            new PotionEffect(MobEffects.SLOWNESS, 400, 3),
            new PotionEffect(MobEffects.RESISTANCE, 400, 2)
    }).setRegistryName("turtle_master");
    public static final PotionType LONG_TURTLE_MASTER_TYPE = new PotionType("turtle_master", new PotionEffect[] {
            new PotionEffect(MobEffects.SLOWNESS, 800, 3),
            new PotionEffect(MobEffects.RESISTANCE, 800, 2)
    }).setRegistryName("long_turtle_master");
    public static final PotionType STRONG_TURTLE_MASTER_TYPE = new PotionType("turtle_master", new PotionEffect[] {
            new PotionEffect(MobEffects.SLOWNESS, 400, 5),
            new PotionEffect(MobEffects.RESISTANCE, 400, 3)
    }).setRegistryName("strong_turtle_master");


    public static final Potion[] POTIONS = new Potion[]{
            SLOW_FALLING
    };

    public static final PotionType[] POTION_TYPES = {
            SLOW_FALLING_TYPE,
            LONG_SLOW_FALLING_TYPE,
            TURTLE_MASTER_TYPE,
            LONG_TURTLE_MASTER_TYPE,
            STRONG_TURTLE_MASTER_TYPE
    };


    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(POTIONS);
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().registerAll(POTION_TYPES);
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
