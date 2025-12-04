package com.qsteam.afm.proxy;

import com.qsteam.afm.handler.BlockHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        BlockHandler.render();
    }

}
