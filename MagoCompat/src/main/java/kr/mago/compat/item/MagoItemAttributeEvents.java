package kr.mago.compat.item;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MagoItemAttributeEvents {

    // =========================================================
    // Cataclysm Spellbooks items
    // =========================================================

    private static final ResourceLocation PHARAOH_HELMET =
            id(
                    "cataclysm_spellbooks",
                    "pharaoh_helmet"
            );

    private static final ResourceLocation PHARAOH_CHESTPLATE =
            id(
                    "cataclysm_spellbooks",
                    "pharaoh_chestplate"
            );

    private static final ResourceLocation PHARAOH_LEGGINGS =
            id(
                    "cataclysm_spellbooks",
                    "pharaoh_leggings"
            );

    private static final ResourceLocation PHARAOH_GREAVES =
            id(
                    "cataclysm_spellbooks",
                    "pharaoh_greaves"
            );

    /*
     * Yes, the original registry ID really is "wudjets".
     */
    private static final ResourceLocation FAKE_WUDJETS_STAFF =
            id(
                    "cataclysm_spellbooks",
                    "fake_wudjets_staff"
            );


    // =========================================================
    // L_Ender's Cataclysm items
    // =========================================================

    private static final ResourceLocation CURSIUM_HELMET =
            id(
                    "cataclysm",
                    "cursium_helmet"
            );

    private static final ResourceLocation CURSIUM_CHESTPLATE =
            id(
                    "cataclysm",
                    "cursium_chestplate"
            );

    private static final ResourceLocation CURSIUM_LEGGINGS =
            id(
                    "cataclysm",
                    "cursium_leggings"
            );

    private static final ResourceLocation CURSIUM_BOOTS =
            id(
                    "cataclysm",
                    "cursium_boots"
            );

        private static final ResourceLocation MURAMASA =
        id(
                "iss_magicfromtheeast",
                "muramasa"
        );

        // =========================================================
        // Discerning The Eldritch
        // =========================================================

        private static final ResourceLocation DTE_CATACLYSM =
                id(
                        "discerning_the_eldritch",
                        "cataclysm"
                );

        private static final ResourceLocation DTE_DEVOURER =
                id(
                        "discerning_the_eldritch",
                        "devourer"
                );

        private static final ResourceLocation DTE_MOURNING_STAR =
                id(
                        "discerning_the_eldritch",
                        "mourning_star"
                );

        private static final ResourceLocation DTE_CATACLYSM_AWAKENED =
                id(
                        "discerning_the_eldritch",
                        "cataclysm_awakened"
                );

        private static final ResourceLocation DTE_DEVOURER_AWAKENED =
                id(
                        "discerning_the_eldritch",
                        "devourer_awakened"
                );

        private static final ResourceLocation DTE_MOURNING_STAR_AWAKENED =
                id(
                        "discerning_the_eldritch",
                        "mourning_star_awakened"
                );

        // =========================================================
        // Hazen N Stuff
        // =========================================================

        private static final ResourceLocation VAMPIRE_KNIVES =
                id(
                        "hazennstuff",
                        "vampire_knives"
                );

        private static final ResourceLocation BLOOD_STAFF =
                id(
                        "irons_spellbooks",
                        "blood_staff"
                );

        // =========================================================
        // Crystal Chronicles
        // =========================================================

        private static final ResourceLocation SANGUINE_EDGE =
                id(
                        "crystal_chronicles",
                        "blood_scythe"
                );

        // =========================================================
        // Somake
        // =========================================================

        private static final ResourceLocation ABYSSIUM_HELMET =
                id(
                        "somakespells",
                        "abyssium_helmet"
                );

        private static final ResourceLocation ABYSSIUM_CHESTPLATE =
                id(
                        "somakespells",
                        "abyssium_chestplate"
                );

        private static final ResourceLocation ABYSSIUM_LEGGINGS =
                id(
                        "somakespells",
                        "abyssium_leggings"
                );

        private static final ResourceLocation ABYSSIUM_BOOTS =
                id(
                        "somakespells",
                        "abyssium_boots"
                );

        private static final ResourceLocation SHADOW_SCALE_HELMET =
                id(
                        "hazennstuff",
                        "shadow_scale_helmet"
                );

        private static final ResourceLocation SHADOW_SCALE_CHESTPLATE =
                id(
                        "hazennstuff",
                        "shadow_scale_chestplate"
                );

        private static final ResourceLocation SHADOW_SCALE_LEGGINGS =
                id(
                        "hazennstuff",
                        "shadow_scale_leggings"
                );

        private static final ResourceLocation SHADOW_SCALE_BOOTS =
                id(
                        "hazennstuff",
                        "shadow_scale_boots"
                );

        private static final ResourceLocation SHADOW_JESTER_HELMET =
                id(
                        "hazennstuff",
                        "shadow_jester_helmet"
                );

        private static final ResourceLocation SHADOW_JESTER_CHESTPLATE =
                id(
                        "hazennstuff",
                        "shadow_jester_chestplate"
                );

        private static final ResourceLocation SHADOW_JESTER_LEGGINGS =
                id(
                        "hazennstuff",
                        "shadow_jester_leggings"
                );

        private static final ResourceLocation SHADOW_JESTER_BOOTS =
                id(
                        "hazennstuff",
                        "shadow_jester_boots"
                );


    // =========================================================
    // Attributes
    // =========================================================

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

    private static final ResourceLocation GEO_SPELL_POWER =
            id(
                    "gtbcs_geomancy_plus",
                    "geo_spell_power"
            );

    private static final ResourceLocation SYMMETRY_SPELL_POWER =
            id(
                    "iss_magicfromtheeast",
                    "symmetry_spell_power"
            );

    private static final ResourceLocation SPIRIT_SPELL_POWER =
            id(
                    "iss_magicfromtheeast",
                    "spirit_spell_power"
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

        private static final ResourceLocation ENDER_SPELL_POWER =
                id(
                        "irons_spellbooks",
                        "ender_spell_power"
                );

        private static final ResourceLocation ABYSSAL_SPELL_POWER =
                id(
                        "cataclysm_spellbooks",
                        "abyssal_spell_power"
                );

        private static final ResourceLocation SHADOW_SPELL_POWER =
        id(
                "hazentouvelib",
                "shadow_spell_power"
        );

    // =========================================================
    // Logging
    // =========================================================

    private static final Set<ResourceLocation> LOGGED_ITEMS =
            new HashSet<>();


    private MagoItemAttributeEvents() {
    }


    public static void register() {
        /*
         * Run after normal-priority compatibility listeners.
         *
         * This matters for Cursium because its magic school bonuses
         * are added by compatibility layers rather than the base
         * Cataclysm armor itself.
         */
        NeoForge.EVENT_BUS.addListener(
                EventPriority.LOWEST,
                MagoItemAttributeEvents::onItemAttributeModifiers
        );
    }


    private static void onItemAttributeModifiers(
            ItemAttributeModifierEvent event
    ) {
        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(
                        event.getItemStack().getItem()
                );

        // ---------------------------------------------------------
        // Cataclysm Spellbooks
        // ---------------------------------------------------------

        if (PHARAOH_HELMET.equals(itemId)) {
            patchPharaohArmor(
                    event,
                    itemId,
                    EquipmentSlot.HEAD
            );
            return;
        }

        if (PHARAOH_CHESTPLATE.equals(itemId)) {
            patchPharaohArmor(
                    event,
                    itemId,
                    EquipmentSlot.CHEST
            );
            return;
        }

        if (PHARAOH_LEGGINGS.equals(itemId)) {
            patchPharaohArmor(
                    event,
                    itemId,
                    EquipmentSlot.LEGS
            );
            return;
        }

        if (PHARAOH_GREAVES.equals(itemId)) {
            patchPharaohArmor(
                    event,
                    itemId,
                    EquipmentSlot.FEET
            );
            return;
        }

        if (FAKE_WUDJETS_STAFF.equals(itemId)) {
        patchFakeWudjetsStaff(
                event,
                itemId
        );
        return;
        }


        // ---------------------------------------------------------
        // ISS: Magic From The East
        // ---------------------------------------------------------

        // ---------------------------------------------------------
        // Blood -> Occult weapon migrations
        // ---------------------------------------------------------

        if (MURAMASA.equals(itemId)
                || ROTTEN_SICKLE.equals(itemId)
                || WHISPERING_HARVESTER.equals(itemId)
                || SCYTHE_OF_ROTTEN_DREAMS.equals(itemId)
                || ARM_OF_DECAY.equals(itemId)
                || VAMPIRE_KNIVES.equals(itemId)
                || DTE_CATACLYSM.equals(itemId)
                || DTE_DEVOURER.equals(itemId)
                || DTE_MOURNING_STAR.equals(itemId)
                || DTE_CATACLYSM_AWAKENED.equals(itemId)
                || DTE_DEVOURER_AWAKENED.equals(itemId)
                || DTE_MOURNING_STAR_AWAKENED.equals(itemId)
                || BLOOD_STAFF.equals(itemId)
                || SANGUINE_EDGE.equals(itemId)
                ) {

        patchBloodWeaponToRitual(
                event,
                itemId
        );

        return;
        }

        // ---------------------------------------------------------
        // Somake school consolidation
        // ---------------------------------------------------------

        if ("somakespells".equals(itemId.getNamespace())) {

        int bloodMigrated =
                migrateItemAttribute(
                        event,
                        BLOOD_SPELL_POWER,
                        RITUAL_SPELL_POWER
                );

        int aquaMigrated =
                migrateItemAttribute(
                        event,
                        AQUA_SPELL_POWER,
                        HYDRO_SPELL_POWER
                );

        int enderMigrated = 0;

        if (isAbyssiumArmor(itemId)) {
                enderMigrated =
                        migrateItemAttribute(
                                event,
                                ENDER_SPELL_POWER,
                                ABYSSAL_SPELL_POWER
                        );
        }

        if (bloodMigrated > 0
                || aquaMigrated > 0
                || enderMigrated > 0) {

                logPatchOnce(
                        itemId,
                        "Somake school migration | "
                                + "Blood->Occult: "
                                + bloodMigrated
                                + " | Aqua->Hydro: "
                                + aquaMigrated
                                + " | Ender->Abyssal: "
                                + enderMigrated
                );
        }

        return;
        }

        // ---------------------------------------------------------
        // Hazen Shadow -> Abyssal armor migrations
        // ---------------------------------------------------------

        if (SHADOW_SCALE_HELMET.equals(itemId)
                || SHADOW_SCALE_CHESTPLATE.equals(itemId)
                || SHADOW_SCALE_LEGGINGS.equals(itemId)
                || SHADOW_SCALE_BOOTS.equals(itemId)

                || SHADOW_JESTER_HELMET.equals(itemId)
                || SHADOW_JESTER_CHESTPLATE.equals(itemId)
                || SHADOW_JESTER_LEGGINGS.equals(itemId)
                || SHADOW_JESTER_BOOTS.equals(itemId)) {

        patchShadowItemToAbyssal(
                event,
                itemId
        );

        return;
        }

        // ---------------------------------------------------------
        // L_Ender's Cataclysm
        // ---------------------------------------------------------
                
        if (CURSIUM_HELMET.equals(itemId)
                || CURSIUM_CHESTPLATE.equals(itemId)
                || CURSIUM_LEGGINGS.equals(itemId)
                || CURSIUM_BOOTS.equals(itemId)) {

            patchCursiumArmor(
                    event,
                    itemId
            );
        }
    }


    // =========================================================
    // Cataclysm Spellbooks
    // =========================================================

    /**
     * Pharaoh armor
     *
     * Original per piece:
     * +150 Max Mana
     * +20% Nature Spell Power
     * +20% Holy Spell Power
     * +5% Generic Spell Power
     *
     * Mago:
     * +150 Max Mana
     * +20% Geo Spell Power
     * +5% Generic Spell Power
     */
    private static void patchPharaohArmor(
            ItemAttributeModifierEvent event,
            ResourceLocation itemId,
            EquipmentSlot equipmentSlot
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

        String slotName =
                equipmentSlot.getName();

        ResourceLocation originalNatureModifier =
                id(
                        "irons_spellbooks",
                        slotName
                                + "_nature_spell_power_modifier"
                );

        ResourceLocation originalHolyModifier =
                id(
                        "irons_spellbooks",
                        slotName
                                + "_holy_spell_power_modifier"
                );

        event.removeModifier(
                nature,
                originalNatureModifier
        );

        event.removeModifier(
                holy,
                originalHolyModifier
        );

        EquipmentSlotGroup slotGroup =
                EquipmentSlotGroup.bySlot(
                        equipmentSlot
                );

        ResourceLocation geoModifierId =
                id(
                        MagoCompat.MOD_ID,
                        itemId.getPath()
                                + "_geo_spell_power"
                );

        event.addModifier(
                geo,
                new AttributeModifier(
                        geoModifierId,
                        0.20D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                slotGroup
        );

        logPatchOnce(
                itemId,
                "Removed base Nature/Holy Spell Power | "
                        + "Added Geo Spell Power +20%"
        );
    }


    /**
     * Fake Wadjet's Staff
     *
     * Original:
     * +10% Nature Spell Power
     * +20% Holy Spell Power
     * +25% Cooldown Reduction
     *
     * Mago:
     * +20% Geo Spell Power
     * +25% Cooldown Reduction
     */
    private static void patchFakeWudjetsStaff(
            ItemAttributeModifierEvent event,
            ResourceLocation itemId
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

        ResourceLocation originalNatureModifier =
                id(
                        "irons_spellbooks",
                        "mainhand_nature_spell_power_modifier"
                );

        ResourceLocation originalHolyModifier =
                id(
                        "irons_spellbooks",
                        "mainhand_holy_spell_power_modifier"
                );

        event.removeModifier(
                nature,
                originalNatureModifier
        );

        event.removeModifier(
                holy,
                originalHolyModifier
        );

        event.addModifier(
                geo,
                new AttributeModifier(
                        id(
                                MagoCompat.MOD_ID,
                                "fake_wudjets_staff_geo_spell_power"
                        ),
                        0.20D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        logPatchOnce(
                itemId,
                "Removed base Nature/Holy Spell Power | "
                        + "Added Geo Spell Power +20%"
        );
    }


    // =========================================================
    // L_Ender's Cataclysm
    // =========================================================

    /**
     * Cursium armor
     *
     * Current compatibility bonuses:
     * +10% Ice Spell Power
     * +10% Symmetry Spell Power
     *
     * Mago:
     * +10% Ice Spell Power remains untouched
     * Symmetry Spell Power -> Spirit Spell Power
     *
     * Rather than guessing the modifier ID supplied by the
     * compatibility mod, migrate the actual Symmetry modifiers
     * present on the final item attribute list.
     */
    private static void patchCursiumArmor(
            ItemAttributeModifierEvent event,
            ResourceLocation itemId
    ) {
        Holder<Attribute> symmetry =
                requireAttribute(
                        SYMMETRY_SPELL_POWER
                );

        Holder<Attribute> spirit =
                requireAttribute(
                        SPIRIT_SPELL_POWER
                );

        /*
         * Take a snapshot first because the event's modifier list
         * changes as modifiers are removed and added.
         */
        List<ItemAttributeModifiers.Entry> symmetryEntries =
                new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
            if (entry.attribute().equals(symmetry)) {
                symmetryEntries.add(entry);
            }
        }

        if (symmetryEntries.isEmpty()) {
            return;
        }

        int migrated = 0;

        for (ItemAttributeModifiers.Entry entry : symmetryEntries) {
            AttributeModifier oldModifier =
                    entry.modifier();

            boolean removed =
                    event.removeModifier(
                            symmetry,
                            oldModifier.id()
                    );

            if (!removed) {
                continue;
            }

            /*
             * Reuse the original amount, operation and slot.
             *
             * Only the target Attribute changes:
             * Symmetry -> Spirit.
             */
            boolean added =
                    event.addModifier(
                            spirit,
                            new AttributeModifier(
                                    oldModifier.id(),
                                    oldModifier.amount(),
                                    oldModifier.operation()
                            ),
                            entry.slot()
                    );

            if (added) {
                migrated++;
            }
        }

        if (migrated > 0) {
            logPatchOnce(
                    itemId,
                    "Migrated "
                            + migrated
                            + " Symmetry Spell Power modifier(s) "
                            + "to Spirit Spell Power"
            );
        }
    }

        /**
         * Muramasa
         *
         * Original:
         * Blood Spell Power
         *
         * Mago:
         * Occult / Ritual Spell Power
         *
         * Preserve the actual amount, operation and slot
         * from every Blood Spell Power entry on Muramasa.
         */
        /**
         * Migrates all Blood Spell Power modifiers on the target weapon
         * to Mago's Occult / Ritual Spell Power.
         *
         * Preserves:
         * - modifier amount
         * - operation
         * - equipment slot
         * - modifier ID
         *
         * Other attributes on the item are untouched.
         *
         * Used by:
         * - ISS: Muramasa
         * - ESS Requiem: Rotten Sickle
         * - ESS Requiem: Whispering Harvester
         * - ESS Requiem: Scythe of Rotten Dreams
         * - ESS Requiem: Arm of Decay
         */
        private static void patchBloodWeaponToRitual(
                ItemAttributeModifierEvent event,
                ResourceLocation itemId
        ) {
        Holder<Attribute> blood =
                requireAttribute(
                        BLOOD_SPELL_POWER
                );

        Holder<Attribute> ritual =
                requireAttribute(
                        RITUAL_SPELL_POWER
                );

        List<ItemAttributeModifiers.Entry> bloodEntries =
                new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
                if (entry.attribute().equals(blood)) {
                bloodEntries.add(entry);
                }
        }

        if (bloodEntries.isEmpty()) {
                return;
        }

        int migrated = 0;

        for (ItemAttributeModifiers.Entry entry : bloodEntries) {
                AttributeModifier oldModifier =
                        entry.modifier();

                boolean removed =
                        event.removeModifier(
                                blood,
                                oldModifier.id()
                        );

                if (!removed) {
                continue;
                }

                boolean added =
                        event.addModifier(
                                ritual,
                                new AttributeModifier(
                                        oldModifier.id(),
                                        oldModifier.amount(),
                                        oldModifier.operation()
                                ),
                                entry.slot()
                        );

                if (added) {
                migrated++;
                }
        }

        if (migrated > 0) {
                logPatchOnce(
                        itemId,
                        "Migrated "
                                + migrated
                                + " Blood Spell Power modifier(s) "
                                + "to Occult/Ritual Spell Power"
                );
        }
        }

        // =========================================================
        // Ender's Spells and Stuff: Requiem
        // =========================================================

        private static final ResourceLocation ROTTEN_SICKLE =
                id(
                        "ess_requiem",
                        "rotten_sickle"
                );

        private static final ResourceLocation WHISPERING_HARVESTER =
                id(
                        "ess_requiem",
                        "whispering_harvester"
                );

        private static final ResourceLocation SCYTHE_OF_ROTTEN_DREAMS =
                id(
                        "ess_requiem",
                        "scythe_of_rotten_dreams"
                );

        private static final ResourceLocation ARM_OF_DECAY =
                id(
                        "ess_requiem",
                        "arm_of_decay"
                );

        /**
         * Migrates every modifier of one Attribute to another
         * while preserving:
         *
         * - modifier ID
         * - amount
         * - operation
         * - equipment slot/group
         */
        private static int migrateItemAttribute(
                ItemAttributeModifierEvent event,
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

        List<ItemAttributeModifiers.Entry> entries =
                new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
                if (entry.attribute().equals(source)) {
                entries.add(entry);
                }
        }

        if (entries.isEmpty()) {
                return 0;
        }

        int migrated = 0;

        for (ItemAttributeModifiers.Entry entry : entries) {
                AttributeModifier oldModifier =
                        entry.modifier();

                boolean removed =
                        event.removeModifier(
                                source,
                                oldModifier.id()
                        );

                if (!removed) {
                continue;
                }

                boolean added =
                        event.addModifier(
                                target,
                                new AttributeModifier(
                                        oldModifier.id(),
                                        oldModifier.amount(),
                                        oldModifier.operation()
                                ),
                                entry.slot()
                        );

                if (added) {
                migrated++;
                }
        }

        return migrated;
        }

        private static boolean isAbyssiumArmor(
                ResourceLocation itemId
        ) {
        return ABYSSIUM_HELMET.equals(itemId)
                || ABYSSIUM_CHESTPLATE.equals(itemId)
                || ABYSSIUM_LEGGINGS.equals(itemId)
                || ABYSSIUM_BOOTS.equals(itemId);
        }

    // =========================================================
    // Helpers
    // =========================================================

    /**
         * Migrates all Shadow Spell Power modifiers on the target item
         * to Mago's Abyssal Spell Power.
         *
         * Preserves:
         * - modifier amount
         * - operation
         * - equipment slot
         * - modifier ID
         *
         * Other attributes remain untouched.
         */
        private static void patchShadowItemToAbyssal(
                ItemAttributeModifierEvent event,
                ResourceLocation itemId
        ) {
        Holder<Attribute> shadow =
                requireAttribute(
                        SHADOW_SPELL_POWER
                );

        Holder<Attribute> abyssal =
                requireAttribute(
                        ABYSSAL_SPELL_POWER
                );

        List<ItemAttributeModifiers.Entry> shadowEntries =
                new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
                if (entry.attribute().equals(shadow)) {
                shadowEntries.add(entry);
                }
        }

        if (shadowEntries.isEmpty()) {
                return;
        }

        int migrated = 0;

        for (ItemAttributeModifiers.Entry entry : shadowEntries) {
                AttributeModifier oldModifier =
                        entry.modifier();

                boolean removed =
                        event.removeModifier(
                                shadow,
                                oldModifier.id()
                        );

                if (!removed) {
                continue;
                }

                boolean added =
                        event.addModifier(
                                abyssal,
                                new AttributeModifier(
                                        oldModifier.id(),
                                        oldModifier.amount(),
                                        oldModifier.operation()
                                ),
                                entry.slot()
                        );

                if (added) {
                migrated++;
                }
        }

        if (migrated > 0) {
                logPatchOnce(
                        itemId,
                        "Migrated "
                                + migrated
                                + " Shadow Spell Power modifier(s) "
                                + "to Abyssal Spell Power"
                );
        }
        }

    private static void logPatchOnce(
            ResourceLocation itemId,
            String message
    ) {
        if (!LOGGED_ITEMS.add(itemId)) {
            return;
        }

        MagoCompat.LOGGER.info(
                "[MagoCompat] Item attribute patch active. "
                        + "Item: {} | {}",
                itemId,
                message
        );
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