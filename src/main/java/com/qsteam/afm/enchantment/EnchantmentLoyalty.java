package com.qsteam.afm.enchantment;

import com.qsteam.afm.handler.EnchantmentHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentLoyalty extends Enchantment {
    public EnchantmentLoyalty() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("loyalty");
        this.setName("loyalty");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + enchantmentLevel * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench != EnchantmentHandler.RIPTIDE;
    }
}