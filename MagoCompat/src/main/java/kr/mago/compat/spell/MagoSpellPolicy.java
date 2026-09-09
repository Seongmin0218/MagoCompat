package kr.mago.compat.spell;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MagoSpellPolicy {

    // =========================================================
    // Target schools
    // =========================================================

    private static final ResourceLocation GEO =
            id("gtbcs_geomancy_plus", "geo");

    private static final ResourceLocation OCCULT =
            id("discerning_the_eldritch", "ritual");

    private static final ResourceLocation ENDER =
            id("irons_spellbooks", "ender");

    private static final ResourceLocation HYDRO =
            id("aces_spell_utils", "hydro");

    private static final ResourceLocation WIND =
            id("wind_spellbooks", "wind");

    private static final ResourceLocation NATURE =
            id("irons_spellbooks", "nature");

    private static final ResourceLocation ICE =
            id("irons_spellbooks", "ice");

    private static final ResourceLocation TECHNOMANCY =
            id("cataclysm_spellbooks", "technomancy");

    private static final ResourceLocation RADIANCE =
            id("hazentouvelib", "radiance");

    private static final ResourceLocation ABYSSAL =
            id("cataclysm_spellbooks", "abyssal");

    private static final ResourceLocation COSMIC =
            id("hazentouvelib", "cosmic");

    private static final ResourceLocation SPELLBLADE =
            id("ess_requiem", "blade");

    private static final ResourceLocation SPIRIT =
            id("iss_magicfromtheeast", "spirit");


    // =========================================================
    // Policy data
    // =========================================================

    private static final Map<String, ResourceLocation> SCHOOL_OVERRIDES =
            new HashMap<>();

    private static final Set<String> DISABLED_SPELLS =
            new HashSet<>();

    private static final Set<ResourceLocation> HIDDEN_SCHOOLS =
            new HashSet<>();


    static {

        hideSchools(
                // Retired / merged schools
                "irons_spellbooks:blood",
                "cataclysm_spellbooks:sand",
                "somakespells:aqua",
                "iss_magicfromtheeast:symmetry",
                "hazentouvelib:shadow",
                "crystal_chronicles:prismatic",
                "asterismarcanum:astral",
                "legendary_spellbooks:annihilation",

                // Empty duplicate / legacy schools
                "iss_magicfromtheeast:dune",
                "aces_spell_utils:ritual",
                "aces_spell_utils:technomancy"
        );

        // =====================================================
        // Geo
        // =====================================================

        move(
                GEO,

                "cataclysm_spellbooks:summon_koboleton",
                "cataclysm_spellbooks:thoths_witness",
                "cataclysm_spellbooks:conjure_koboldiator",

                "traveloptics:stele_cascade",

                // Sand -> Geo
                "cataclysm_spellbooks:pharaohs_wrath",
                "cataclysm_spellbooks:monolith_crash",
                "cataclysm_spellbooks:sandstorm",
                "cataclysm_spellbooks:desert_winds",

                // Already redirected by another addon.
                // Mago policy makes the result explicit.
                "irons_spellbooks:earthquake",
                "irons_spellbooks:stomp"
        );


        // =====================================================
        // Occult
        //
        // Actual registry ID:
        // discerning_the_eldritch:ritual
        // =====================================================

        move(
                OCCULT,

                // Blood -> Occult
                "ess_requiem:field_of_mourning",
                "ess_requiem:wretch",
                "ess_requiem:boiling_blood",
                "ess_requiem:necrotic_burst",
                "ess_requiem:maggot_burst",
                "ess_requiem:decaying_will",
                "ess_requiem:corpse_explosion",
                "ess_requiem:finality_of_decay",
                "ess_requiem:strain",

                "irons_spellbooks:acupuncture",
                "irons_spellbooks:blood_needles",
                "irons_spellbooks:blood_slash",
                "irons_spellbooks:blood_step",
                "irons_spellbooks:devour",
                "irons_spellbooks:heartstop",
                "irons_spellbooks:raise_dead",
                "irons_spellbooks:ray_of_siphoning",
                "irons_spellbooks:sacrifice",

                "gametechbcs_spellbooks:crimson_downpour",

                "somakespells:cursed_connection",
                "somakespells:blood_rush",
                "somakespells:blood_cut",
                "somakespells:bloody_legacy",
                "somakespells:bloodmark",
                "somakespells:fragmented_requiem",
                "somakespells:rose_secret",

                "hazennstuff:violent_regurgitation",
                "hazennstuff:bone_bolt",

                "traveloptics:blood_howl",

                "ess_requiem:rip_and_tear",

                "apprenticecodex:higanbana",

                "discerning_the_eldritch:vein_ripper",

                "gogspells:summon_mummy",

                "ess_requiem:summon_skulls",
                "ess_requiem:reaper",
                "ess_requiem:pact_of_the_dead",

                "legendary_spellbooks:hematite_trishula",
                "legendary_spellbooks:possessed_wing",

                // Shadow -> Occult
                "irons_spellbooks:wither_skull",

                // Nature -> Occult
                "hazennstuff:shard_sword",
                "hazennstuff:death_sentence",

                // Summon -> Occult
                "gogspells:summon_witch",

                // Internal Blood spells.
                // Blood school must become empty after consolidation.
                "ess_requiem:arm_of_decay_insta_raise_strong",
                "ess_requiem:arm_of_decay_insta_raise_weak"
        );


        // =====================================================
        // Ender
        // =====================================================

        move(
                ENDER,

                // Shadow -> Ender
                "hazennstuff:arcane_cards",

                // Eldritch -> Ender
                "irons_spellbooks:telekinesis"
        );


        // =====================================================
        // Hydro
        // =====================================================

        move(
                HYDRO,

                // Aqua -> Hydro
                "somakespells:water_ball",
                "somakespells:water_control",
                "somakespells:submerge",
                "somakespells:tidal_grasp",
                "somakespells:storm_aura",
                "somakespells:tsunami",
                "somakespells:tidal_dash",
                "somakespells:thunder_cloud",
                "somakespells:hydro_slash",
                "somakespells:chain_connection",
                "somakespells:sea_serpent",
                "somakespells:sea_serpent_jet",
                "somakespells:water_spear",

                // Blood -> Hydro
                "apprenticecodex:mist_form",

                // Nature -> Hydro
                "apprenticecodex:graced_rain"
        );


        // =====================================================
        // Wind
        // =====================================================

        move(
                WIND,

                // Ender -> Wind
                "apprenticecodex:assist_wings",
                "apprenticecodex:mantis_leap",

                // Eldritch -> Wind
                "apprenticecodex:spectral_wing",

                // Lightning -> Wind
                "legendary_spellbooks:cloud_rail",
                "legendary_spellbooks:cloud_ring",

                // Symmetry -> Wind
                "iss_magicfromtheeast:cloud_ride"
        );


        // =====================================================
        // Nature
        // =====================================================

        move(
                NATURE,

                // Radiance -> Nature
                "hazennstuff:syringe_barrage"
        );


        // =====================================================
        // Ice
        // =====================================================

        move(
                ICE,

                // Symmetry -> Ice
                "somakespells:halberd_strike",
                "somakespells:axe_cleave",
                "somakespells:soul_grab"
        );


        // =====================================================
        // Technomancy
        // =====================================================

        move(
                TECHNOMANCY,

                "traveloptics:rapid_laser",
                "traveloptics:death_laser",
                "traveloptics:em_pulse",

                "apprenticecodex:commence_fire",
                "apprenticecodex:quick_arms",
                "apprenticecodex:breaching_enemy",
                "apprenticecodex:bullet_stream",
                "apprenticecodex:fly_swatter",

                // Summon -> Technomancy
                "apprenticecodex:silent_assassin"
        );


        // =====================================================
        // Radiance
        //
        // Prismatic is retired and merged into Radiance.
        // Radiance retains its Upgrade Orb / Rune growth system.
        // =====================================================

        move(
                RADIANCE,

                // Existing Radiance spells
                "hazennstuff:terraprismic_barrage",
                "hazennstuff:call_forth_terraprisma",
                "hazennstuff:prismatic_shift",

                // Summon -> Radiance
                "hazennstuff:spectral_axe",

                // Astral -> Radiance
                "asterismarcanum:silvery_barbs",
                "asterismarcanum:brightburst",
                "asterismarcanum:celestial_tether",
                "asterismarcanum:luminous_beam",
                "asterismarcanum:astral_echo",
                "asterismarcanum:piercing_light",

                // Holy -> Radiance
                // GTBC version retained.
                "gametechbcs_spellbooks:nullflare"
        );


        // =====================================================
        // Abyssal
        //
        // Abyssal Blast is intentionally NOT included.
        // User already resolved that duplicate separately.
        // =====================================================

        move(
                ABYSSAL,

                // Eldritch -> Abyssal
                "irons_spellbooks:abyssal_shroud",
                "ess_requiem:twilight_assault",
                "ess_requiem:pale_flame",
                "ess_requiem:nights_cover",
                "ess_requiem:protection_of_the_fallen",

                // Shadow -> Abyssal
                "hazennstuff:nights_edge_strike",
                "hazennstuff:umbrashift_barrage",
                "hazennstuff:shadow_reaver"
        );


        // =====================================================
        // Cosmic
        // =====================================================

        move(
                COSMIC,

                // Already redirected by another addon.
                "irons_spellbooks:starfall",
                "irons_spellbooks:black_hole",

                // Eldritch -> Cosmic
                "apprenticecodex:moon_light",
                "irons_spellbooks:pocket_dimension",

                // Astral -> Cosmic
                "asterismarcanum:starfire",
                "asterismarcanum:star_swarm",
                "asterismarcanum:summon_lunar_moths",
                "asterismarcanum:starcutter",

                // Astral-wide consolidation.
                "asterismarcanum:astral_gateway"
        );


        // =====================================================
        // Spellblade
        // =====================================================

        move(
                SPELLBLADE,

                // Eldritch -> Spellblade
                "ess_requiem:ebony_armor",
                "ess_requiem:ebony_cataphract",

                // Symmetry -> Spellblade
                "iss_magicfromtheeast:sword_dance"
        );


        // =====================================================
        // Spirit
        //
        // Soul school is NOT created.
        // Former Soul targets are folded into Spirit.
        // =====================================================

        move(
                SPIRIT,

                // Spellblade -> Spirit
                "ess_requiem:undying_dread",

                // Summon -> Spirit
                "legendary_spellbooks:possessed_soul_blade",
                "legendary_spellbooks:collapsed_kingdoms_legion",

                // Symmetry -> Spirit
                "iss_magicfromtheeast:nephrite_slash",
                "iss_magicfromtheeast:jade_bullet",
                "somakespells:phantom_barrage",
                "somakespells:mirror_strike",
                "somakespells:render_rush",
                "somakespells:desert_wrath",
                "iss_magicfromtheeast:bagua_array_circle",
                "iss_magicfromtheeast:dragon_glide",
                "iss_magicfromtheeast:jade_judgement",
                "iss_magicfromtheeast:underworld_aid",
                "iss_magicfromtheeast:drapes_of_reflection",
                "iss_magicfromtheeast:punishing_heaven",

                // Previously planned for Soul.
                // Soul school abandoned -> Spirit.
                "hazennstuff:soul_seekers",
                "gametechbcs_spellbooks:psychic_bolt"
        );


        // =====================================================
        // Disabled / deleted spells
        // =====================================================

        disable(
                // Retired Prismatic
                "crystal_chronicles:prismatic_portal",

                // Occult
                "discerning_the_eldritch:call_ascended_one",

                // Functional duplicate
                "discerning_the_eldritch:guardians_gaze",

                // Duplicate / boss summon
                "gametechbcs_spellbooks:ashen_breath",
                "gametechbcs_spellbooks:call_forth_the_dead_king",

                // Summon
                "hazennstuff:parry",
                "irons_spellbooks:wololo",

                // Symmetry
                "iss_magicfromtheeast:jiangshi_invoke",
                "somakespells:symmetry_empowerment",

                // Lightning duplicates
                "legendary_spellbooks:tornado",
                "legendary_spellbooks:quad_tornado",

                // Annihilation school - entire school deleted
                "legendary_spellbooks:annihilation_arrow",
                "legendary_spellbooks:annihilation_beam",
                "legendary_spellbooks:annihilation_bomb",
                "legendary_spellbooks:annihilation_geyser",
                "legendary_spellbooks:annihilation_resonance",
                "legendary_spellbooks:annihilation_shockwave",
                "legendary_spellbooks:flameborn_drift",
                "legendary_spellbooks:release_riftwalker_predator",
                "legendary_spellbooks:summon_flameborn_knights",

                // Misc delete
                "somakespells:pumpkin_bomb",
                "somakespells:summon_zombie",

                // T.O duplicates / crash spells
                "traveloptics:aerial_collapse",
                "traveloptics:ashen_breath",
                "traveloptics:astral_sense",
                "traveloptics:axe_of_the_doomed",
                "traveloptics:blackout",
                "traveloptics:cursed_revenants",
                "traveloptics:eternal_sentinel",
                "traveloptics:ignited_onslaught",
                "traveloptics:lingering_strain",
                "traveloptics:mechanized_predator",
                "traveloptics:psychic_bolt",
                "traveloptics:reversal",
                "traveloptics:spectral_blink",
                "traveloptics:summon_desert_dwellers",
                "traveloptics:sword_of_the_ancients",

                // Nullflare duplicate:
                // GTBC kept and moved to Radiance.
                "traveloptics:nullflare"
        );
    }


    private MagoSpellPolicy() {
    }


    public static ResourceLocation getTargetSchool(
            String spellId
    ) {
        return SCHOOL_OVERRIDES.get(spellId);
    }


    public static boolean isDisabled(
            String spellId
    ) {
        return DISABLED_SPELLS.contains(spellId);
    }


    public static boolean isHiddenSchool(
            ResourceLocation schoolId
    ) {
        return HIDDEN_SCHOOLS.contains(schoolId);
    }


    public static int getSchoolOverrideCount() {
        return SCHOOL_OVERRIDES.size();
    }


    public static int getDisabledSpellCount() {
        return DISABLED_SPELLS.size();
    }


    private static ResourceLocation id(
            String namespace,
            String path
    ) {
        return ResourceLocation.fromNamespaceAndPath(
                namespace,
                path
        );
    }


    private static void move(
            ResourceLocation targetSchool,
            String... spellIds
    ) {
        for (String spellId : spellIds) {

            ResourceLocation previous =
                    SCHOOL_OVERRIDES.put(
                            spellId,
                            targetSchool
                    );

            if (previous != null
                    && !previous.equals(targetSchool)) {

                throw new IllegalStateException(
                        "Spell school policy conflict for "
                                + spellId
                                + ": "
                                + previous
                                + " -> "
                                + targetSchool
                );
            }
        }
    }


    private static void disable(
            String... spellIds
    ) {
        for (String spellId : spellIds) {
            DISABLED_SPELLS.add(spellId);
        }
    }


    private static void hideSchools(
            String... schoolIds
    ) {
        for (String schoolId : schoolIds) {

            ResourceLocation parsed =
                    ResourceLocation.tryParse(schoolId);

            if (parsed == null) {
                throw new IllegalArgumentException(
                        "Invalid hidden school id: " + schoolId
                );
            }

            HIDDEN_SCHOOLS.add(parsed);
        }
    }
}