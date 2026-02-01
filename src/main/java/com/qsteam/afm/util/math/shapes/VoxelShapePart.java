package com.qsteam.afm.util.math.shapes;

import com.qsteam.afm.util.EnumAxisRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class VoxelShapePart {
    private static final Axis[] AXIS_VALUES = Axis.values();
    protected final int xSize;
    protected final int ySize;
    protected final int zSize;

    protected VoxelShapePart(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public boolean containsWithRotation(EnumAxisRotation rotation, int x, int y, int z) {
        return this.contains(
                rotation.getCoordinate(x, y, z, Axis.X),
                rotation.getCoordinate(x, y, z, Axis.Y),
                rotation.getCoordinate(x, y, z, Axis.Z));
    }

    public boolean contains(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0) {
            return x < this.xSize && y < this.ySize && z < this.zSize && this.isFilled(x, y, z);
        } else {
            return false;
        }
    }

    public boolean isFilledWithRotation(EnumAxisRotation rotation, int x, int y, int z) {
        return this.isFilled(
                rotation.getCoordinate(x, y, z, Axis.X),
                rotation.getCoordinate(x, y, z, Axis.Y),
                rotation.getCoordinate(x, y, z, Axis.Z));
    }

    public abstract boolean isFilled(int x, int y, int z);

    public abstract void setFilled(int x, int y, int z, boolean updateBounds, boolean filled);

    public boolean isEmpty() {
        for (Axis axis : AXIS_VALUES) {
            if (this.getStart(axis) >= this.getEnd(axis)) {
                return true;
            }
        }
        return false;
    }

    public abstract int getStart(Axis axis);

    public abstract int getEnd(Axis axis);

    @SideOnly(Side.CLIENT)
    public int firstFilled(Axis axis, int u, int v) {
        int size = this.getSize(axis);
        if (u >= 0 && v >= 0) {
            Axis uAxis = EnumAxisRotation.FORWARD.rotate(axis);
            Axis vAxis = EnumAxisRotation.BACKWARD.rotate(axis);
            if (u < this.getSize(uAxis) && v < this.getSize(vAxis)) {
                EnumAxisRotation rotation = EnumAxisRotation.from(Axis.X, axis);

                for (int i = 0; i < size; ++i) {
                    if (this.isFilledWithRotation(rotation, i, u, v)) {
                        return i;
                    }
                }

                return size;
            } else {
                return size;
            }
        } else {
            return size;
        }
    }

    @SideOnly(Side.CLIENT)
    public int lastFilled(Axis axis, int u, int v) {
        if (u >= 0 && v >= 0) {
            Axis uAxis = EnumAxisRotation.FORWARD.rotate(axis);
            Axis vAxis = EnumAxisRotation.BACKWARD.rotate(axis);
            if (u < this.getSize(uAxis) && v < this.getSize(vAxis)) {
                int size = this.getSize(axis);
                EnumAxisRotation rotation = EnumAxisRotation.from(Axis.X, axis);

                for (int i = size - 1; i >= 0; --i) {
                    if (this.isFilledWithRotation(rotation, i, u, v)) {
                        return i + 1;
                    }
                }

                return 0;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getSize(Axis axis) {
        if (axis == Axis.X)
            return this.xSize;
        if (axis == Axis.Y)
            return this.ySize;
        return this.zSize;
    }

    public int getXSize() {
        return this.getSize(Axis.X);
    }

    public int getYSize() {
        return this.getSize(Axis.Y);
    }

    public int getZSize() {
        return this.getSize(Axis.Z);
    }

    @SideOnly(Side.CLIENT)
    public void forEachEdge(ILineConsumer consumer, boolean combine) {
        this.forEachEdgeOnAxis(consumer, EnumAxisRotation.NONE, combine);
        this.forEachEdgeOnAxis(consumer, EnumAxisRotation.FORWARD, combine);
        this.forEachEdgeOnAxis(consumer, EnumAxisRotation.BACKWARD, combine);
    }

    @SideOnly(Side.CLIENT)
    private void forEachEdgeOnAxis(ILineConsumer consumer, EnumAxisRotation rotation, boolean combine) {
        EnumAxisRotation revRotation = rotation.reverse();
        int sizeX = this.getSize(revRotation.rotate(Axis.X));
        int sizeY = this.getSize(revRotation.rotate(Axis.Y));
        int sizeZ = this.getSize(revRotation.rotate(Axis.Z));

        for (int x = 0; x <= sizeX; ++x) {
            for (int y = 0; y <= sizeY; ++y) {
                int edgeStart = -1;

                for (int z = 0; z <= sizeZ; ++z) {
                    int filledCount = 0;
                    int xorSum = 0;

                    for (int dx = 0; dx <= 1; ++dx) {
                        for (int dy = 0; dy <= 1; ++dy) {
                            if (this.containsWithRotation(revRotation, x + dx - 1, y + dy - 1, z)) {
                                ++filledCount;
                                xorSum ^= dx ^ dy;
                            }
                        }
                    }

                    if (filledCount == 1 || filledCount == 3 || filledCount == 2 && (xorSum & 1) == 0) {
                        if (combine) {
                            if (edgeStart == -1) {
                                edgeStart = z;
                            }
                        } else {
                            consumer.consume(
                                    revRotation.getCoordinate(x, y, z, Axis.X),
                                    revRotation.getCoordinate(x, y, z, Axis.Y),
                                    revRotation.getCoordinate(x, y, z, Axis.Z),
                                    revRotation.getCoordinate(x, y, z + 1, Axis.X),
                                    revRotation.getCoordinate(x, y, z + 1, Axis.Y),
                                    revRotation.getCoordinate(x, y, z + 1, Axis.Z));
                        }
                    } else if (edgeStart != -1) {
                        consumer.consume(
                                revRotation.getCoordinate(x, y, edgeStart, Axis.X),
                                revRotation.getCoordinate(x, y, edgeStart, Axis.Y),
                                revRotation.getCoordinate(x, y, edgeStart, Axis.Z),
                                revRotation.getCoordinate(x, y, z, Axis.X),
                                revRotation.getCoordinate(x, y, z, Axis.Y),
                                revRotation.getCoordinate(x, y, z, Axis.Z));
                        edgeStart = -1;
                    }
                }
            }
        }
    }

    protected boolean isZAxisLineFull(int zStart, int zEnd, int x, int y) {
        for (int z = zStart; z < zEnd; ++z) {
            if (!this.contains(x, y, z)) {
                return false;
            }
        }
        return true;
    }

    protected void setZAxisLine(int zStart, int zEnd, int x, int y, boolean filled) {
        for (int z = zStart; z < zEnd; ++z) {
            this.setFilled(x, y, z, false, filled);
        }
    }

    protected boolean isXZRectangleFull(int minX, int maxX, int zStart, int zEnd, int y) {
        for (int x = minX; x < maxX; ++x) {
            if (!this.isZAxisLineFull(zStart, zEnd, x, y)) {
                return false;
            }
        }
        return true;
    }

    public void forEachBox(ILineConsumer consumer, boolean combine) {
        VoxelShapePart workingCopy = new BitSetVoxelShapePart(this);

        for (int x = 0; x <= this.xSize; ++x) {
            for (int y = 0; y <= this.ySize; ++y) {
                int zStart = -1;

                for (int z = 0; z <= this.zSize; ++z) {
                    if (workingCopy.contains(x, y, z)) {
                        if (combine) {
                            if (zStart == -1) {
                                zStart = z;
                            }
                        } else {
                            consumer.consume(x, y, z, x + 1, y + 1, z + 1);
                        }
                    } else if (zStart != -1) {
                        int minX = x;
                        int maxX = x;
                        int minY = y;
                        int maxY = y;
                        workingCopy.setZAxisLine(zStart, z, x, y, false);

                        while (workingCopy.isZAxisLineFull(zStart, z, minX - 1, minY)) {
                            workingCopy.setZAxisLine(zStart, z, minX - 1, minY, false);
                            --minX;
                        }

                        while (workingCopy.isZAxisLineFull(zStart, z, maxX + 1, minY)) {
                            workingCopy.setZAxisLine(zStart, z, maxX + 1, minY, false);
                            ++maxX;
                        }

                        while (workingCopy.isXZRectangleFull(minX, maxX + 1, zStart, z, minY - 1)) {
                            for (int ix = minX; ix <= maxX; ++ix) {
                                workingCopy.setZAxisLine(zStart, z, ix, minY - 1, false);
                            }
                            --minY;
                        }

                        while (workingCopy.isXZRectangleFull(minX, maxX + 1, zStart, z, maxY + 1)) {
                            for (int ix = minX; ix <= maxX; ++ix) {
                                workingCopy.setZAxisLine(zStart, z, ix, maxY + 1, false);
                            }
                            ++maxY;
                        }

                        consumer.consume(minX, minY, zStart, maxX + 1, maxY + 1, z);
                        zStart = -1;
                    }
                }
            }
        }
    }

    public void forEachFace(IFaceConsumer consumer) {
        this.forEachFaceOnAxis(consumer, EnumAxisRotation.NONE);
        this.forEachFaceOnAxis(consumer, EnumAxisRotation.FORWARD);
        this.forEachFaceOnAxis(consumer, EnumAxisRotation.BACKWARD);
    }

    private void forEachFaceOnAxis(IFaceConsumer consumer, EnumAxisRotation rotation) {
        EnumAxisRotation revRotation = rotation.reverse();
        Axis depthAxis = revRotation.rotate(Axis.Z);
        int sizeX = this.getSize(revRotation.rotate(Axis.X));
        int sizeY = this.getSize(revRotation.rotate(Axis.Y));
        int sizeZ = this.getSize(depthAxis);
        EnumFacing negativeFace = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, depthAxis);
        EnumFacing positiveFace = EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, depthAxis);

        for (int x = 0; x < sizeX; ++x) {
            for (int y = 0; y < sizeY; ++y) {
                boolean prevFilled = false;

                for (int z = 0; z <= sizeZ; ++z) {
                    boolean currFilled = z != sizeZ && this.isFilledWithRotation(revRotation, x, y, z);
                    if (!prevFilled && currFilled) {
                        consumer.consume(
                                negativeFace,
                                revRotation.getCoordinate(x, y, z, Axis.X),
                                revRotation.getCoordinate(x, y, z, Axis.Y),
                                revRotation.getCoordinate(x, y, z, Axis.Z));
                    }

                    if (prevFilled && !currFilled) {
                        consumer.consume(
                                positiveFace,
                                revRotation.getCoordinate(x, y, z - 1, Axis.X),
                                revRotation.getCoordinate(x, y, z - 1, Axis.Y),
                                revRotation.getCoordinate(x, y, z - 1, Axis.Z));
                    }

                    prevFilled = currFilled;
                }
            }
        }
    }

    public interface IFaceConsumer {
        void consume(EnumFacing side, int x, int y, int z);
    }

    public interface ILineConsumer {
        void consume(int minX, int minY, int minZ, int maxX, int maxY, int maxZ);
    }
}
