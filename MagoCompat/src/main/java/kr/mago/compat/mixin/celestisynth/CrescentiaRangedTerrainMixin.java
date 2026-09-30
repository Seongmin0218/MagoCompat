package kr.mago.compat.mixin.celestisynth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mago terrain protection for Crescentia ranged attack.
 *
 * Original behavior:
 * - destroys replaceable blocks while moving
 * - creates a TNT-style explosion on termination
 *
 * Mago behavior:
 * - preserves skill damage / movement / particles / explosion damage
 * - prevents all terrain destruction
 */
@Pseudo
@Mixin(
        targets = "org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastCrescentiaRanged",
        remap = false
)
public abstract class CrescentiaRangedTerrainMixin {

    /**
     * Prevent direct destruction of replaceable blocks.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private boolean magoCompat$preventTerrainDestruction(
            Level level,
            BlockPos pos,
            boolean dropBlock,
            Entity breaker
    ) {
        return false;
    }

    /**
     * Since replaceable blocks are no longer physically deleted,
     * treat them as passable only for the final collision check.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 1
            )
    )
    private BlockState magoCompat$treatReplaceableBlocksAsPassable(
            Level level,
            BlockPos pos
    ) {
        BlockState state = level.getBlockState(pos);

        if (state.canBeReplaced()) {
            return Blocks.AIR.defaultBlockState();
        }

        return state;
    }

    /**
     * Redirect Crescentia's TNT-style explosion and force it
     * to use ExplosionInteraction.NONE.
     *
     * Explosion damage / knockback / particles / sound remain.
     * Only block interaction is disabled.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            )
    )
    private Explosion magoCompat$explodeWithoutTerrainDamage(
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