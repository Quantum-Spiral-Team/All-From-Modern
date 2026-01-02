package com.qsteam.afm.handler;

import com.qsteam.afm.item.ItemBase;
import com.qsteam.afm.item.ItemTurtleShell;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class ItemHandler {

    private static final List<Item> ITEMS;
//    private static final Map<Item, String[]> META_ITEMS;

    static {
        List<Item> items = new ArrayList<>();

        items.add(new ItemBase("turtle_scute"));
        items.add(new ItemTurtleShell());
        items.add(new ItemBase("phantom_membrane"));

        ITEMS = Collections.unmodifiableList(items);


//        Map<Item, String[]> metaBlocks = new LinkedHashMap<>();
//
//
//
//        META_ITEMS = Collections.unmodifiableMap(metaBlocks);
    }

    public static void register() {
        ITEMS.forEach(ForgeRegistries.ITEMS::register);
//        META_ITEMS.forEach((item, types) ->
//            ForgeRegistries.ITEMS.register(item)
//        );
    }

    @SideOnly(Side.CLIENT)
    public static void render() {
        ITEMS.forEach(item ->
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"))
        );
//        META_ITEMS.forEach((item, types) -> {
//            for (int i = 0; i < types.length; i++) {
//                ModelLoader.setCustomModelResourceLocation(item, i,
//                        new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "type=" + types[i]));
//            }
//        });
    }
}