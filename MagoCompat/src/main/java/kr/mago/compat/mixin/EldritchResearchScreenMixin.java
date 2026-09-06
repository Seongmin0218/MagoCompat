package kr.mago.compat.mixin;

import kr.mago.compat.MagoCompat;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Restores the disabled viewport dragging in Iron's Spells 'n Spellbooks'
 * Eldritch Research Screen.
 *
 * Iron's 1.21.1-3.16.1 already contains:
 * - viewportOffset
 * - mouse drag state tracking
 * - clipping/rendering using viewportOffset
 *
 * but mouseDragged() is hard-disabled with:
 *
 *     if (this.isMouseDragging && false)
 *
 * Mago restores the intended behavior without replacing the rest
 * of the research screen.
 */
@Mixin(
        targets = "io.redspace.ironsspellbooks.gui.EldritchResearchScreen"
)
public abstract class EldritchResearchScreenMixin {

    @Shadow
    boolean isMouseDragging;

    @Shadow
    Vec2 viewportOffset;

    @Unique
    private static boolean magoCompat$dragPatchLogged = false;

    /**
     * Restores viewport movement before Iron's disabled implementation runs.
     *
     * When the player clicks and drags an empty section of the Eldritch
     * Research Screen, Iron's already sets isMouseDragging to true.
     */
    @Inject(
            method = "mouseDragged",
            at = @At("HEAD"),
            cancellable = true
    )
    private void magoCompat$restoreEldritchViewportDragging(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!this.isMouseDragging) {
            return;
        }

        this.viewportOffset = new Vec2(
                (float) (this.viewportOffset.x + dragX),
                (float) (this.viewportOffset.y + dragY)
        );

        if (!magoCompat$dragPatchLogged) {
            magoCompat$dragPatchLogged = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Eldritch Research Screen viewport dragging restored."
            );
        }

        cir.setReturnValue(true);
    }
}