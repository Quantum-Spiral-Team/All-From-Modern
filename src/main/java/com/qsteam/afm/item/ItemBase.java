package com.qsteam.afm.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemBase extends Item {

    public ItemBase(String name) {
        this(name, name);
    }

    public ItemBase(String name, String translationKey) {
        super();
        setRegistryName(name);
        setTranslationKey(translationKey);
        setCreativeTab(AFM_TAB);
    }
}
