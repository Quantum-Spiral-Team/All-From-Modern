package com.qsteam.afm.item;

import com.qsteam.afm.Tags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Objects;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemTurtleShell extends ItemArmor {

    public static final ItemArmor.ArmorMaterial TURTLE_SCUTE = Objects.requireNonNull(EnumHelper.addArmorMaterial(
            "turtle",
            Tags.MOD_ID + ":turtle",
            25,
            new int[]{2, 5, 6, 2, 5},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F
    ));

    public ItemTurtleShell() {
        super(TURTLE_SCUTE, 0, EntityEquipmentSlot.HEAD);
        setRegistryName("turtle_helmet");
        setTranslationKey("turtle_helmet");
        setCreativeTab(AFM_TAB);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 201, 0, false, false));
        }
    }
}
