package com.selfdot.pixilcraftmounts.mixin;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.selfdot.pixilcraftmounts.imixin.IPokemonEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PokemonEntity.class)
public abstract class PokemonEntityMixin extends MobEntityMixin implements IPokemonEntityMixin {

    @Shadow
    private Pokemon pokemon;

    @Unique
    private boolean isMount = false;

    @Unique
    public void pixilCraftMounts$setIsMount(boolean isMount) {
        this.isMount = isMount;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void injectDamage(CallbackInfoReturnable<Boolean> cir) {
        if (isMount) cir.setReturnValue(false);
    }
    
    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void injectRemove(CallbackInfo cir) {
        if (isMount) cir.cancel();
    }

    @Inject(method = "shouldSave", at = @At("HEAD"), cancellable = true)
    private void injectShouldSave(CallbackInfoReturnable<Boolean> cir) {
        if (isMount) cir.setReturnValue(false);
    }

    @Override
    protected void injectGetControlledMovementInput(
        PlayerEntity playerEntity, Vec3d vec3d, CallbackInfoReturnable<Vec3d> cir
    ) {
        float f = playerEntity.sidewaysSpeed * 0.5F;
        float g = playerEntity.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        cir.setReturnValue(new Vec3d(f, 0.0, g));
    }

    @Override
    protected void injectTickControlled(PlayerEntity playerEntity, Vec3d vec3d, CallbackInfo ci) {
        Vec2f vec2f = new Vec2f(playerEntity.getPitch() * 0.5F, playerEntity.getYaw());
        this.setRotation(vec2f.y, vec2f.x);
        this.prevYaw = this.bodyYaw = this.headYaw = this.getYaw();
    }

    @Override
    protected void injectGetControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        if (getFirstPassenger() instanceof LivingEntity livingEntity) {
            cir.setReturnValue(livingEntity);
        }
    }

    @Override
    protected void injectGetMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (getWorld().isClient && isLogicalSideForUpdatingMovement()) {
            cir.setReturnValue((3 + (25 * (pokemon.getStat(Stats.SPEED) / 500f))) / 40f);
        }
    }

    @Override
    protected void injectRemovePassenger(Entity entity, CallbackInfo ci) {
        discard();
    }

    @Override
    protected void injectSaveNbt(NbtCompound nbtCompound, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
    }

}
