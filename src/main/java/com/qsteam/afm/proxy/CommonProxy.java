package com.qsteam.afm.proxy;

import com.qsteam.afm.handler.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        BlockHandler.register();
        ItemHandler.register();
    }

    public void init(FMLInitializationEvent event) {
        OreDictHandler.registerOreDictAFMPlanks();
        GameRegistry.registerFuelHandler(new FuelHandler());
        FlammableBlockHandler.register();
    }

    public void postInit(FMLPostInitializationEvent event) {}


}
