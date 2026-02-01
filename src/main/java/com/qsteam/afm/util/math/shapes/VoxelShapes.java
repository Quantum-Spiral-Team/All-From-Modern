package com.qsteam.afm.util.math.shapes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.qsteam.afm.util.EnumAxisRotation;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public final class VoxelShapes {
    private static final VoxelShape FULL_CUBE;
    static {
        VoxelShapePart part = new BitSetVoxelShapePart(1, 1, 1);
        part.setFilled(0, 0, 0, true, true);
        FULL_CUBE = new VoxelShapeCube(part);
    }

    public static final VoxelShape INFINITY = create(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    private static final VoxelShape EMPTY = new VoxelShapeArray(new BitSetVoxelShapePart(0, 0, 0),
            new DoubleArrayList(new double[] { 0.0D }), new DoubleArrayList(new double[] { 0.0D }),
            new DoubleArrayList(new double[] { 0.0D }));

    public static VoxelShape empty() {
        return EMPTY;
    }

    public static VoxelShape fullCube() {
        return FULL_CUBE;
    }

    public static VoxelShape create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return create(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public static VoxelShape create(AxisAlignedBB aabb) {
        int xBits = getPrecisionBits(aabb.minX, aabb.maxX);
        int yBits = getPrecisionBits(aabb.minY, aabb.maxY);
        int zBits = getPrecisionBits(aabb.minZ, aabb.maxZ);
        if (xBits >= 0 && yBits >= 0 && zBits >= 0) {
            if (xBits == 0 && yBits == 0 && zBits == 0) {
                return aabb.contains(new Vec3d(0.5D, 0.5D, 0.5D)) ? fullCube() : empty();
            } else {
                int xScale = 1 << xBits;
                int yScale = 1 << yBits;
                int zScale = 1 << zBits;
                int minX = (int) Math.round(aabb.minX * xScale);
                int maxX = (int) Math.round(aabb.maxX * xScale);
                int minY = (int) Math.round(aabb.minY * yScale);
                int maxY = (int) Math.round(aabb.maxY * yScale);
                int minZ = (int) Math.round(aabb.minZ * zScale);
                int maxZ = (int) Math.round(aabb.maxZ * zScale);
                BitSetVoxelShapePart part = new BitSetVoxelShapePart(xScale, yScale, zScale, minX, minY, minZ, maxX,
                        maxY, maxZ);

                for (int x = minX; x < maxX; ++x) {
                    for (int y = minY; y < maxY; ++y) {
                        for (int z = minZ; z < maxZ; ++z) {
                            part.setFilled(x, y, z, false, true);
                        }
                    }
                }

                return new VoxelShapeCube(part);
            }
        } else {
            return new VoxelShapeArray(FULL_CUBE.part, new double[] { aabb.minX, aabb.maxX },
                    new double[] { aabb.minY, aabb.maxY }, new double[] { aabb.minZ, aabb.maxZ });
        }
    }

    private static int getPrecisionBits(double min, double max) {
        if (min >= -1.0E-7D && max <= 1.0000001D) {
            for (int i = 0; i <= 3; ++i) {
                double d0 = min * (1 << i);
                double d1 = max * (1 << i);
                boolean flag = Math.abs(d0 - Math.floor(d0)) < 1.0E-7D;
                boolean flag1 = Math.abs(d1 - Math.floor(d1)) < 1.0E-7D;
                if (flag && flag1) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static long lcm(int a, int b) {
        return (long) a * (b / IntMath.gcd(a, b));
    }

    public static VoxelShape or(VoxelShape shape1, VoxelShape shape2) {
        return combineAndSimplify(shape1, shape2, IBooleanFunction.OR);
    }

    public static VoxelShape or(VoxelShape shape, VoxelShape... shapes) {
        return Arrays.stream(shapes).reduce(shape, VoxelShapes::or);
    }

    public static VoxelShape combineAndSimplify(VoxelShape shape1, VoxelShape shape2, IBooleanFunction function) {
        return combine(shape1, shape2, function).simplify();
    }

    public static VoxelShape combine(VoxelShape shape1, VoxelShape shape2, IBooleanFunction function) {
        if (function.apply(false, false)) {
            throw new IllegalArgumentException();
        }
        if (shape1 == shape2) {
            return function.apply(true, true) ? shape1 : empty();
        }
        boolean flag = function.apply(true, false);
        boolean flag1 = function.apply(false, true);
        if (shape1.isEmpty()) {
            return flag1 ? shape2 : empty();
        }
        if (shape2.isEmpty()) {
            return flag ? shape1 : empty();
        }
        IDoubleListMerger mergerX = makeListMerger(1, shape1.getValues(Axis.X), shape2.getValues(Axis.X), flag,
                flag1);
        IDoubleListMerger mergerY = makeListMerger(mergerX.getList().size() - 1, shape1.getValues(Axis.Y),
                shape2.getValues(Axis.Y), flag, flag1);
        IDoubleListMerger mergerZ = makeListMerger(
                (mergerX.getList().size() - 1) * (mergerY.getList().size() - 1),
                shape1.getValues(Axis.Z), shape2.getValues(Axis.Z), flag, flag1);
        BitSetVoxelShapePart part = BitSetVoxelShapePart.join(shape1.part, shape2.part, mergerX,
                mergerY, mergerZ, function);
        return mergerX instanceof DoubleCubeMergingList
                && mergerY instanceof DoubleCubeMergingList && mergerZ instanceof DoubleCubeMergingList
                        ? new VoxelShapeCube(part)
                        : new VoxelShapeArray(part, mergerX.getList(), mergerY.getList(),
                                mergerZ.getList());
    }

    public static boolean compare(VoxelShape shape1, VoxelShape shape2, IBooleanFunction function) {
        if (function.apply(false, false)) {
            throw new IllegalArgumentException();
        }
        if (shape1 == shape2) {
            return function.apply(true, true);
        }
        if (shape1.isEmpty()) {
            return function.apply(false, !shape2.isEmpty());
        }
        if (shape2.isEmpty()) {
            return function.apply(!shape1.isEmpty(), false);
        }
        boolean flag = function.apply(true, false);
        boolean flag1 = function.apply(false, true);

        for (Axis axis : EnumAxisRotation.AXES) {
            if (shape1.getEnd(axis) < shape2.getStart(axis) - 1.0E-7D) {
                return flag || flag1;
            }

            if (shape2.getEnd(axis) < shape1.getStart(axis) - 1.0E-7D) {
                return flag || flag1;
            }
        }

        IDoubleListMerger mergerX = makeListMerger(1, shape1.getValues(Axis.X), shape2.getValues(Axis.X), flag,
                flag1);
        IDoubleListMerger mergerY = makeListMerger(mergerX.getList().size() - 1, shape1.getValues(Axis.Y),
                shape2.getValues(Axis.Y), flag, flag1);
        IDoubleListMerger mergerZ = makeListMerger(
                (mergerX.getList().size() - 1) * (mergerY.getList().size() - 1),
                shape1.getValues(Axis.Z), shape2.getValues(Axis.Z), flag, flag1);
        return joinIsNotEmpty(mergerX, mergerY, mergerZ, shape1.part, shape2.part, function);
    }

    private static boolean joinIsNotEmpty(IDoubleListMerger mergerX, IDoubleListMerger mergerY,
            IDoubleListMerger mergerZ, VoxelShapePart part1, VoxelShapePart part2, IBooleanFunction function) {
        return !mergerX
                .forMergedIndexes((i1, i2, i3) -> mergerY.forMergedIndexes((j1, j2, j3) -> mergerZ.forMergedIndexes(
                        (k1, k2, k3) -> !function.apply(part1.contains(i1, j1, k1), part2.contains(i2, j2, k2)))));
    }

    public static double getAllowedOffset(Axis axis, AxisAlignedBB aabb, Stream<VoxelShape> shapes, double offset) {
        for (Iterator<VoxelShape> iterator = shapes.iterator(); iterator
                .hasNext(); offset = iterator.next().getAllowedOffset(axis, aabb, offset)) {
            if (Math.abs(offset) < 1.0E-7D) {
                return 0.0D;
            }
        }
        return offset;
    }

    public static double getAllowedOffset(Axis axis, AxisAlignedBB aabb, IBlockAccess world, double offset,
            ISelectionContext context, Stream<VoxelShape> shapes) {
        return getAllowedOffset(aabb, world, offset, context, EnumAxisRotation.from(axis, Axis.Z), shapes);
    }

    @SuppressWarnings("unused")
    private static double getAllowedOffset(AxisAlignedBB aabb, IBlockAccess world, double offset,
            ISelectionContext context, EnumAxisRotation rotation, Stream<VoxelShape> shapes) {
        if (Math.abs(aabb.maxX - aabb.minX) < 1.0E-6D || Math.abs(aabb.maxY - aabb.minY) < 1.0E-6D
                || Math.abs(aabb.maxZ - aabb.minZ) < 1.0E-6D || Math.abs(offset) < 1.0E-7D) {
            return offset;
        }

        EnumAxisRotation reverseRot = rotation.reverse();
        Axis axisZ = reverseRot.rotate(Axis.Z);
        double minZV = getMin(aabb, axisZ) - 1.0E-7D;
        double maxZV = getMax(aabb, axisZ) + 1.0E-7D;

        offset = computeWorldOffset(aabb, world, offset, axisZ, reverseRot, minZV, maxZV);

        for (Iterator<VoxelShape> it = shapes.iterator(); it.hasNext() && Math.abs(offset) >= 1.0E-7D;) {
            offset = it.next().getAllowedOffset(axisZ, aabb, offset);
        }

        return Math.abs(offset) < 1.0E-7D ? 0.0D : offset;
    }

    private static double computeWorldOffset(AxisAlignedBB aabb, IBlockAccess world, double offset, Axis axisZ,
            EnumAxisRotation reverseRot, double minZ, double maxZ) {
        Axis axisX = reverseRot.rotate(Axis.X);
        Axis axisY = reverseRot.rotate(Axis.Y);
        int minX = MathHelper.floor(getMin(aabb, axisX) - 1.0E-7D) - 1;
        int maxX = MathHelper.floor(getMax(aabb, axisX) + 1.0E-7D) + 1;
        int minY = MathHelper.floor(getMin(aabb, axisY) - 1.0E-7D) - 1;
        int maxY = MathHelper.floor(getMax(aabb, axisY) + 1.0E-7D) + 1;
        boolean positive = offset > 0.0D;
        int sZ = positive ? MathHelper.floor(getMax(aabb, axisZ) - 1.0E-7D) - 1
                : MathHelper.floor(getMin(aabb, axisZ) + 1.0E-7D) + 1;
        int step = positive ? 1 : -1;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int z = sZ; positive ? z <= getDifferenceFloored(offset, minZ, maxZ)
                : z >= getDifferenceFloored(offset, minZ, maxZ); z += step) {
            offset = scanPlane(aabb, world, offset, axisZ, reverseRot, minX, maxX, minY, maxY, z, sZ,
                    getDifferenceFloored(offset, minZ, maxZ), pos);
            if (Math.abs(offset) < 1.0E-7D)
                return 0.0D;
        }
        return offset;
    }

    private static double scanPlane(AxisAlignedBB aabb, IBlockAccess world, double offset, Axis axisZ,
            EnumAxisRotation rot, int minX, int maxX, int minY, int maxY, int z, int sZ, int eZ,
            BlockPos.MutableBlockPos pos) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                int edges = (x == minX || x == maxX ? 1 : 0) + (y == minY || y == maxY ? 1 : 0)
                        + (z == sZ || z == eZ ? 1 : 0);
                if (edges < 3) {
                    setPosFromRotation(pos, rot, x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    if (!((edges == 1 && !state.isFullCube())
                            || (edges == 2 && state.getBlock() != Blocks.PISTON_EXTENSION))) {
                        AxisAlignedBB box = state.getCollisionBoundingBox(world, pos);
                        if (box != null) {
                            offset = create(box.offset(-pos.getX(), -pos.getY(), -pos.getZ()))
                                    .getAllowedOffset(axisZ, aabb.offset(-pos.getX(), -pos.getY(), -pos.getZ()),
                                            offset);
                            if (Math.abs(offset) < 1.0E-7D)
                                return 0.0D;
                        }
                    }
                }
            }
        }
        return offset;
    }

    private static void setPosFromRotation(BlockPos.MutableBlockPos pos, EnumAxisRotation rotation, int x, int y,
            int z) {
        pos.setPos(rotation.getCoordinate(x, y, z, Axis.X), rotation.getCoordinate(x, y, z, Axis.Y),
                rotation.getCoordinate(x, y, z, Axis.Z));
    }

    private static double getMin(AxisAlignedBB box, Axis axis) {
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

    private static double getMax(AxisAlignedBB box, Axis axis) {
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

    private static int getDifferenceFloored(double offset, double min, double max) {
        return offset > 0.0D ? MathHelper.floor(max + offset) + 1 : MathHelper.floor(min + offset) - 1;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCubeSideCovered(VoxelShape shape1, VoxelShape shape2, EnumFacing facing) {
        if (shape1 == fullCube() && shape2 == fullCube()) {
            return true;
        }
        if (shape2.isEmpty()) {
            return false;
        }
        Axis axis = facing.getAxis();
        AxisDirection direction = facing.getAxisDirection();
        VoxelShape s1 = direction == AxisDirection.POSITIVE ? shape1 : shape2;
        VoxelShape s2 = direction == AxisDirection.POSITIVE ? shape2 : shape1;
        IBooleanFunction function = direction == AxisDirection.POSITIVE ? IBooleanFunction.ONLY_FIRST
                : IBooleanFunction.ONLY_SECOND;
        return DoubleMath.fuzzyEquals(s1.getEnd(axis), 1.0D, 1.0E-7D)
                && DoubleMath.fuzzyEquals(s2.getStart(axis), 0.0D, 1.0E-7D)
                && !compare(new SplitVoxelShape(s1, axis, s1.part.getSize(axis) - 1),
                        new SplitVoxelShape(s2, axis, 0), function);
    }

    public static VoxelShape getFaceShape(VoxelShape shape, EnumFacing facing) {
        if (shape == fullCube()) {
            return fullCube();
        }
        Axis axis = facing.getAxis();
        boolean flag;
        int i;
        if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
            flag = DoubleMath.fuzzyEquals(shape.getEnd(axis), 1.0D, 1.0E-7D);
            i = shape.part.getSize(axis) - 1;
        } else {
            flag = DoubleMath.fuzzyEquals(shape.getStart(axis), 0.0D, 1.0E-7D);
            i = 0;
        }

        return !flag ? empty() : new SplitVoxelShape(shape, axis, i);
    }

    public static boolean doAdjacentCubeSidesFillSquare(VoxelShape shape1, VoxelShape shape2, EnumFacing facing) {
        if (shape1 == fullCube() || shape2 == fullCube()) {
            return true;
        }
        Axis axis = facing.getAxis();
        AxisDirection direction = facing.getAxisDirection();
        VoxelShape s1 = direction == AxisDirection.POSITIVE ? shape1 : shape2;
        VoxelShape s2 = direction == AxisDirection.POSITIVE ? shape2 : shape1;
        if (!DoubleMath.fuzzyEquals(s1.getEnd(axis), 1.0D, 1.0E-7D)) {
            s1 = empty();
        }

        if (!DoubleMath.fuzzyEquals(s2.getStart(axis), 0.0D, 1.0E-7D)) {
            s2 = empty();
        }

        return !compare(fullCube(), combine(new SplitVoxelShape(s1, axis, s1.part.getSize(axis) - 1),
                new SplitVoxelShape(s2, axis, 0), IBooleanFunction.OR), IBooleanFunction.ONLY_FIRST);
    }

    public static boolean faceShapeCovers(VoxelShape shape1, VoxelShape shape2) {
        if (shape1 == fullCube() || shape2 == fullCube()) {
            return true;
        }
        if (shape1.isEmpty() && shape2.isEmpty()) {
            return false;
        }
        return !compare(fullCube(), combine(shape1, shape2, IBooleanFunction.OR), IBooleanFunction.ONLY_FIRST);
    }

    @VisibleForTesting
    public static IDoubleListMerger makeListMerger(int size, DoubleList listA, DoubleList listB,
            boolean includeFirst, boolean includeSecond) {
        int i = listA.size() - 1;
        int j = listB.size() - 1;
        if (listA instanceof DoubleRangeList && listB instanceof DoubleRangeList) {
            long k = lcm(i, j);
            if (size * k <= 256L) {
                return new DoubleCubeMergingList(i, j);
            }
        }

        if (listA.getDouble(i) < listB.getDouble(0) - 1.0E-7D) {
            return new NonOverlappingMerger(listA, listB, false);
        }
        if (listB.getDouble(j) < listA.getDouble(0) - 1.0E-7D) {
            return new NonOverlappingMerger(listB, listA, true);
        }
        if (i == j && Objects.equals(listA, listB)) {
            if (listA instanceof SimpleDoubleMerger) {
                return (IDoubleListMerger) listA;
            }
            return listB instanceof SimpleDoubleMerger ? (IDoubleListMerger) listB
                    : new SimpleDoubleMerger(listA);
        }
        return new IndirectMerger(listA, listB, includeFirst, includeSecond);
    }

    public interface ILineConsumer {
        void consume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);
    }

}
