package kr.mago.compat.mixin.celestisynth;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mago terrain protection for Crescentia Dragon.
 *
 * Both expiration and block-impact explosions retain their
 * combat effects but cannot modify blocks.
 */
@Pseudo
@Mixin(
        targets = "org.thecelestialworkshop.celestisynth.common.entity.projectile.CrescentiaDragon",
        remap = false
)
public abstract class CrescentiaDragonTerrainMixin {

    /**
     * Explosion caused when the dragon reaches the end of its lifespan.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            )
    )
    private Explosion magoCompat$lifespanExplosionWithoutTerrainDamage(
            Level level,
            Entity source,
            double x,
            double y,
            double z,
            float radius,
            Level.ExplosionInteraction originalInteraction
    ) {
        return level.explode(
                source,
                x,
                y,
                z,
                radius,
                Level.ExplosionInteraction.NONE
        );
    }

    /**
     * Explosion caused when the dragon collides with a block.
     */
    @Redirect(
            method = "onHitBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            )
    )
    private Explosion magoCompat$impactExplosionWithoutTerrainDamage(
            Level level,
            Entity source,
            double x,
            double y,
            double z,
            float radius,
            Level.ExplosionInteraction originalInteraction
    ) {
        return level.explode(
                source,
                x,
                y,
                z,
                radius,
                Level.ExplosionInteraction.NONE
        );
    }
}