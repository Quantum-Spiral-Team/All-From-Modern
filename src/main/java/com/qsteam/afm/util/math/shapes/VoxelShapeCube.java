package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

public final class VoxelShapeCube extends VoxelShape {

    public VoxelShapeCube(VoxelShapePart part) {
        super(part);
    }

    @Override
    protected DoubleList getValues(Axis axis) {
        return new DoubleRangeList(this.part.getSize(axis));
    }

    @Override
    protected int getClosestIndex(Axis axis, double coord) {
        int size = this.part.getSize(axis);
        return MathHelper.clamp(MathHelper.floor(coord * size), -1, size);
    }

}
