package com.qsteam.afm.util.math.shapes;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import com.qsteam.afm.util.EnumAxisRotation;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntPredicate;

public abstract class VoxelShape {
    protected final VoxelShapePart part;
    @Nullable
    private VoxelShape[] projectionCache;

    VoxelShape(VoxelShapePart part) {
        this.part = part;
    }

    public double getStart(Axis axis) {
        int i = this.part.getStart(axis);
        return i >= this.part.getSize(axis) ? Double.POSITIVE_INFINITY : this.getValueUnchecked(axis, i);
    }

    public double getEnd(Axis axis) {
        int i = this.part.getEnd(axis);
        return i <= 0 ? Double.NEGATIVE_INFINITY : this.getValueUnchecked(axis, i);
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.isEmpty()) {
            throw new UnsupportedOperationException("No bounds for empty shape.");
        } else {
            return new AxisAlignedBB(this.getStart(Axis.X), this.getStart(Axis.Y), this.getStart(Axis.Z),
                    this.getEnd(Axis.X), this.getEnd(Axis.Y), this.getEnd(Axis.Z));
        }
    }

    protected double getValueUnchecked(Axis axis, int index) {
        return this.getValues(axis).getDouble(index);
    }

    protected abstract DoubleList getValues(Axis axis);

    public boolean isEmpty() {
        return this.part.isEmpty();
    }

    public VoxelShape withOffset(double x, double y, double z) {
        return this.isEmpty() ? VoxelShapes.empty()
                : new VoxelShapeArray(this.part, new OffsetDoubleList(this.getValues(Axis.X), x),
                        new OffsetDoubleList(this.getValues(Axis.Y), y),
                        new OffsetDoubleList(this.getValues(Axis.Z), z));
    }

    public VoxelShape simplify() {
        VoxelShape[] shapes = new VoxelShape[] { VoxelShapes.empty() };
        this.forEachBox((x1, y1, z1, x2, y2, z2) -> shapes[0] = VoxelShapes.combine(shapes[0],
                VoxelShapes.create(x1, y1, z1, x2, y2, z2), IBooleanFunction.OR));
        return shapes[0];
    }

    @SideOnly(Side.CLIENT)
    public void forEachEdge(VoxelShapes.ILineConsumer consumer) {
        this.part.forEachEdge((x1, y1, z1, x2, y2, z2) -> consumer.consume(this.getValueUnchecked(Axis.X, x1),
                this.getValueUnchecked(Axis.Y, y1), this.getValueUnchecked(Axis.Z, z1),
                this.getValueUnchecked(Axis.X, x2), this.getValueUnchecked(Axis.Y, y2),
                this.getValueUnchecked(Axis.Z, z2)), true);
    }

    public void forEachBox(VoxelShapes.ILineConsumer consumer) {
        DoubleList xList = this.getValues(Axis.X);
        DoubleList yList = this.getValues(Axis.Y);
        DoubleList zList = this.getValues(Axis.Z);
        this.part
                .forEachBox(
                        (x1, y1, z1, x2, y2, z2) -> consumer.consume(xList.getDouble(x1), yList.getDouble(y1),
                                zList.getDouble(z1), xList.getDouble(x2), yList.getDouble(y2), zList.getDouble(z2)),
                        true);
    }

    public List<AxisAlignedBB> toBoundingBoxList() {
        List<AxisAlignedBB> list = Lists.newArrayList();
        this.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> list
                .add(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)));
        return list;
    }

    @SideOnly(Side.CLIENT)
    public double min(Axis axis, double coord1, double coord2) {
        Axis axis1 = EnumAxisRotation.FORWARD.rotate(axis);
        Axis axis2 = EnumAxisRotation.BACKWARD.rotate(axis);
        int i = this.getClosestIndex(axis1, coord1);
        int j = this.getClosestIndex(axis2, coord2);
        int k = this.part.firstFilled(axis, i, j);
        return k >= this.part.getSize(axis) ? Double.POSITIVE_INFINITY : this.getValueUnchecked(axis, k);
    }

    @SideOnly(Side.CLIENT)
    public double max(Axis axis, double coord1, double coord2) {
        Axis axis1 = EnumAxisRotation.FORWARD.rotate(axis);
        Axis axis2 = EnumAxisRotation.BACKWARD.rotate(axis);
        int i = this.getClosestIndex(axis1, coord1);
        int j = this.getClosestIndex(axis2, coord2);
        int k = this.part.lastFilled(axis, i, j);
        return k <= 0 ? Double.NEGATIVE_INFINITY : this.getValueUnchecked(axis, k);
    }

    protected int getClosestIndex(Axis axis, double coord) {
        return binarySearch(0, this.part.getSize(axis) + 1, index -> {
            if (index < 0) {
                return false;
            } else if (index > this.part.getSize(axis)) {
                return true;
            } else {
                return coord < this.getValueUnchecked(axis, index);
            }
        }) - 1;
    }

    protected int binarySearch(int min, int max, IntPredicate isTarget) {
        int i = min;
        int j = max - 1;
        while (i <= j) {
            int k = i + j >>> 1;
            if (isTarget.test(k)) {
                j = k - 1;
            } else {
                i = k + 1;
            }
        }
        return i;
    }

    protected boolean contains(double x, double y, double z) {
        return this.part.contains(this.getClosestIndex(Axis.X, x), this.getClosestIndex(Axis.Y, y),
                this.getClosestIndex(Axis.Z, z));
    }

    @Nullable
    public RayTraceResult rayTrace(Vec3d start, Vec3d end, BlockPos pos) {
        if (this.isEmpty()) {
            return null;
        } else {
            Vec3d vec3d = end.subtract(start);
            if (vec3d.lengthSquared() < 1.0E-7D) {
                return null;
            } else {
                Vec3d vec3d1 = start.add(vec3d.scale(0.001D));
                return this
                        .contains(vec3d1.x - (double) pos.getX(), vec3d1.y - (double) pos.getY(),
                                vec3d1.z - (double) pos.getZ())
                                        ? new RayTraceResult(vec3d1,
                                                EnumFacing.getFacingFromVector((float) vec3d.x, (float) vec3d.y,
                                                        (float) vec3d.z).getOpposite(),
                                                pos)
                                        : this.rayTraceBoxes(this.toBoundingBoxList(), start, end, pos);
            }
        }
    }

    @Nullable
    protected RayTraceResult rayTraceBoxes(List<AxisAlignedBB> boxes, Vec3d start, Vec3d end, BlockPos pos) {
        RayTraceResult closest = null;
        double closestDist = Double.POSITIVE_INFINITY;

        for (AxisAlignedBB box : boxes) {
            AxisAlignedBB boxWorld = box.offset(pos);
            RayTraceResult result = boxWorld.calculateIntercept(start, end);
            if (result != null) {
                double dist = start.squareDistanceTo(result.hitVec);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = result;
                }
            }
        }

        if (closest != null) {
            return new RayTraceResult(closest.hitVec, closest.sideHit, pos);
        }
        return null;
    }

    public VoxelShape project(EnumFacing facing) {
        if (!this.isEmpty() && this != VoxelShapes.fullCube()) {
            if (this.projectionCache != null) {
                VoxelShape shape = this.projectionCache[facing.ordinal()];
                if (shape != null) {
                    return shape;
                }
            } else {
                this.projectionCache = new VoxelShape[6];
            }

            VoxelShape shape = this.doProject(facing);
            this.projectionCache[facing.ordinal()] = shape;
            return shape;
        } else {
            return this;
        }
    }

    private VoxelShape doProject(EnumFacing facing) {
        Axis axis = facing.getAxis();
        AxisDirection axisDirection = facing.getAxisDirection();
        DoubleList doubleList = this.getValues(axis);
        if (doubleList.size() == 2 && DoubleMath.fuzzyEquals(doubleList.getDouble(0), 0.0D, 1.0E-7D)
                && DoubleMath.fuzzyEquals(doubleList.getDouble(1), 1.0D, 1.0E-7D)) {
            return this;
        } else {
            int i = this.getClosestIndex(axis, axisDirection == AxisDirection.POSITIVE ? 0.9999999D : 1.0E-7D);
            return new SplitVoxelShape(this, axis, i);
        }
    }

    public double getAllowedOffset(Axis axis, AxisAlignedBB box, double offset) {
        return this.getAllowedOffset(EnumAxisRotation.from(axis, Axis.X), box, offset);
    }

    protected double getAllowedOffset(EnumAxisRotation rotation, AxisAlignedBB box, double offset) {
        if (this.isEmpty()) {
            return offset;
        } else if (Math.abs(offset) < 1.0E-7D) {
            return 0.0D;
        } else {
            EnumAxisRotation reverseRotation = rotation.reverse();
            Axis axisX = reverseRotation.rotate(Axis.X);
            Axis axisY = reverseRotation.rotate(Axis.Y);
            Axis axisZ = reverseRotation.rotate(Axis.Z);

            double maxCoord = this.getMax(box, axisX);
            double minCoord = this.getMin(box, axisX);

            int i = this.getClosestIndex(axisX, minCoord + 1.0E-7D);
            int j = this.getClosestIndex(axisX, maxCoord - 1.0E-7D);
            int k = Math.max(0, this.getClosestIndex(axisY, this.getMin(box, axisY) + 1.0E-7D));
            int l = Math.min(this.part.getSize(axisY),
                    this.getClosestIndex(axisY, this.getMax(box, axisY) - 1.0E-7D) + 1);
            int m = Math.max(0, this.getClosestIndex(axisZ, this.getMin(box, axisZ) + 1.0E-7D));
            int n = Math.min(this.part.getSize(axisZ),
                    this.getClosestIndex(axisZ, this.getMax(box, axisZ) - 1.0E-7D) + 1);
            int o = this.part.getSize(axisX);

            if (offset > 0.0D) {
                for (int p = j + 1; p < o; ++p) {
                    for (int q = k; q < l; ++q) {
                        for (int r = m; r < n; ++r) {
                            if (this.part.containsWithRotation(reverseRotation, p, q, r)) {
                                double d = this.getValueUnchecked(axisX, p) - maxCoord;
                                if (d >= -1.0E-7D) {
                                    offset = Math.min(offset, d);
                                }
                                return offset;
                            }
                        }
                    }
                }
            } else if (offset < 0.0D) {
                for (int p = i - 1; p >= 0; --p) {
                    for (int q = k; q < l; ++q) {
                        for (int r = m; r < n; ++r) {
                            if (this.part.containsWithRotation(reverseRotation, p, q, r)) {
                                double d = this.getValueUnchecked(axisX, p + 1) - minCoord;
                                if (d <= 1.0E-7D) {
                                    offset = Math.max(offset, d);
                                }
                                return offset;
                            }
                        }
                    }
                }
            }

            return offset;
        }
    }

    private double getMin(AxisAlignedBB box, Axis axis) {
        switch (axis) {
            case X:
                return box.minX;
            case Y:
                return box.minY;
            case Z:
                return box.minZ;
            default:
                return 0.0D;
        }
    }

    private double getMax(AxisAlignedBB box, Axis axis) {
        switch (axis) {
            case X:
                return box.maxX;
            case Y:
                return box.maxY;
            case Z:
                return box.maxZ;
            default:
                return 0.0D;
        }
    }

    public String toString() {
        return this.isEmpty() ? "EMPTY" : "VoxelShape[" + this.getBoundingBox() + "]";
    }
}
