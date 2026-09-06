package kr.mago.compat.mixin;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import kr.mago.compat.spell.MagoSpellPolicy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Hides Mago-retired spell schools from Apprentice Codex School Affinity
 * without unregistering the schools themselves.
 *
 * Registry objects remain intact for compatibility with addons.
 */
@Pseudo
@Mixin(
        targets = "jp.aquafactory.apprenticecodex.utility.SchoolAffinityRegistry",
        remap = false
)
public abstract class SchoolAffinityRegistryMixin {

    /**
     * Removes hidden addon schools before Apprentice Codex chooses
     * the 16 extra School Affinity slots.
     *
     * This prevents retired schools from appearing and from consuming
     * one of the available extra slots.
     */
    @ModifyVariable(
            method = "resolveSelectedExtraSchools",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private static List<SchoolType> magoCompat$filterHiddenExtraSchools(
            List<SchoolType> extraSchools
    ) {
        return extraSchools.stream()
                .filter(school ->
                        !MagoSpellPolicy.isHiddenSchool(
                                school.getId()
                        )
                )
                .toList();
    }

    /**
     * Final safeguard for every School Affinity assignment.
     *
     * This is required for Blood because Apprentice Codex treats
     * Iron's built-in schools as fixed slots and its normal deny
     * configuration cannot remove them.
     */
    @Inject(
            method = "assignSchoolToSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void magoCompat$blockHiddenSchoolAssignment(
            int slotIndex,
            SchoolType schoolType,
            CallbackInfoReturnable<Object> cir
    ) {
        if (schoolType == null) {
            return;
        }

        if (!MagoSpellPolicy.isHiddenSchool(
                schoolType.getId()
        )) {
            return;
        }

        cir.setReturnValue(null);
    }
}
