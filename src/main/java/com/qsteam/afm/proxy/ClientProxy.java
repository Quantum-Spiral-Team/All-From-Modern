package com.qsteam.afm.proxy;

import com.qsteam.afm.handler.BlockHandler;
import com.qsteam.afm.handler.ItemHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        BlockHandler.render();
        ItemHandler.render();
    }

}
