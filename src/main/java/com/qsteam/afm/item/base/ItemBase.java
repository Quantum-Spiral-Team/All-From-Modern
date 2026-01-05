package com.qsteam.afm.item.base;

import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.handler.RegistryHandler;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.item.Item;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class ItemBase extends Item implements IItemModel {

    public ItemBase(String name) {
        this(name, name);
    }

    public ItemBase(String name, String translationKey) {
        super();
        setRegistryName(name);
        setTranslationKey(translationKey);
        setCreativeTab(AFM_TAB);
        
        RegistryHandler.registerItem(this);
    }

    @Override
    public void registerModels() {
        ClientProxy.registerItemModel(this);
    }
}
