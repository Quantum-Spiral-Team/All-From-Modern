package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.EnumFacing.Axis;

import static com.qsteam.afm.util.EnumFacingHelper.AxisHelper.getCoordinate;

public class SplitVoxelShape extends VoxelShape {

    private final VoxelShape shape;
    private final Axis axis;
    private static final DoubleList DOUBLE_LIST = new DoubleRangeList(1);

    public SplitVoxelShape(VoxelShape shape, Axis axis, int offset) {
        super(makeShapePart(shape.part, axis, offset));
        this.shape = shape;
        this.axis = axis;
    }

    private static VoxelShapePart makeShapePart(VoxelShapePart part, Axis axis, int offset) {
        return new PartSplitVoxelShape(part, getCoordinate(axis, offset, 0, 0), getCoordinate(axis, 0, offset, 0), getCoordinate(axis, 0, 0, offset), getCoordinate(axis, offset + 1, part.xSize, part.xSize), getCoordinate(axis, part.ySize, offset + 1, part.ySize), getCoordinate(axis, part.zSize, part.zSize, offset + 1));
    }

    @Override
    protected DoubleList getValues(Axis axis) {
        return axis == this.axis ? DOUBLE_LIST : this.shape.getValues(axis);
    }

}
