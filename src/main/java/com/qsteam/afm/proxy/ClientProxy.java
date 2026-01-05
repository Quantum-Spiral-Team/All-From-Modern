package com.qsteam.afm.proxy;

import com.qsteam.afm.api.block.IBlockMeta;
import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.block.base.BlockHalfSlabBase;
import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("ConstantConditions")
@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : RegistryHandler.ITEMS) {
            if (item instanceof IItemModel) ((IItemModel) item).registerModels();
            else registerItemModel(item);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item item) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            if (item.getHasSubtypes() && block instanceof IBlockMeta) {
                IProperty<?> prop = ((IBlockMeta) block).getVariantProperty();
                int i = 0;
                for (Object value : prop.getAllowedValues()) {
                    ModelLoader.setCustomModelResourceLocation(item, i++, new ModelResourceLocation(item.getRegistryName(), ((IBlockMeta) block).getModelVariant() + ((IStringSerializable) value).getName()));
                }
            } else if (item instanceof ItemSlab || block instanceof BlockHalfSlabBase) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "half=bottom"));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        } else {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(Block block) {
        registerItemModel(Item.getItemFromBlock(block));
    }
}