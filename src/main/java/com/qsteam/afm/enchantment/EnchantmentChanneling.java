package com.qsteam.afm.enchantment;

import com.qsteam.afm.handler.EnchantmentHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentChanneling extends Enchantment {
    public EnchantmentChanneling() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("channeling");
        this.setName("channeling");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench != EnchantmentHandler.RIPTIDE;
    }
}
