package com.qsteam.afm.item.base;

import com.qsteam.afm.Tags;
import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.handler.RegistryHandler;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Objects;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemArmorBase extends ItemArmor implements IItemModel {

    public static final ItemArmor.ArmorMaterial TURTLE_SCUTE = Objects.requireNonNull(EnumHelper.addArmorMaterial(
            "turtle",
            Tags.MOD_ID + ":turtle",
            25,
            new int[]{2, 5, 6, 2, 5},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F
    ));

    public ItemArmorBase(String name, ArmorMaterial material, int renderIndex, EntityEquipmentSlot armorType) {
        super(material,renderIndex, armorType);
        setRegistryName(name);
        setTranslationKey(name);
        setCreativeTab(AFM_TAB);
        
        RegistryHandler.registerItem(this);
    }

    public ItemArmorBase(String name, ArmorMaterial material, EntityEquipmentSlot armorType) {
        this(name, material, 0, armorType);
    }

    @Override
    public void registerModels() {
        ClientProxy.registerItemModel(this);
    }

}
