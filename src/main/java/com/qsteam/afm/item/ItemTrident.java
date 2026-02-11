package com.qsteam.afm.item;

import com.google.common.collect.Multimap;
import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.entity.projectile.EntityAbstractArrow;
import com.qsteam.afm.entity.projectile.EntityTrident;
import com.qsteam.afm.handler.EnchantmentHandler;
import com.qsteam.afm.handler.EnumHandler;
import com.qsteam.afm.handler.RegistryHandler;
import com.qsteam.afm.handler.SoundHandler;
import com.qsteam.afm.item.base.ItemBase;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemTrident extends ItemBase implements IItemModel {

    public static final ToolMaterial TRIDENT_MATERIAL = Objects.requireNonNull(EnumHelper.addToolMaterial(
            "TRIDENT",
            3,
            250,
            1.0F,
            8.0F,
            22));

    public ItemTrident() {
        super("trident");
        this.setFull3D();
        this.setMaxStackSize(1);
        this.setMaxDamage(TRIDENT_MATERIAL.getMaxUses());

        this.addPropertyOverride(new ResourceLocation("model"), (stack, worldIn, entityIn) -> {
            if (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) {
                return 2.0F;
            }
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Thrown")) {
                return 0.0F;
            }
            return worldIn == null ? 1.0F : 0.0F;
        });

        RegistryHandler.registerItem(this);
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumHandler.SPEAR;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            int duration = this.getMaxItemUseDuration(stack) - timeLeft;
            if (duration >= 10) {
                int riptideLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.RIPTIDE, stack);
                if (riptideLevel > 0 && !player.isWet()) {
                    return;
                }

                if (!world.isRemote) {
                    stack.damageItem(1, player);
                    if (riptideLevel == 0) {
                        EntityTrident trident = new EntityTrident(world, player, stack);
                        trident.shoot(player, player.rotationPitch, player.rotationYaw,
                                2.5F * getTridentVelocity(duration),  1.0F);
                        if (player.capabilities.isCreativeMode) {
                            trident.setPickupStatus(EntityAbstractArrow.PickupStatus.CREATIVE_ONLY);
                        }

                        world.spawnEntity(trident);
                        world.playSound(null, trident.posX, trident.posY, trident.posZ, SoundHandler.ITEM_TRIDENT_THROW,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);

                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.deleteStack(stack);
                        }
                    }
                }

                // TODO: add stat for item used

                if (riptideLevel > 0) {
                    float yaw = player.rotationYaw;
                    float pitch = player.rotationPitch;
                    float fX = -MathHelper.sin(yaw * ((float) Math.PI / 180F))
                            * MathHelper.cos(pitch * ((float) Math.PI / 180F));
                    float fY = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
                    float fZ = MathHelper.cos(yaw * ((float) Math.PI / 180F))
                            * MathHelper.cos(pitch * ((float) Math.PI / 180F));
                    float length = MathHelper.sqrt(fX * fX + fY * fY + fZ * fZ);
                    float force = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
                    fX *= force / length;
                    fY *= force / length;
                    fZ *= force / length;
                    player.addVelocity(fX, fY, fZ);

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundHandler.ITEM_TRIDENT_RIPTIDE,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);

                    if (player.onGround) {
                        player.move(MoverType.SELF, 0.0, 1.1999999284744263D, 0.0);
                    }

                    // TODO: startSpinAttack(20)
                }
            }
        }
    }

    public static float getTridentVelocity(int duration) {
        float f = duration / 20.0F;
        return Math.min(1.0F, f * (f + 2.0F) / 3.0F);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (itemstack.getItemDamage() >= itemstack.getMaxDamage() - 1) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        } else if (EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.RIPTIDE, itemstack) > 0
                && !playerIn.isWet()) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
            EntityLivingBase entityLiving) {
        if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER,
                    "Weapon modifier", (double) TRIDENT_MATERIAL.getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.9000000953674316D, 0));
        }
        return multimap;
    }

    @Override
    public int getItemEnchantability() {
        return TRIDENT_MATERIAL.getEnchantability();
    }

    @Override
    public void registerModels() {
        ClientProxy.registerItemModel(this);
    }
}
