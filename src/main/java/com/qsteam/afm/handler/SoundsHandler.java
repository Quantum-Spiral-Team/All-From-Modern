package com.qsteam.afm.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class SoundsHandler {
    
    public static final SoundEvent BLOCK_PUMPKIN_CARVE = createSoundEvent("block.pumpkin.carve");
    
    private static SoundEvent createSoundEvent(String soundName) {
        ResourceLocation location = new ResourceLocation("afm", soundName);
        return new SoundEvent(location).setRegistryName(location);
    }
    
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(BLOCK_PUMPKIN_CARVE);
    }
}
