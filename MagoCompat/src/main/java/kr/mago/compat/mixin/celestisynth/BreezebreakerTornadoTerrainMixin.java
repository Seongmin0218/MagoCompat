package kr.mago.compat.mixin.celestisynth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mago terrain protection for Celestisynth Breezebreaker.
 *
 * Original Breezebreaker tornado destroys replaceable blocks
 * around its path.
 *
 * Mago keeps the tornado's damage / movement / visuals,
 * but prevents all block destruction.
 */
@Pseudo
@Mixin(
        targets = "org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastBreezebreakerTornado",
        remap = false
)
public abstract class BreezebreakerTornadoTerrainMixin {

    /**
     * Prevent the tornado from actually destroying replaceable blocks.
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
     * Original behavior destroys replaceable blocks first and then checks
     * whether the tornado collided with a non-air block.
     *
     * Since Mago no longer destroys those blocks, treat replaceable blocks
     * as air only for this collision check.
     *
     * This lets the tornado continue through grass, snow layers, flowers,
     * etc. without deleting them.
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
}