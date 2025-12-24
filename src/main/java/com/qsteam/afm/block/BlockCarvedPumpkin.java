package com.qsteam.afm.block;


import com.qsteam.afm.AllFromModern;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockCarvedPumpkin extends BlockHorizontal {

    private static final int SPAWN_PARTICLE_COUNT = 120;
    private final BlockPattern snowmanPattern;
    private final BlockPattern golemPattern;

    public BlockCarvedPumpkin() {
        super(Material.WOOD);
        setCreativeTab(AllFromModern.AFM_TAB);
        setRegistryName("carved_pumpkin");
        setTranslationKey("carved_pumpkin");
        setHardness(1.0F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        
        this.snowmanPattern = FactoryBlockPattern.start()
            .aisle("^", "#", "#")
            .where('^', BlockWorldState.hasState(state -> state != null && state.getBlock() == AFMBlocks.CARVED_PUMPKIN))
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW)))
            .build();
        
        this.golemPattern = FactoryBlockPattern.start()
            .aisle("~^~", "###", "~#~")
            .where('^', BlockWorldState.hasState(state -> state != null && state.getBlock() == AFMBlocks.CARVED_PUMPKIN))
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
            .build();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (!worldIn.isRemote) {
            trySpawnGolem(worldIn, pos);
        }
    }

    private void trySpawnGolem(World worldIn, BlockPos pos) {
        BlockPattern.PatternHelper helper = snowmanPattern.match(worldIn, pos);
        if (helper != null) {
            spawnSnowman(worldIn, helper);
        } else {
            helper = golemPattern.match(worldIn, pos);
            if (helper != null) {
                spawnIronGolem(worldIn, helper);
            }
        }
    }

    private void spawnSnowman(World world, BlockPattern.PatternHelper helper) {
        clearBlocksWithParticles(world, helper, snowmanPattern.getThumbLength(), 1);
        
        BlockPos spawnPos = helper.translateOffset(0, 2, 0).getPos();
        EntitySnowman snowman = new EntitySnowman(world);
        snowman.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.05D, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        world.spawnEntity(snowman);
        
        triggerSummonedEntity(world, snowman);
        for (int j1 = 0; j1 < SPAWN_PARTICLE_COUNT; ++j1) {
            spawnEffectParticles((WorldServer) world, spawnPos, EnumParticleTypes.SNOW_SHOVEL, 2.5D);
        }
        notifyNeighbors(world, helper, snowmanPattern.getThumbLength(), 1);
    }

    private void spawnIronGolem(World world, BlockPattern.PatternHelper helper) {
        clearBlocksWithParticles(world, helper, golemPattern.getThumbLength(), golemPattern.getPalmLength());
        
        BlockPos spawnPos = helper.translateOffset(1, 2, 0).getPos();
        EntityIronGolem golem = new EntityIronGolem(world);
        golem.setPlayerCreated(true);
        golem.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.05D, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        world.spawnEntity(golem);
        
        triggerSummonedEntity(world, golem);
        for (int j1 = 0; j1 < SPAWN_PARTICLE_COUNT; ++j1) {
            spawnEffectParticles((WorldServer) world, spawnPos, EnumParticleTypes.SNOWBALL, 3.9D);
        }
        notifyNeighbors(world, helper, golemPattern.getThumbLength(), golemPattern.getPalmLength());
    }

    private void clearBlocksWithParticles(World world, BlockPattern.PatternHelper helper, int height, int width) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                BlockPos blockPos = helper.translateOffset(x, y, 0).getPos();
                IBlockState blockState = world.getBlockState(blockPos);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);
                world.playEvent(2001, blockPos, Block.getStateId(blockState));
            }
        }
    }

    private void triggerSummonedEntity(World world, Entity entity) {
        for (EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, entity.getEntityBoundingBox().grow(5.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(player, entity);
        }
    }

    private void spawnEffectParticles(WorldServer world, BlockPos pos, EnumParticleTypes particleType, double heightMultiplier) {
        for (int i = 0; i < SPAWN_PARTICLE_COUNT; ++i) {
            double x = pos.getX() + world.rand.nextDouble();
            double y = pos.getY() + world.rand.nextDouble() * heightMultiplier;
            double z = pos.getZ() + world.rand.nextDouble();
            world.spawnParticle(particleType, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    private void notifyNeighbors(World world, BlockPattern.PatternHelper helper, int height, int width) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                BlockWorldState state = helper.translateOffset(x, y, 0);
                world.notifyNeighborsRespectDebug(state.getPos(), Blocks.AIR, false);
            }
        }
    }



}
