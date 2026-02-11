package com.qsteam.afm.enchantment;

import com.qsteam.afm.handler.EnchantmentHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentRiptide extends Enchantment {

    public EnchantmentRiptide() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("riptide");
        this.setName("riptide");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 7;
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
        return super.canApplyTogether(ench) && ench != EnchantmentHandler.LOYALTY && ench != EnchantmentHandler.CHANNELING;
    }
}
