package com.qsteam.afm.core;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.*;
import java.util.function.BooleanSupplier;

@IFMLLoadingPlugin.Name(AFMLoadingPlugin.PLUGIN_NAME)
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class AFMLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static final String PLUGIN_NAME = "AllFromModernCore";
    private static final Logger LOGGER = LogManager.getLogger(PLUGIN_NAME);

    public static final boolean IS_CLIENT = FMLLaunchHandler.side().isClient();

    private static final Map<String, BooleanSupplier> CLIENT_MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/mixins.afm.potion.json", () -> true
    );

    private static final Map<String, BooleanSupplier> SERVER_MIXIN_CONFIGS = ImmutableMap.of();

    private static final Map<String, BooleanSupplier> COMMON_MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/mixins.afm.pumpkin.json", () -> true,

            "mixins/mixins.afm.trident.json", () -> true
    );

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return AFMModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        try {

        } catch (Exception e) {
            LOGGER.fatal(e);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return AFMTransformer.class.getName();
    }

    @Override
    public List<String> getMixinConfigs() {
        List<String> configs = new ArrayList<>();
        if (IS_CLIENT) {
            configs.addAll(CLIENT_MIXIN_CONFIGS.keySet());
        } else {
            configs.addAll(SERVER_MIXIN_CONFIGS.keySet());
        }
        configs.addAll(COMMON_MIXIN_CONFIGS.keySet());
        return configs;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        BooleanSupplier sidedSupplier = IS_CLIENT ? CLIENT_MIXIN_CONFIGS.get(mixinConfig) : null;
        BooleanSupplier commonSupplier = COMMON_MIXIN_CONFIGS.get(mixinConfig);
        return sidedSupplier != null ? sidedSupplier.getAsBoolean() : commonSupplier == null || commonSupplier.getAsBoolean();
    }

}
