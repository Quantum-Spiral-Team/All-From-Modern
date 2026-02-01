package com.qsteam.afm.util.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.qsteam.afm.util.math.AFMMathHelper;
import com.qsteam.afm.util.math.RayTraceContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class AFMProjectileHelper {

    /**
     * Основной метод для получения результата попадания снаряда.
     * Проверяет столкновения как с блоками, так и с сущностями.
     */
    public static RayTraceResult getProjectileHitResult(Entity projectile, boolean checkBlocks, boolean checkEntities,
            @Nullable Entity excludedEntity, RayTraceContext.BlockMode blockMode) {
        return rayTrace(projectile, checkBlocks, checkEntities, excludedEntity, blockMode, true,
                (entity) -> entity instanceof EntityPlayer &&
                        !((EntityPlayer) entity).isSpectator() &&
                        entity.canBeCollidedWith() &&
                        (checkEntities || !entity.isEntityEqual(excludedEntity)) &&
                        !entity.noClip,
                projectile.getEntityBoundingBox().grow(projectile.motionX, projectile.motionY, projectile.motionZ)
                        .expand(1.0D, 1.0D, 1.0D));
    }

    /**
     * Перегрузка для общего поиска столкновений.
     */
    public static RayTraceResult getHitResult(Entity entity, AxisAlignedBB aabb, Predicate<Entity> filter,
            RayTraceContext.BlockMode blockMode, boolean checkBlocks) {
        return rayTrace(entity, checkBlocks, false, null, blockMode, false, filter, aabb);
    }

    /**
     * Публичный метод для поиска столкновения только с сущностями.
     */
    @Nullable
    public static RayTraceResult rayTraceEntities(World world, Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter) {
        return findEntityOnPath(world, projectile, startVec, endVec, boundingBox, filter, Double.MAX_VALUE);
    }

    /**
     * Внутренний метод, выполняющий основную логику трассировки лучей (RayTracing).
     * Сначала проверяет блоки, затем сущности, и возвращает ближайшее столкновение.
     */
    private static RayTraceResult rayTrace(Entity projectile, boolean checkBlocks, boolean checkEntities,
            @Nullable Entity excludedEntity, RayTraceContext.BlockMode blockMode, boolean checkCollisionBoxes,
            Predicate<Entity> filter, AxisAlignedBB aabb) {
        double x = projectile.posX;
        double y = projectile.posY;
        double z = projectile.posZ;
        Vec3d motion = new Vec3d(projectile.motionX, projectile.motionY, projectile.motionZ);
        World world = projectile.world;
        Vec3d startVec = new Vec3d(x, y, z);

        // В 1.12.2 используем getCollisionBoxes для проверки коллизий
        if (checkCollisionBoxes && !world.getCollisionBoxes(projectile, projectile.getEntityBoundingBox()).isEmpty()) {
            return new RayTraceResult(RayTraceResult.Type.BLOCK, startVec,
                    EnumFacing.getFacingFromVector((float) motion.x, (float) motion.y, (float) motion.z),
                    new BlockPos(projectile));
        } else {
            Vec3d endVec = startVec.add(motion);
            // Используем стандартный метод rayTraceBlocks из 1.12.2
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

            // Если rayTraceBlocks вернул null (MISS), создаем объект MISS для
            // согласованности, если требуется,
            // но в 1.12.2 часто возвращается RTR.Type.MISS. Однако world.rayTraceBlocks
            // может вернуть null в 1.12.2 если MISS.
            // Для совместимости с логикой 1.14 (где всегда возвращается объект), проверим
            // null.
            if (rayTraceResult == null) {
                // Важно: в 1.12.2 null означает MISS.
                // Однако вызывающий код может ожидать объект.
                // Создадим фиктивный MISS result или вернем null, если вызывающий код готов.
                // Посмотрим на getProjectileHitResult -> возвращает RayTraceResult.
                // RayTraceResult.Type.MISS существует.
                rayTraceResult = new RayTraceResult(RayTraceResult.Type.MISS, endVec, null, null);
            }

            return rayTraceResult;
        }
    }

    /**
     * Клиентский метод для поиска сущности под курсором (или на пути вектора).
     * Учитывает хитбоксы с учетом `collisionBorderSize` и логику наездников.
     */
    @Nullable
    @SideOnly(Side.CLIENT)
    public static RayTraceResult rayTraceEntitiesClient(Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter, double maxDistance) {
        World world = projectile.world;
        double closestDistance = maxDistance;
        Entity closestEntity = null;
        Vec3d hitVec = null;

        for (Entity entity : world.getEntitiesInAABBexcluding(projectile, boundingBox, filter)) {
            AxisAlignedBB entityAABB = entity.getEntityBoundingBox().expand((double) entity.getCollisionBorderSize(),
                    (double) entity.getCollisionBorderSize(), (double) entity.getCollisionBorderSize());
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

    /**
     * Поиск ближайшей сущности на векторе движения (Серверная логика).
     * Используется для определения попадания снаряда в моба/игрока.
     */
    @Nullable
    public static RayTraceResult findEntityOnPath(World world, Entity projectile, Vec3d startVec, Vec3d endVec,
            AxisAlignedBB boundingBox, Predicate<Entity> filter, double maxDistance) {
        double closestDistance = maxDistance;
        Entity closestEntity = null;

        for (Entity entity : world.getEntitiesInAABBexcluding(projectile, boundingBox, filter)) {
            AxisAlignedBB entityAABB = entity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D); // Увеличенный хитбокс
                                                                                               // для снарядов
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

    private static Set<Entity> getEntityAndVehicle(Entity entity) {
        Entity vehicle = entity.getRidingEntity();
        return vehicle != null ? ImmutableSet.of(entity, vehicle) : ImmutableSet.of(entity);
    }

    /**
     * Поворачивает сущность (снаряд) в направлении её движения.
     * Обновляет yaw и pitch.
     */
    public static void rotateTowardsMovement(Entity entity, float partialTicks) {
        Vec3d motion = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
        float horizontalSpeed = MathHelper.sqrt(motion.x * motion.x + motion.z * motion.z);

        entity.rotationYaw = (float) (MathHelper.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F;

        for (entity.rotationPitch = (float) (MathHelper.atan2((double) horizontalSpeed, motion.y)
                * (double) (180F / (float) Math.PI)) - 90.0F; entity.rotationPitch
                        - entity.prevRotationPitch < -180.0F; entity.prevRotationPitch -= 360.0F) {
        }

        while (entity.rotationPitch - entity.prevRotationPitch >= 180.0F) {
            entity.prevRotationPitch += 360.0F;
        }

        while (entity.rotationYaw - entity.prevRotationYaw < -180.0F) {
            entity.prevRotationYaw -= 360.0F;
        }

        while (entity.rotationYaw - entity.prevRotationYaw >= 180.0F) {
            entity.prevRotationYaw += 360.0F;
        }

        entity.rotationPitch = AFMMathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        entity.rotationYaw = AFMMathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);
    }

    /**
     * Определяет, в какой руке находится указанный предмет.
     */
    public static EnumHand getHandWith(EntityLivingBase livingEntity, Item item) {
        return livingEntity.getHeldItemMainhand().getItem() == item ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }

    /**
     * Создает сущность стрелы на основе ItemStack.
     * Обрабатывает зачарования и эффекты зелий (если это Tipped Arrow).
     */
    // public static EntityAbstractArrow createArrow(EntityLivingBase shooter,
    // ItemStack ammoStack, float distanceFactor) {
    // ArrowItem arrowItem = (ArrowItem)(ammoStack.getItem() instanceof ArrowItem ?
    // ammoStack.getItem() : Items.ARROW);
    // EntityAbstractArrow arrowEntity = arrowItem.createArrow(shooter.world,
    // ammoStack, shooter);
    // arrowEntity.setEnchantmentEffectsFromEntity(shooter, distanceFactor);
    //
    // if (ammoStack.getItem() == Items.TIPPED_ARROW && arrowEntity instanceof
    // ArrowEntity) {
    // ((EntityAbstractArrow) arrowEntity).setPotionEffect(ammoStack);
    // }
    //
    // return arrowEntity;
    // }

}
