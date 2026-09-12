package kr.mago.compat.item;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import java.util.HashSet;
import java.util.Collection;
import java.util.Set;

public final class MagoCurioAttributeEvents {

    // =========================================================
    // Items
    // =========================================================

    private static final ResourceLocation ARCHIVE_OF_ABYSSAL_SECRETS =
            id(
                    "traveloptics",
                    "archive_of_abyssal_secrets"
            );

    private static final ResourceLocation CHRONICLES_OF_THE_FIRELORD =
            id(
                    "traveloptics",
                    "chronicles_of_the_firelord"
            );

    private static final ResourceLocation DESERT_SPELL_BOOK =
            id(
                    "cataclysm_spellbooks",
                    "desert_spell_book"
            );

        private static final ResourceLocation VAMPIRIC_SPELL_BOOK =
        id(
                "irons_spellbooks",
                "cursed_doll_spell_book"
        );

        // =========================================================
        // Somake
        // =========================================================

        private static final ResourceLocation VOIDBOUND_CODEX_3 =
                id(
                        "somakespells",
                        "voidbound_codex_3_spell_book"
                );


    // =========================================================
    // Attributes
    // =========================================================

    private static final ResourceLocation ENDER_SPELL_POWER =
            id(
                    "irons_spellbooks",
                    "ender_spell_power"
            );

    private static final ResourceLocation ELDRITCH_SPELL_POWER =
            id(
                    "irons_spellbooks",
                    "eldritch_spell_power"
            );

    private static final ResourceLocation FIRE_SPELL_POWER =
            id(
                    "irons_spellbooks",
                    "fire_spell_power"
            );

    private static final ResourceLocation NATURE_SPELL_POWER =
            id(
                    "irons_spellbooks",
                    "nature_spell_power"
            );

    private static final ResourceLocation HOLY_SPELL_POWER =
            id(
                    "irons_spellbooks",
                    "holy_spell_power"
            );

    private static final ResourceLocation ABYSSAL_SPELL_POWER =
            id(
                    "cataclysm_spellbooks",
                    "abyssal_spell_power"
            );

    private static final ResourceLocation GEO_SPELL_POWER =
            id(
                    "gtbcs_geomancy_plus",
                    "geo_spell_power"
            );

        private static final ResourceLocation BLOOD_SPELL_POWER =
        id(
                "irons_spellbooks",
                "blood_spell_power"
        );

        private static final ResourceLocation RITUAL_SPELL_POWER =
                id(
                        "aces_spell_utils",
                        "ritual_spell_power"
                );

        private static final ResourceLocation AQUA_SPELL_POWER =
        id(
                "somakespells",
                "aqua_spell_power"
        );

        private static final ResourceLocation HYDRO_SPELL_POWER =
                id(
                        "aces_spell_utils",
                        "hydro_spell_power"
                );


    // =========================================================
    // Mago modifier IDs
    // =========================================================

    private static final ResourceLocation ARCHIVE_ABYSSAL_MODIFIER =
            id(
                    MagoCompat.MOD_ID,
                    "archive_of_abyssal_secrets_abyssal_spell_power"
            );

    private static final ResourceLocation FIRELORD_FIRE_MODIFIER =
            id(
                    MagoCompat.MOD_ID,
                    "chronicles_of_the_firelord_fire_spell_power"
            );

    private static final ResourceLocation DESERT_GEO_MODIFIER =
            id(
                    MagoCompat.MOD_ID,
                    "desert_spell_book_geo_spell_power"
            );

        private static final ResourceLocation VAMPIRIC_BOOK_RITUAL_MODIFIER =
        id(
                MagoCompat.MOD_ID,
                "vampiric_spell_book_ritual_spell_power"
        );


    // =========================================================
    // Logging
    // =========================================================

    private static boolean loggedArchivePatch = false;
    private static boolean loggedFirelordPatch = false;
    private static boolean loggedDesertSpellBookPatch = false;
        private static boolean loggedVampiricSpellBookPatch = false;
        private static final Set<ResourceLocation> LOGGED_SOMAKE_CURIO_ITEMS =
        new HashSet<>();

