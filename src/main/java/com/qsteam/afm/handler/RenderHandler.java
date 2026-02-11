package com.qsteam.afm.handler;

import com.qsteam.afm.client.render.RenderTrident;
import com.qsteam.afm.entity.projectile.EntityTrident;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHandler {

    public static void registerEntityRenders() {
        registerEntityRender(EntityTrident.class, RenderTrident::new);
    }

    private static <T extends Entity> void registerEntityRender(Class<T> entityClass, IRenderFactory<? super T> renderFactory) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
    }

}
