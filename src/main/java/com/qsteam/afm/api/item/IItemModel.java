package com.qsteam.afm.api.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemModel {
    @SideOnly(Side.CLIENT)
    void registerModels();
}