    private MagoCurioAttributeEvents() {
    }


    public static void register() {
        NeoForge.EVENT_BUS.addListener(
                MagoCurioAttributeEvents::onCurioAttributeModifiers
        );
    }


    private static void onCurioAttributeModifiers(
            CurioAttributeModifierEvent event
    ) {
        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(
                        event.getItemStack().getItem()
                );

        if ("somakespells".equals(itemId.getNamespace())) {
        patchSomakeCurioAttributes(
                event,
                itemId
        );

        return;
        }

        if (ARCHIVE_OF_ABYSSAL_SECRETS.equals(itemId)) {
            patchArchiveOfAbyssalSecrets(event);
            return;
        }

        if (CHRONICLES_OF_THE_FIRELORD.equals(itemId)) {
            patchChroniclesOfTheFirelord(event);
            return;
        }

        if (DESERT_SPELL_BOOK.equals(itemId)) {
        patchDesertSpellBook(event);
        return;
        }

        if (VAMPIRIC_SPELL_BOOK.equals(itemId)) {
        patchVampiricSpellBook(event);
        }
    }


    /**
     * Archive Of Abyssal Secrets
     *
     * Old:
     * +15% Ender Spell Power
     * +15% Eldritch Spell Power
     *
     * Mago:
     * +30% Abyssal Spell Power
     */
    private static void patchArchiveOfAbyssalSecrets(
            CurioAttributeModifierEvent event
    ) {
        Holder<Attribute> ender =
                requireAttribute(
                        ENDER_SPELL_POWER
                );

        Holder<Attribute> eldritch =
                requireAttribute(
                        ELDRITCH_SPELL_POWER
                );

        Holder<Attribute> abyssal =
                requireAttribute(
                        ABYSSAL_SPELL_POWER
                );

        event.removeAttribute(ender);
        event.removeAttribute(eldritch);

        event.addModifier(
                abyssal,
                new AttributeModifier(
                        ARCHIVE_ABYSSAL_MODIFIER,
                        0.30D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );

        if (!loggedArchivePatch) {
            loggedArchivePatch = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Archive Of Abyssal Secrets patched. "
                            + "Removed Ender/Eldritch Spell Power | "
                            + "Added Abyssal Spell Power +30%"
            );
        }
    }


    /**
     * Chronicles Of The Firelord
     *
     * Mago:
     * +30% Fire Spell Power
     */
    private static void patchChroniclesOfTheFirelord(
            CurioAttributeModifierEvent event
    ) {
        Holder<Attribute> fire =
                requireAttribute(
                        FIRE_SPELL_POWER
                );

        Holder<Attribute> eldritch =
                requireAttribute(
                        ELDRITCH_SPELL_POWER
                );

        event.removeAttribute(fire);
        event.removeAttribute(eldritch);

        event.addModifier(
                fire,
                new AttributeModifier(
                        FIRELORD_FIRE_MODIFIER,
                        0.30D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );

        if (!loggedFirelordPatch) {
            loggedFirelordPatch = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Chronicles Of The Firelord patched. "
                            + "Removed original Fire/Eldritch Spell Power | "
                            + "Added Fire Spell Power +30%"
            );
        }
    }


    /**
     * Desert Spellbook
     *
     * Old:
     * +30% Nature Spell Power
     * +20% Holy Spell Power
     * +300 Max Mana
     *
     * Mago:
     * +30% Geo Spell Power
     * +300 Max Mana remains untouched
     */
    private static void patchDesertSpellBook(
            CurioAttributeModifierEvent event
    ) {
        Holder<Attribute> nature =
                requireAttribute(
                        NATURE_SPELL_POWER
                );

        Holder<Attribute> holy =
                requireAttribute(
                        HOLY_SPELL_POWER
                );

        Holder<Attribute> geo =
                requireAttribute(
                        GEO_SPELL_POWER
                );

        event.removeAttribute(nature);
        event.removeAttribute(holy);

        event.addModifier(
                geo,
                new AttributeModifier(
                        DESERT_GEO_MODIFIER,
                        0.30D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );

        if (!loggedDesertSpellBookPatch) {
            loggedDesertSpellBookPatch = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Desert Spellbook patched. "
                            + "Removed Nature/Holy Spell Power | "
                            + "Added Geo Spell Power +30%"
            );
        }
    }

