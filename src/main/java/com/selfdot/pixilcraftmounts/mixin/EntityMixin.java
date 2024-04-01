package com.selfdot.pixilcraftmounts.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public float prevYaw;
    @Shadow public boolean velocityDirty;

    @Shadow @Nullable public abstract Entity getFirstPassenger();
    @Shadow public abstract boolean isLogicalSideForUpdatingMovement();
    @Shadow public abstract World getWorld();
    @Shadow protected abstract void setRotation(float f, float g);
    @Shadow public abstract float getYaw();
    @Shadow public abstract boolean isOnGround();
    @Shadow protected abstract float getJumpVelocityMultiplier();
    @Shadow public abstract Vec3d getVelocity();
    @Shadow public abstract void setVelocity(Vec3d velocity);
    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Shadow public abstract void discard();

    @Inject(method = "removePassenger", at = @At("HEAD"))
    protected void injectRemovePassenger(Entity entity, CallbackInfo ci) { }

    @Inject(method = "saveNbt", at = @At("HEAD"), cancellable = true)
    protected void injectSaveNbt(NbtCompound nbtCompound, CallbackInfoReturnable<Boolean> cir) { }

}
