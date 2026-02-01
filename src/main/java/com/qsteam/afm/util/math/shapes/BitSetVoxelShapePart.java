package com.qsteam.afm.util.math.shapes;

import com.qsteam.afm.util.EnumAxisRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

import java.util.BitSet;

public final class BitSetVoxelShapePart extends VoxelShapePart {
    private final BitSet bitSet;
    private int startX;
    private int startY;
    private int startZ;
    private int endX;
    private int endY;
    private int endZ;

    public BitSetVoxelShapePart(int xSize, int ySize, int zSize) {
        this(xSize, ySize, zSize, xSize, ySize, zSize, 0, 0, 0);
    }

    public BitSetVoxelShapePart(int xSize, int ySize, int zSize, int startX, int startY, int startZ, int endX, int endY,
            int endZ) {
        super(xSize, ySize, zSize);
        this.bitSet = new BitSet(xSize * ySize * zSize);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    public BitSetVoxelShapePart(VoxelShapePart other) {
        super(other.xSize, other.ySize, other.zSize);
        if (other instanceof BitSetVoxelShapePart) {
            this.bitSet = (BitSet) ((BitSetVoxelShapePart) other).bitSet.clone();
        } else {
            this.bitSet = new BitSet(this.xSize * this.ySize * this.zSize);

            for (int x = 0; x < this.xSize; ++x) {
                for (int y = 0; y < this.ySize; ++y) {
                    for (int z = 0; z < this.zSize; ++z) {
                        if (other.isFilled(x, y, z)) {
                            this.bitSet.set(this.getIndex(x, y, z));
                        }
                    }
                }
            }
        }

        this.startX = other.getStart(Axis.X);
        this.startY = other.getStart(Axis.Y);
        this.startZ = other.getStart(Axis.Z);
        this.endX = other.getEnd(Axis.X);
        this.endY = other.getEnd(Axis.Y);
        this.endZ = other.getEnd(Axis.Z);
    }

    public int getIndex(int x, int y, int z) {
        return (x * this.ySize + y) * this.zSize + z;
    }

    @Override
    public boolean isFilled(int x, int y, int z) {
        return this.bitSet.get(this.getIndex(x, y, z));
    }

    @Override
    public void setFilled(int x, int y, int z, boolean updateBounds, boolean filled) {
        this.bitSet.set(this.getIndex(x, y, z), filled);
        if (updateBounds && filled) {
            this.startX = Math.min(this.startX, x);
            this.startY = Math.min(this.startY, y);
            this.startZ = Math.min(this.startZ, z);
            this.endX = Math.max(this.endX, x + 1);
            this.endY = Math.max(this.endY, y + 1);
            this.endZ = Math.max(this.endZ, z + 1);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.bitSet.isEmpty();
    }

    @Override
    public int getStart(Axis axis) {
        switch (axis) {
            case X:
                return this.startX;
            case Y:
                return this.startY;
            case Z:
                return this.startZ;
            default:
                return 0;
        }
    }

    @Override
    public int getEnd(Axis axis) {
        switch (axis) {
            case X:
                return this.endX;
            case Y:
                return this.endY;
            case Z:
                return this.endZ;
            default:
                return 0;
        }
    }

    @Override
    protected boolean isZAxisLineFull(int zStart, int zEnd, int x, int y) {
        if (x >= 0 && y >= 0 && zStart >= 0 && x < this.xSize && y < this.ySize && zEnd <= this.zSize) {
                return this.bitSet.nextClearBit(this.getIndex(x, y, zStart)) >= this.getIndex(x, y, zEnd);
        } else return false;
    }

    @Override
    protected void setZAxisLine(int zStart, int zEnd, int x, int y, boolean filled) {
        this.bitSet.set(this.getIndex(x, y, zStart), this.getIndex(x, y, zEnd), filled);
    }

    static BitSetVoxelShapePart join(VoxelShapePart part1, VoxelShapePart part2, IDoubleListMerger mergerX,
            IDoubleListMerger mergerY, IDoubleListMerger mergerZ, IBooleanFunction function) {
        BitSetVoxelShapePart result = new BitSetVoxelShapePart(mergerX.getList().size() - 1,
                mergerY.getList().size() - 1, mergerZ.getList().size() - 1);
        int[] bounds = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE };

        mergerX.forMergedIndexes((x1, x2, x3) -> {
            boolean[] xFlag = new boolean[] { false };
            boolean resultY = mergerY.forMergedIndexes((y1, y2, y3) -> {
                boolean[] yFlag = new boolean[] { false };
                boolean resultZ = mergerZ.forMergedIndexes((z1, z2, z3) -> {
                    boolean val = function.apply(part1.contains(x1, y1, z1), part2.contains(x2, y2, z2));
                    if (val) {
                        result.bitSet.set(result.getIndex(x3, y3, z3));
                        bounds[2] = Math.min(bounds[2], z3);
                        bounds[5] = Math.max(bounds[5], z3);
                        yFlag[0] = true;
                    }

                    return true;
                });
                if (yFlag[0]) {
                    bounds[1] = Math.min(bounds[1], y3);
                    bounds[4] = Math.max(bounds[4], y3);
                    xFlag[0] = true;
                }

                return resultZ;
            });
            if (xFlag[0]) {
                bounds[0] = Math.min(bounds[0], x3);
                bounds[3] = Math.max(bounds[3], x3);
            }

            return resultY;
        });

        result.startX = bounds[0];
        result.startY = bounds[1];
        result.startZ = bounds[2];
        result.endX = bounds[3] + 1;
        result.endY = bounds[4] + 1;
        result.endZ = bounds[5] + 1;

        return result;
    }
}
