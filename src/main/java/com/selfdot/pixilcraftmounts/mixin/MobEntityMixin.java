package com.selfdot.pixilcraftmounts.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntityMixin {

    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    protected void injectGetControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) { }

}
