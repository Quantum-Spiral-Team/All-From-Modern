package com.qsteam.afm.util.math.shapes;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IForgeSelectionContext {
    @Nullable
    default Entity getEntity() {
        return null;
    }
}