        /**
         * Vampiric Spell Book
         *
         * Old:
         * +10% Blood Spell Power
         * +10% Spell Resist
         * +200 Max Mana
         *
         * Mago:
         * +10% Occult / Ritual Spell Power
         * +10% Spell Resist remains
         * +200 Max Mana remains
         */
        private static void patchVampiricSpellBook(
                CurioAttributeModifierEvent event
        ) {
        Holder<Attribute> blood =
                requireAttribute(
                        BLOOD_SPELL_POWER
                );

        Holder<Attribute> ritual =
                requireAttribute(
                        RITUAL_SPELL_POWER
                );

        event.removeAttribute(blood);

        event.addModifier(
                ritual,
                new AttributeModifier(
                        VAMPIRIC_BOOK_RITUAL_MODIFIER,
                        0.10D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );

        if (!loggedVampiricSpellBookPatch) {
                loggedVampiricSpellBookPatch = true;

                MagoCompat.LOGGER.info(
                        "[MagoCompat] Vampiric Spell Book patched. "
                                + "Removed Blood Spell Power | "
                                + "Added Occult/Ritual Spell Power +10%"
                );
        }
        }

        private static void patchSomakeCurioAttributes(
        CurioAttributeModifierEvent event,
        ResourceLocation itemId
) {
    int bloodMigrated =
            migrateCurioAttribute(
                    event,
                    BLOOD_SPELL_POWER,
                    RITUAL_SPELL_POWER
            );

    int aquaMigrated =
            migrateCurioAttribute(
                    event,
                    AQUA_SPELL_POWER,
                    HYDRO_SPELL_POWER
            );

    int enderMigrated = 0;

    if (VOIDBOUND_CODEX_3.equals(itemId)) {
        enderMigrated =
                migrateCurioAttribute(
                        event,
                        ENDER_SPELL_POWER,
                        ABYSSAL_SPELL_POWER
                );
    }

    if (bloodMigrated == 0
            && aquaMigrated == 0
            && enderMigrated == 0) {
        return;
    }

    if (LOGGED_SOMAKE_CURIO_ITEMS.add(itemId)) {
        MagoCompat.LOGGER.info(
                "[MagoCompat] Somake Curio patch active. "
                        + "Item: {} | Blood->Occult: {} | "
                        + "Aqua->Hydro: {} | Ender->Abyssal: {}",
                itemId,
                bloodMigrated,
                aquaMigrated,
                enderMigrated
        );
    }
}


        private static int migrateCurioAttribute(
                CurioAttributeModifierEvent event,
                ResourceLocation sourceAttributeId,
                ResourceLocation targetAttributeId
        ) {
        Holder<Attribute> source =
                requireAttribute(
                        sourceAttributeId
                );

        Holder<Attribute> target =
                requireAttribute(
                        targetAttributeId
                );

        /*
        * Curios 9.5.1 API:
        *
        * removeAttribute() returns the modifiers which were removed.
        * We can therefore migrate the actual modifier values without
        * guessing IDs, amounts or operations.
        */
        Collection<AttributeModifier> removedModifiers =
                event.removeAttribute(
                        source
                );

        if (removedModifiers.isEmpty()) {
                return 0;
        }

        int migrated = 0;

        for (AttributeModifier oldModifier : removedModifiers) {
                event.addModifier(
                        target,
                        new AttributeModifier(
                                oldModifier.id(),
                                oldModifier.amount(),
                                oldModifier.operation()
                        )
                );

                migrated++;
        }

        return migrated;
        }

    private static Holder<Attribute> requireAttribute(
            ResourceLocation attributeId
    ) {
        Attribute attribute =
                BuiltInRegistries.ATTRIBUTE.get(
                        attributeId
                );

        if (attribute == null) {
            throw new IllegalStateException(
                    "[MagoCompat] Missing required attribute: "
                            + attributeId
            );
        }

        return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                attribute
        );
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
}