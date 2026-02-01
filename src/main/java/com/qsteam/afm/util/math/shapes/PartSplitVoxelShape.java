package com.qsteam.afm.util.math.shapes;

import net.minecraft.util.EnumFacing.Axis;

import static com.qsteam.afm.util.EnumFacingHelper.AxisHelper.getCoordinate;

public final class PartSplitVoxelShape extends VoxelShapePart {

    private final VoxelShapePart part;
    private final int startX;
    private final int startY;
    private final int startZ;
    private final int endX;
    private final int endY;
    private final int endZ;

    public PartSplitVoxelShape(VoxelShapePart part, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        super(endX - startX, endY - startY, endZ - startZ);
        this.part = part;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    @Override
    public boolean isFilled(int x, int y, int z) {
        return this.part.isFilled(this.startX + x, this.startY + y, this.startZ + z);
    }

    @Override
    public void setFilled(int x, int y, int z, boolean updateBounds, boolean filled) {
        this.part.setFilled(this.startX + x, this.startY + y, this.startZ + z, updateBounds, filled);
    }

    @Override
    public int getStart(Axis axis) {
        return Math.max(0, this.part.getStart(axis) - getCoordinate(axis, this.startX, this.startY, this.startZ));
    }

    @Override
    public int getEnd(Axis axis) {
        return Math.min(getCoordinate(axis, this.endX, this.endY, this.endZ),
                this.part.getEnd(axis) - getCoordinate(axis, this.startX, this.startY, this.startZ));
    }

}
