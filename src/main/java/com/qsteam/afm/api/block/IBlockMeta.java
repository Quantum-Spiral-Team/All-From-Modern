package com.qsteam.afm.api.block;

import net.minecraft.block.properties.PropertyEnum;

public interface IBlockMeta {

    PropertyEnum<?> getVariantProperty();

    default String getModelVariant() {
        return "variant=";
    }

}
