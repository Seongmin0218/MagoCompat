package kr.mago.compat.item;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

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


    // =========================================================
    // Logging
    // =========================================================

    private static boolean loggedArchivePatch = false;
    private static boolean loggedFirelordPatch = false;
    private static boolean loggedDesertSpellBookPatch = false;


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