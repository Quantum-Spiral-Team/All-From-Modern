package com.qsteam.afm;

import com.qsteam.afm.block.AFMBlocks;
import com.qsteam.afm.core.AFMTransformer;
import com.qsteam.afm.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = Tags.DEPENDENCIES)
public class AllFromModern {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public static final CreativeTabs AFM_TAB = new CreativeTabs("afm") {
        @Override
        @SideOnly(Side.CLIENT)
        public @NotNull ItemStack createIcon() {
            return new ItemStack(AFMBlocks.BLUE_ICE);
        }
    };

    @SidedProxy(
            clientSide = "com.qsteam.afm.proxy.ClientProxy",
            serverSide = "com.qsteam.afm.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @Instance
    public static AllFromModern instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
