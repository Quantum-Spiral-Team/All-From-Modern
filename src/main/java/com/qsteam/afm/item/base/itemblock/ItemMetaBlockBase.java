package com.qsteam.afm.item.base.itemblock;

import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.util.IStringSerializable;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class ItemMetaBlockBase extends ItemMultiTexture implements IItemModel {

    public <T extends Enum<T> & IStringSerializable> ItemMetaBlockBase(Block block, Class<T> enumClass) {
        super(block, block, stack -> enumClass.getEnumConstants()[stack.getMetadata()].getName());
        setHasSubtypes(true);
        setCreativeTab(AFM_TAB);
    }

    public <T extends Enum<T> & IStringSerializable> ItemMetaBlockBase(Block block, PropertyEnum<T> property) {
        super(block, block, stack -> ((T) property.getAllowedValues().toArray()[stack.getMetadata()]).getName());
        setHasSubtypes(true);
        setCreativeTab(AFM_TAB);
    }

    @Override
    public void registerModels() {
        ClientProxy.registerItemModel(this);
    }

}