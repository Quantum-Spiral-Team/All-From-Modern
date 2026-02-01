package com.qsteam.afm.util.math.shapes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class EntitySelectionContext implements ISelectionContext {

    protected static final ISelectionContext DUMMY;
    private final boolean sneaking;
    private final double entityBottomY;
    private final Item item;
    @Nullable
    private final Entity entity;

    protected EntitySelectionContext(boolean sneaking, double entityBottomY, Item item) {
        this(null, sneaking, entityBottomY, item);
    }

    protected EntitySelectionContext(@Nullable Entity entity, boolean sneaking, double entityBottomY, Item item) {
        this.entity = entity;
        this.sneaking = sneaking;
        this.entityBottomY = entityBottomY;
        this.item = item;
    }

    protected EntitySelectionContext(Entity entity) {
        this(
                entity,
                entity.isSneaking(),
                entity.getEntityBoundingBox().minY,
                entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHeldItemMainhand().getItem()
                        : Items.AIR);
    }

    @Override
    public boolean hasItem(Item item) {
        return this.item == item;
    }

    @Override
    public boolean isSneaking() {
        return this.sneaking;
    }

    @Override
    public boolean isAbove(VoxelShape shape, BlockPos pos, boolean bool) {
        return this.entityBottomY > (double) pos.getY() + shape.getEnd(EnumFacing.Axis.Y) - 1.0E-5;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return this.entity;
    }

    static {
        DUMMY = new EntitySelectionContext(false, -Double.MAX_VALUE, Items.AIR) {
            @Override
            public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) {
                return defaultValue;
            }
        };
    }

}
