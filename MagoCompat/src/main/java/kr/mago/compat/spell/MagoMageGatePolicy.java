package kr.mago.compat.spell;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public final class MagoMageGatePolicy {

    public static final String ACTIVE_TAG =
            "mago_mage_active";

    private static final Map<String, String> SCHOOL_TAGS =
            Map.ofEntries(
                    Map.entry(
                            "irons_spellbooks:fire",
                            "mago_magic_school_fire"
                    ),
                    Map.entry(
                            "aces_spell_utils:hydro",
                            "mago_magic_school_hydro"
                    ),
                    Map.entry(
                            "irons_spellbooks:nature",
                            "mago_magic_school_nature"
                    ),
                    Map.entry(
                            "gtbcs_geomancy_plus:geo",
                            "mago_magic_school_geo"
                    ),
                    Map.entry(
                            "irons_spellbooks:lightning",
                            "mago_magic_school_lightning"
                    ),
                    Map.entry(
                            "irons_spellbooks:ice",
                            "mago_magic_school_ice"
                    ),
                    Map.entry(
                            "irons_spellbooks:ender",
                            "mago_magic_school_ender"
                    ),
                    Map.entry(
                            "irons_spellbooks:holy",
                            "mago_magic_school_holy"
                    ),
                    Map.entry(
                            "irons_spellbooks:evocation",
                            "mago_magic_school_summon"
                    ),
                    Map.entry(
                            "tunes_n_tomes:melody",
                            "mago_magic_school_melody"
                    ),
                    Map.entry(
                            "wind_spellbooks:wind",
                            "mago_magic_school_wind"
                    )
            );

    private static final Map<SpellRarity, String> RARITY_TAGS =
            Map.of(
                    SpellRarity.COMMON,
                    "mago_mage_tier_common",

                    SpellRarity.UNCOMMON,
                    "mago_mage_tier_uncommon",

                    SpellRarity.RARE,
                    "mago_mage_tier_rare",

                    SpellRarity.EPIC,
                    "mago_mage_tier_epic",

                    SpellRarity.LEGENDARY,
                    "mago_mage_tier_legendary"
            );


    private MagoMageGatePolicy() {
    }


    public static GateResult check(
            Player player,
            String spellId,
            int spellLevel,
            SchoolType school
    ) {

        if (player == null || school == null) {
            return GateResult.permit();
        }

        String schoolId =
                String.valueOf(
                        school.getId()
                );

        String requiredSchoolTag =
                SCHOOL_TAGS.get(
                        schoolId
                );


        /*
         * Hidden schools and other-class hidden schools are intentionally
         * outside the basic Mage 11-school gate.
         *
         * Occult / Abyssal / Radiance / Cosmic / Technomancy /
         * Spellblade / Spirit etc. are handled by their own qualification
         * systems later.
         */
        if (requiredSchoolTag == null) {
            return GateResult.permit();
        }


        if (
                !player.getTags().contains(
                        ACTIVE_TAG
                )
        ) {
            return GateResult.denied(
                    "현재 마법사 전문화가 활성화되어 있지 않습니다."
            );
        }


        if (
                !player.getTags().contains(
                        requiredSchoolTag
                )
        ) {
            return GateResult.denied(
                    "해당 학파의 마법 사용권이 없습니다."
            );
        }


        var spell =
                SpellRegistry.getSpell(
                        spellId
                );

        SpellRarity rarity =
                spell.getRarity(
                        spellLevel
                );

        String requiredRarityTag =
                RARITY_TAGS.get(
                        rarity
                );


        if (
                requiredRarityTag != null
                        && !player.getTags().contains(
                                requiredRarityTag
                        )
        ) {
            return GateResult.denied(
                    "현재 마법 등급으로 사용할 수 없는 주문입니다. 필요 등급: "
                            + rarity.getDisplayName().getString()
            );
        }


        return GateResult.permit();
    }


        public record GateResult(
                boolean allowed,
                Component message
        ) {

        public static GateResult permit() {
                return new GateResult(
                        true,
                        Component.empty()
                );
        }


        public static GateResult denied(
                String message
        ) {
                return new GateResult(
                        false,
                        Component.literal(
                                message
                        )
                );
        }
        }
}
