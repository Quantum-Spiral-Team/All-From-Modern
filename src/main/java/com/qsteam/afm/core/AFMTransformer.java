package com.qsteam.afm.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;

//Personal credits for EnderDeveloper and his EnderModpackTweaks
@SuppressWarnings("unused") // Used by EMTLoadingPlugin
public class AFMTransformer implements IClassTransformer, Opcodes {

    private static final String HOOKS = "com/qsteam/afm/core/AFMTransformer$Hooks";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return basicClass;
    }

    @SuppressWarnings("unused")
    public static class Hooks {

    }
}
