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
 * +15% Blood Spell Power
 *
 * Mago:
 * +15% Occult (Ritual) Spell Power
 *
 * The original amount and operation are preserved rather
 * than hard-coding the value into the migration.
 */
        private static void patchMuramasa(
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

        ResourceLocation originalModifierId =
                id(
                        "irons_spellbooks",
                        "mainhand_blood_spell_power_modifier"
                );

        ItemAttributeModifiers.Entry targetEntry = null;

        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
                if (!entry.attribute().equals(blood)) {
                continue;
                }

                if (!entry.modifier().id().equals(originalModifierId)) {
                continue;
                }

                targetEntry = entry;
                break;
        }

        if (targetEntry == null) {
                return;
        }

        AttributeModifier oldModifier =
                targetEntry.modifier();

        boolean removed =
                event.removeModifier(
                        blood,
                        originalModifierId
                );

        if (!removed) {
                return;
        }

        boolean added =
                event.addModifier(
                        ritual,
                        new AttributeModifier(
                                id(
                                        MagoCompat.MOD_ID,
                                        "muramasa_ritual_spell_power"
                                ),
                                oldModifier.amount(),
                                oldModifier.operation()
                        ),
                        targetEntry.slot()
                );

        if (added) {
                logPatchOnce(
                        itemId,
                        "Migrated Blood Spell Power "
                                + oldModifier.amount()
                                + " to Occult/Ritual Spell Power"
                );
        }
        }


    // =========================================================
    // Helpers
    // =========================================================

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