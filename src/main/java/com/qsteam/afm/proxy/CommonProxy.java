package com.qsteam.afm.proxy;

import com.qsteam.afm.handler.BlockHandler;
import com.qsteam.afm.handler.OreDictHandler;
import com.qsteam.afm.handler.RecipeHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        BlockHandler.register();
    }

    public void init(FMLInitializationEvent event) {
        OreDictHandler.registerOreDictAFMPlanks();
        RecipeHandler.removeRecipes();
    }

    public void postInit(FMLPostInitializationEvent event) {}


}
