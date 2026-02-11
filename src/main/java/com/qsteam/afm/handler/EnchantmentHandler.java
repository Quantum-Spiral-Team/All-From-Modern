package com.qsteam.afm.handler;

import com.qsteam.afm.enchantment.EnchantmentChanneling;
import com.qsteam.afm.enchantment.EnchantmentImpaling;
import com.qsteam.afm.enchantment.EnchantmentLoyalty;
import com.qsteam.afm.enchantment.EnchantmentRiptide;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EnchantmentHandler {

    public static final Enchantment LOYALTY = new EnchantmentLoyalty();
    public static final Enchantment IMPALING = new EnchantmentImpaling();
    public static final Enchantment RIPTIDE = new EnchantmentRiptide();
    public static final Enchantment CHANNELING = new EnchantmentChanneling();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(LOYALTY, IMPALING, RIPTIDE, CHANNELING);
    }

}
