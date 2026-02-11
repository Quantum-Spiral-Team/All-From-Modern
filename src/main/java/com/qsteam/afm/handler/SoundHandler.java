package com.qsteam.afm.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class SoundHandler {

    public static final SoundEvent BLOCK_PUMPKIN_CARVE = createSoundEvent("block.pumpkin.carve");
    public static final SoundEvent ITEM_AXE_STRIP = createSoundEvent("item.axe.strip");

    public static final SoundEvent ITEM_TRIDENT_HIT = createSoundEvent("item.trident.hit");
    public static final SoundEvent ITEM_TRIDENT_HIT_GROUND = createSoundEvent("item.trident.hit_ground");
    public static final SoundEvent ITEM_TRIDENT_RETURN = createSoundEvent("item.trident.return");
    public static final SoundEvent ITEM_TRIDENT_THROW = createSoundEvent("item.trident.throw");
    public static final SoundEvent ITEM_TRIDENT_THUNDER = createSoundEvent("item.trident.thunder");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE = createSoundEvent("item.trident.riptide");

    private static SoundEvent createSoundEvent(String soundName) {
        ResourceLocation location = new ResourceLocation("afm", soundName);
        return new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                BLOCK_PUMPKIN_CARVE,
                ITEM_AXE_STRIP,
                ITEM_TRIDENT_HIT,
                ITEM_TRIDENT_HIT_GROUND,
                ITEM_TRIDENT_RETURN,
                ITEM_TRIDENT_THROW,
                ITEM_TRIDENT_THUNDER,
                ITEM_TRIDENT_RIPTIDE);
    }

}
