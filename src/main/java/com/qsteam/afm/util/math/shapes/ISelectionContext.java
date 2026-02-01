package com.qsteam.afm.util.math.shapes;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public interface ISelectionContext extends IForgeSelectionContext {
    static ISelectionContext dummy() {
        return EntitySelectionContext.DUMMY;
    }

    static ISelectionContext forEntity(Entity entity) {
        return new EntitySelectionContext(entity);
    }

    boolean isSneaking();

    boolean isAbove(VoxelShape shape, BlockPos pos, boolean bool);

    boolean hasItem(Item item);
}
