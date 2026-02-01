package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.EnumFacing.Axis;

import java.util.Arrays;

public final class VoxelShapeArray extends VoxelShape {
    private final DoubleList xPoints;
    private final DoubleList yPoints;
    private final DoubleList zPoints;

    public VoxelShapeArray(VoxelShapePart part, double[] xPoints, double[] yPoints, double[] zPoints) {
        this(part, DoubleArrayList.wrap(Arrays.copyOf(xPoints, part.getXSize() + 1)),
                DoubleArrayList.wrap(Arrays.copyOf(yPoints, part.getYSize() + 1)),
                DoubleArrayList.wrap(Arrays.copyOf(zPoints, part.getZSize() + 1)));
    }

    VoxelShapeArray(VoxelShapePart part, DoubleList xPoints, DoubleList yPoints, DoubleList zPoints) {
        super(part);
        int i = part.getXSize() + 1;
        int j = part.getYSize() + 1;
        int k = part.getZSize() + 1;
        if (i == xPoints.size() && j == yPoints.size() && k == zPoints.size()) {
            this.xPoints = xPoints;
            this.yPoints = yPoints;
            this.zPoints = zPoints;
        } else {
            throw new IllegalArgumentException(
                    "Lengths of point arrays must be consistent with the size of the VoxelShape.");
        }
    }

    @Override
    protected DoubleList getValues(Axis axis) {
        switch (axis) {
            case X:
                return this.xPoints;
            case Y:
                return this.yPoints;
            case Z:
                return this.zPoints;
            default:
                throw new IllegalArgumentException();
        }
    }
}
