package com.qsteam.afm.core;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BooleanSupplier;

//Personal credits for EnderDeveloper and his EnderModpackTweaks
public class AFMLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final boolean isClient = FMLLaunchHandler.side().isClient();

    private static final Map<String, BooleanSupplier> SERVER_MIXIN_CONFIGS = Collections.emptyMap();

    private static final Map<String, BooleanSupplier> COMMON_MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/mixins.afm.golems.json", () -> true
    );

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
//        try {
//            Field f_transformerExceptions = LaunchClassLoader.class.getDeclaredField("transformerExceptions");
//            f_transformerExceptions.setAccessible(true);
//            Set<String> transformerExceptions = (Set<String>) f_transformerExceptions.get(Launch.classLoader);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String getAccessTransformerClass() {
        return "com.qsteam.afm.core.AFMTransformer";
    }


    @Override
    public List<String> getMixinConfigs() {
//        List<String> configs = new ArrayList<>();
//        if (isClient) {
//            configs.addAll(CLIENT_MIXIN_CONFIGS.keySet());
//        } else {
//            configs.addAll(SERVER_MIXIN_CONFIGS.keySet());
//        }
//        configs.addAll(COMMON_MIXIN_CONFIGS.keySet());
        return new ArrayList<>(COMMON_MIXIN_CONFIGS.keySet());
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
//        BooleanSupplier sidedSupplier = isClient ? CLIENT_MIXIN_CONFIGS.get(mixinConfig) : null;
        BooleanSupplier commonSupplier = COMMON_MIXIN_CONFIGS.get(mixinConfig);
//        return sidedSupplier != null ? sidedSupplier.getAsBoolean() : commonSupplier == null || commonSupplier.getAsBoolean();
        return commonSupplier != null && commonSupplier.getAsBoolean();
    }
}
