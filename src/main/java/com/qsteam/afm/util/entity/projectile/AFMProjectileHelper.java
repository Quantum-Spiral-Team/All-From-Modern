package com.qsteam.afm.util.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.qsteam.afm.entity.projectile.EntityAbstractArrow;
import com.qsteam.afm.util.math.AFMMathHelper;
import com.qsteam.afm.util.math.RayTraceContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class AFMProjectileHelper {

    public static RayTraceResult getProjectileHitResult(Entity projectile, boolean checkBlocks, boolean checkEntities,
            @Nullable Entity excludedEntity, RayTraceContext.BlockMode blockMode) {
        return rayTrace(projectile, checkBlocks, checkEntities, excludedEntity, blockMode, true,
                entity -> entity instanceof EntityPlayer &&
                        !((EntityPlayer) entity).isSpectator() &&
                        entity.canBeCollidedWith() &&
                        (checkEntities || !entity.isEntityEqual(excludedEntity)) &&
                        !entity.noClip,
                projectile.getEntityBoundingBox().grow(projectile.motionX, projectile.motionY, projectile.motionZ)
                        .expand(1.0D, 1.0D, 1.0D));
    }

    public static RayTraceResult getHitResult(Entity entity, AxisAlignedBB aabb, Predicate<Entity> filter,
            RayTraceContext.BlockMode blockMode, boolean checkBlocks) {
        return rayTrace(entity, checkBlocks, false, null, blockMode, false, filter, aabb);
    }

    @Nullable
    public static RayTraceResult rayTraceEntities(World world, Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter) {
        return findEntityOnPath(world, projectile, startVec, endVec, boundingBox, filter, Double.MAX_VALUE);
    }

    private static RayTraceResult rayTrace(Entity projectile, boolean checkBlocks, boolean checkEntities,
            @Nullable Entity excludedEntity, RayTraceContext.BlockMode blockMode, boolean checkCollisionBoxes,
            Predicate<Entity> filter, AxisAlignedBB aabb) {
        double x = projectile.posX;
        double y = projectile.posY;
        double z = projectile.posZ;
        Vec3d motion = new Vec3d(projectile.motionX, projectile.motionY, projectile.motionZ);
        World world = projectile.world;
        Vec3d startVec = new Vec3d(x, y, z);

        if (checkCollisionBoxes && !world.getCollisionBoxes(projectile, projectile.getEntityBoundingBox()).isEmpty()) {
            return new RayTraceResult(RayTraceResult.Type.BLOCK, startVec,
                    EnumFacing.getFacingFromVector((float) motion.x, (float) motion.y, (float) motion.z),
                    new BlockPos(projectile));
        } else {
            Vec3d endVec = startVec.add(motion);
            RayTraceResult rayTraceResult = world.rayTraceBlocks(startVec, endVec, false, true, false);

            if (checkBlocks) {
                if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
                    endVec = rayTraceResult.hitVec;
                }

                RayTraceResult entityHitResult = rayTraceEntities(world, projectile, startVec, endVec, aabb, filter);
                if (entityHitResult != null) {
                    rayTraceResult = entityHitResult;
                }
            }

            if (rayTraceResult == null) {
                rayTraceResult = new RayTraceResult(RayTraceResult.Type.MISS, endVec, null, null);
            }

            return rayTraceResult;
        }
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public static RayTraceResult rayTraceEntitiesClient(Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter, double maxDistance) {
        World world = projectile.world;
        double closestDistance = maxDistance;
        Entity closestEntity = null;
        Vec3d hitVec = null;

        for (Entity entity : world.getEntitiesInAABBexcluding(projectile, boundingBox, filter)) {
            AxisAlignedBB entityAABB = entity.getEntityBoundingBox().expand(entity.getCollisionBorderSize(),
                    entity.getCollisionBorderSize(), entity.getCollisionBorderSize());
            RayTraceResult intercept = entityAABB.calculateIntercept(startVec, endVec);

            if (entityAABB.contains(startVec)) {
                if (closestDistance >= 0.0D) {
                    closestEntity = entity;
                    hitVec = intercept != null ? intercept.hitVec : startVec;
                    closestDistance = 0.0D;
                }
            } else if (intercept != null) {
                Vec3d resultVec = intercept.hitVec;
                double distSq = startVec.squareDistanceTo(resultVec);

                if (distSq < closestDistance || closestDistance == 0.0D) {
                    if (entity.getLowestRidingEntity() == projectile.getLowestRidingEntity()
                            && !entity.canRiderInteract()) {
                        if (closestDistance == 0.0D) {
                            closestEntity = entity;
                            hitVec = resultVec;
                        }
                    } else {
                        closestEntity = entity;
                        hitVec = resultVec;
                        closestDistance = distSq;
                    }
                }
            }
        }

        if (closestEntity == null) {
            return null;
        } else {
            return new RayTraceResult(closestEntity, hitVec);
        }
    }

    @Nullable
    public static RayTraceResult findEntityOnPath(World world, Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter, double maxDistance) {
        double closestDistance = maxDistance;
        Entity closestEntity = null;

        for (Entity entity : world.getEntitiesInAABBexcluding(projectile, boundingBox, filter)) {
            AxisAlignedBB entityAABB = entity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D);

            RayTraceResult intercept = entityAABB.calculateIntercept(startVec, endVec);

            if (intercept != null) {
                double distSq = startVec.squareDistanceTo(intercept.hitVec);
                if (distSq < closestDistance) {
                    closestEntity = entity;
                    closestDistance = distSq;
                }
            }
        }

        if (closestEntity == null) {
            return null;
        } else {
            return new RayTraceResult(closestEntity);
        }
    }

    public static void rotateTowardsMovement(Entity entity, float partialTicks) {
        double x = entity.motionX;
        double y = entity.motionY;
        double z = entity.motionZ;

        float horizontalSpeed = MathHelper.sqrt(x * x + z * z);

        float targetYaw = (float) (MathHelper.atan2(z, x) * (180D / Math.PI)) + 90.0F;
        float targetPitch = (float) (MathHelper.atan2(horizontalSpeed, y) * (180D / Math.PI)) - 90.0F;

        entity.prevRotationPitch = updateCursor(entity.prevRotationPitch, targetPitch);
        entity.prevRotationYaw = updateCursor(entity.prevRotationYaw, targetYaw);

        entity.rotationPitch = AFMMathHelper.lerp(entity.prevRotationPitch, targetPitch, partialTicks);
        entity.rotationYaw = AFMMathHelper.lerp(entity.prevRotationYaw, targetYaw, partialTicks);
    }

    public static float updateCursor(float prev, float target) {
        while (target - prev < -180.0F) prev -= 360.0F;
        while (target - prev >= 180.0F) prev += 360.0F;
        return prev;
    }

    public static EnumHand getHandWith(EntityLivingBase livingEntity, Item item) {
        return livingEntity.getHeldItemMainhand().getItem() == item ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }

//     public static EntityAbstractArrow createArrow(EntityLivingBase shooter,
//                                                   ItemStack ammoStack, float distanceFactor) {
//        ArrowItem arrowItem = (ArrowItem)(ammoStack.getItem() instanceof ArrowItem ?
//        ammoStack.getItem() : Items.ARROW);
//        EntityAbstractArrow arrowEntity = arrowItem.createArrow(shooter.world,
//        ammoStack, shooter);
//        arrowEntity.setEnchantmentEffectsFromEntity(shooter, distanceFactor);
//
//        if (ammoStack.getItem() == Items.TIPPED_ARROW) {
//            ((EntityAbstractArrow) arrowEntity).setPotionEffect(ammoStack);
//        }
//
//        return arrowEntity;
//     }

}
