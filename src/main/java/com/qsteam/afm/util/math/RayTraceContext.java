package com.qsteam.afm.util.math;

import com.qsteam.afm.util.math.shapes.ISelectionContext;
import com.qsteam.afm.util.math.shapes.VoxelShape;
import com.qsteam.afm.util.math.shapes.VoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

import java.util.function.Predicate;

public class RayTraceContext {

    private final Vec3d startVec;
    private final Vec3d endVec;
    private final BlockMode blockMode;
    private final FluidMode fluidMode;
    private final ISelectionContext context;

    public RayTraceContext(Vec3d startVec, Vec3d endVec, BlockMode blockMode, FluidMode fluidMode, Entity entity) {
        this.startVec = startVec;
        this.endVec = endVec;
        this.blockMode = blockMode;
        this.fluidMode = fluidMode;
        this.context = ISelectionContext.forEntity(entity);
    }

    public Vec3d getEndVec() {
        return this.endVec;
    }

    public Vec3d getStartVec() {
        return this.startVec;
    }

    public VoxelShape getBlockShape(IBlockState state, IBlockAccess world, BlockPos pos) {
        return this.blockMode.get(state, world, pos, this.context);
    }

    public VoxelShape getFluidShape(IBlockState state, IBlockAccess world, BlockPos pos) {
        return this.fluidMode.test(state) ? createFluidShape(state) : VoxelShapes.empty();
    }

    private VoxelShape createFluidShape(IBlockState state) {
        if (state.getMaterial().isLiquid()) {
            int meta = state.getBlock().getMetaFromState(state);
            float f = BlockLiquid.getLiquidHeightPercent(meta);
            return VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, (double) (1.0F - f), 1.0D);
        }
        return VoxelShapes.empty();
    }

    public enum BlockMode implements IVoxelProvider {
        COLLIDER((state, world, pos, context) -> {
            AxisAlignedBB aabb = state.getCollisionBoundingBox(world, pos);
            if (aabb == Block.NULL_AABB) {
                return VoxelShapes.empty();
            } else return VoxelShapes.create(aabb.offset(-pos.getX(), -pos.getY(), -pos.getZ()));
        }),
        OUTLINE((state, world, pos, context) ->
                VoxelShapes.create(state.getBoundingBox(world, pos)));

        private final IVoxelProvider provider;

        BlockMode(IVoxelProvider provider) {
            this.provider = provider;
        }

        public VoxelShape get(IBlockState state, IBlockAccess world, BlockPos pos, ISelectionContext context) {
            return this.provider.get(state, world, pos, context);
        }
    }

    public enum FluidMode {
        NONE(state -> false),
        SOURCE_ONLY(state -> state.getMaterial().isLiquid() && state.getBlock().getMetaFromState(state) == 0),
        ANY(state -> state.getMaterial().isLiquid());

        private final Predicate<IBlockState> fluidTest;

        FluidMode(Predicate<IBlockState> fluidTest) {
            this.fluidTest = fluidTest;
        }

        public boolean test(IBlockState state) {
            return this.fluidTest.test(state);
        }
    }

    public interface IVoxelProvider {
        VoxelShape get(IBlockState state, IBlockAccess world, BlockPos pos, ISelectionContext context);
    }

}
