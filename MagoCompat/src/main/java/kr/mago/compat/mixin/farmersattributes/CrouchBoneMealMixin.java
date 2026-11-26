package kr.mago.compat.mixin.farmersattributes;

import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevents players from bypassing the crouch bonemeal
 * cooldown by repeatedly releasing the crouch key.
 *
 * Target: Farmer's Attributes 1.9.0
 */
@Pseudo
@Mixin(
        targets = "medi.makiba.farmers_attributes.attribute.CrouchBoneMeal",
        remap = false
)
public abstract class CrouchBoneMealMixin {

    /**
     * Preserve the current cooldown when the player
     * stops crouching.
     *
     * The original cooldown progression and growth
     * probability remain unchanged.
     */
    @Redirect(
            method = "tickCrouchBoneMeal",
            at = @At(
                    value = "INVOKE",
                    target = "Lmedi/makiba/farmers_attributes/attribute/"
                            + "CrouchBoneMeal;"
                            + "resetCooldown("
                            + "Lnet/minecraft/server/level/ServerPlayer;"
                            + ")V"
            ),
            require = 1,
            allow = 1
    )
    private static void magoCompat$preserveCrouchBonemealCooldown(
            ServerPlayer player
    ) {
        // Intentionally do nothing.
        // Preserve the existing cooldown attachment.
    }
}