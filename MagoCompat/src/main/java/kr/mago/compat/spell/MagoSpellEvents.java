package kr.mago.compat.spell;

import io.redspace.ironsspellbooks.api.config.ModifyDefaultConfigValuesEvent;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import kr.mago.compat.MagoCompat;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

public final class MagoSpellEvents {

    private static boolean registered = false;


    private MagoSpellEvents() {
    }


    public static void register() {

        if (registered) {
            return;
        }

        registered = true;


        /*
         * Run last.
         *
         * Several Iron's addons also modify default spell schools.
         * Mago is the final modpack-level policy layer.
         */
        NeoForge.EVENT_BUS.addListener(
                EventPriority.LOWEST,
                MagoSpellEvents::onModifyDefaultConfigValues
        );


        /*
         * Hard cast block for deleted spells.
         *
         * enabled=false alone is not enough because an already existing
         * scroll may still attempt to cast the spell.
         */
        NeoForge.EVENT_BUS.addListener(
                EventPriority.HIGHEST,
                MagoSpellEvents::onSpellPreCast
        );


        MagoCompat.LOGGER.info(
                "[MagoCompat] Spell policy registered. "
                        + "School overrides: {} | Disabled spells: {}",
                MagoSpellPolicy.getSchoolOverrideCount(),
                MagoSpellPolicy.getDisabledSpellCount()
        );
    }


    private static void onModifyDefaultConfigValues(
            ModifyDefaultConfigValuesEvent event
    ) {

        String spellId =
                event.getSpell().getSpellId();


        // =====================================================
        // School override
        // =====================================================

        ResourceLocation targetSchoolId =
                MagoSpellPolicy.getTargetSchool(
                        spellId
                );


        if (targetSchoolId != null) {

            SchoolType targetSchool =
                    SchoolRegistry.getSchool(
                            targetSchoolId
                    );


            if (targetSchool == null) {

                throw new IllegalStateException(
                        "[MagoCompat] Target spell school does not exist. "
                                + "Spell: "
                                + spellId
                                + " | Target: "
                                + targetSchoolId
                );
            }


            event.setDefaultValue(
                    SpellConfigParameter.SCHOOL,
                    targetSchool
            );
        }


        // =====================================================
        // Deleted spell
        // =====================================================

        if (
                MagoSpellPolicy.isDisabled(
                        spellId
                )
        ) {

            event.setDefaultValue(
                    SpellConfigParameter.ENABLED,
                    false
            );

            event.setDefaultValue(
                    SpellConfigParameter.ALLOW_CRAFTING,
                    false
            );
        }
    }


    private static void onSpellPreCast(
            SpellPreCastEvent event
    ) {

        if (
                !MagoSpellPolicy.isDisabled(
                        event.getSpellId()
                )
        ) {
            return;
        }


        event.setCanceled(true);
    }
}