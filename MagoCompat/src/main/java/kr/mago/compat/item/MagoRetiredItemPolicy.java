package kr.mago.compat.item;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import kr.mago.compat.spell.MagoSpellPolicy;
import java.util.Iterator;
import java.util.Set;

public final class MagoRetiredItemPolicy {

        // =========================================================
        // ISS: Magic From The East
        // =========================================================

        public static final ResourceLocation SYMMETRY_RUNE =
                id(
                        "iss_magicfromtheeast",
                        "symmetry_rune"
                );

        public static final ResourceLocation SYMMETRY_UPGRADE_ORB =
                id(
                        "iss_magicfromtheeast",
                        "symmetry_upgrade_orb"
                );


        // =========================================================
        // Somake
        // =========================================================

        public static final ResourceLocation SYMMETRY_STAFF =
                id(
                        "somakespells",
                        "symmetry_staff"
                );

        // =========================================================
        // HazentouveLib
        // =========================================================

        public static final ResourceLocation SHADOW_RUNE =
                id(
                        "hazentouvelib",
                        "shadow_rune"
                );

        public static final ResourceLocation SHADOW_UPGRADE_ORB =
                id(
                        "hazentouvelib",
                        "shadow_upgrade_orb"
                );

        // =========================================================
        // Iron's Spells 'n Spellbooks - retired Blood progression
        // =========================================================

        public static final ResourceLocation BLOODY_VELLUM =
                id(
                        "irons_spellbooks",
                        "bloody_vellum"
                );

        public static final ResourceLocation BLOOD_RUNE =
                id(
                        "irons_spellbooks",
                        "blood_rune"
                );

        public static final ResourceLocation BLOOD_UPGRADE_ORB =
                id(
                        "irons_spellbooks",
                        "blood_upgrade_orb"
                );

        // =========================================================
        // Somake Aqua progression
        // =========================================================

        public static final ResourceLocation AQUA_RUNE =
                id(
                        "somakespells",
                        "aqua_rune"
                );

        public static final ResourceLocation AQUA_UPGRADE_ORB =
                id(
                        "somakespells",
                        "aqua_upgrade_orb"
                );

        // =========================================================
        // Legendary Spellbooks - retired Annihilation progression
        // =========================================================

        public static final ResourceLocation ANNIHILATORS_PROTOCOL =
                id(
                        "legendary_spellbooks",
                        "annihilators_protocol"
                );

        public static final ResourceLocation ANNIHILATION_RUNE =
                id(
                        "legendary_spellbooks",
                        "annihilation_rune"
                );

        public static final ResourceLocation ANNIHILATION_UPGRADE_ORB =
                id(
                        "legendary_spellbooks",
                        "upgrade_orb_annihilation"
                );

        public static final ResourceLocation OBLIVIONMANCER_HAT =
                id(
                        "legendary_spellbooks",
                        "oblivionmancer_hat"
                );

        public static final ResourceLocation OBLIVIONMANCER_ROBE =
                id(
                        "legendary_spellbooks",
                        "oblivionmancer_robe"
                );

        public static final ResourceLocation OBLIVIONMANCER_LEGGINGS =
                id(
                        "legendary_spellbooks",
                        "oblivionmancer_leggings"
                );

        public static final ResourceLocation OBLIVIONMANCER_BOOTS =
                id(
                        "legendary_spellbooks",
                        "oblivionmancer_boots"
                );


        // =========================================================
        // Hazen - retired item
        // =========================================================

        public static final ResourceLocation ROD_OF_DISCORD =
                id(
                        "hazennstuff",
                        "rod_of_discord"
                );

        // =========================================================
        // Artifacts - retired everlasting food
        // =========================================================

        public static final ResourceLocation EVERLASTING_BEEF =
                id(
                        "artifacts",
                        "everlasting_beef"
                );

        public static final ResourceLocation ETERNAL_STEAK =
                id(
                        "artifacts",
                        "eternal_steak"
                );

    /*
     * The item shown as Taiji Sword is also the mod's staff item.
     *
     * iss_magicfromtheeast:staff tag:
     * - iss_magicfromtheeast:taiji_sword
     */
    public static final ResourceLocation TAIJI_SWORD =
            id(
                    "iss_magicfromtheeast",
                    "taiji_sword"
            );

    public static final ResourceLocation SPIRIT_RUNE =
            id(
                    "iss_magicfromtheeast",
                    "spirit_rune"
            );


    // =========================================================
    // Retired items
    // =========================================================

        private static final Set<ResourceLocation> RETIRED_ITEMS =
                Set.of(
                        SYMMETRY_RUNE,
                        SYMMETRY_UPGRADE_ORB,
                        SYMMETRY_STAFF,
                        SHADOW_RUNE,
                        SHADOW_UPGRADE_ORB,
                        BLOODY_VELLUM,
                        BLOOD_RUNE,
                        BLOOD_UPGRADE_ORB,
                        AQUA_RUNE,
                        AQUA_UPGRADE_ORB,
                        ANNIHILATORS_PROTOCOL,
                        ANNIHILATION_RUNE,
                        ANNIHILATION_UPGRADE_ORB,
                        OBLIVIONMANCER_HAT,
                        OBLIVIONMANCER_ROBE,
                        OBLIVIONMANCER_LEGGINGS,
                        OBLIVIONMANCER_BOOTS,
                        ROD_OF_DISCORD,
                        EVERLASTING_BEEF,
                        ETERNAL_STEAK
                );


    private static boolean loggedSymmetryDropMigration = false;

    private static boolean loggedRetiredDropRemoval = false;
        private static boolean loggedDisabledScrollRemoval = false;


    private MagoRetiredItemPolicy() {
    }


    public static void register(
            IEventBus modEventBus
    ) {
        /*
         * Creative tab construction is a MOD bus event.
         * LOWEST ensures Mago removes entries after normal listeners
         * have populated their tabs.
         */
        modEventBus.addListener(
                EventPriority.LOWEST,
                MagoRetiredItemPolicy::onBuildCreativeContents
        );

        /*
         * Entity drops are NeoForge game bus events.
         */
        NeoForge.EVENT_BUS.addListener(
                MagoRetiredItemPolicy::onLivingDrops
        );
    }


    /**
     * Removes retired items from:
     *
     * - their normal creative tab
     * - creative search results
     */
    private static void onBuildCreativeContents(
            BuildCreativeModeTabContentsEvent event
    ) {
        for (ResourceLocation itemId : RETIRED_ITEMS) {
            Item item =
                    BuiltInRegistries.ITEM.get(
                            itemId
                    );

            if (item == null) {
                continue;
            }

            event.remove(
                    new ItemStack(item),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    /**
     * ISS Taoists normally drop a Symmetry Rune.
     *
     * Since Symmetry is retired in Mago, preserve the reward
     * by converting that drop into a Spirit Rune instead.
     */
    private static void onLivingDrops(
        LivingDropsEvent event
        ) {
        // =========================================================
        // 1. Symmetry Rune -> Spirit Rune migration
        // =========================================================

        Item spiritRune =
                BuiltInRegistries.ITEM.get(
                        SPIRIT_RUNE
                );

        if (spiritRune == null) {
                throw new IllegalStateException(
                        "[MagoCompat] Missing required item: "
                                + SPIRIT_RUNE
                );
        }

        for (ItemEntity drop : event.getDrops()) {
                ItemStack stack =
                        drop.getItem();

                ResourceLocation itemId =
                        BuiltInRegistries.ITEM.getKey(
                                stack.getItem()
                        );

                if (!SYMMETRY_RUNE.equals(itemId)) {
                continue;
                }

                int count =
                        stack.getCount();

                drop.setItem(
                        new ItemStack(
                                spiritRune,
                                count
                        )
                );

                if (!loggedSymmetryDropMigration) {
                loggedSymmetryDropMigration = true;

                MagoCompat.LOGGER.info(
                        "[MagoCompat] Retired Symmetry Rune drop migrated. "
                                + "{} -> {}",
                        SYMMETRY_RUNE,
                        SPIRIT_RUNE
                );
                }
        }


        // =========================================================
        // 2. Remove retired item drops
        // =========================================================

        Iterator<ItemEntity> iterator =
                event.getDrops().iterator();

        while (iterator.hasNext()) {
                ItemEntity drop =
                        iterator.next();

                ItemStack stack =
                        drop.getItem();

                ResourceLocation itemId =
                        BuiltInRegistries.ITEM.getKey(
                                stack.getItem()
                        );


                /*
                * Retired physical item:
                *
                * - Annihilator's Protocol
                * - Rod of Discord
                * - retired runes/orbs/etc.
                */
                if (RETIRED_ITEMS.contains(itemId)) {
                iterator.remove();

                if (!loggedRetiredDropRemoval) {
                        loggedRetiredDropRemoval = true;

                        MagoCompat.LOGGER.info(
                                "[MagoCompat] Retired mob drop removed. "
                                        + "First detected item: {}",
                                itemId
                        );
                }

                continue;
                }


                /*
                * Deleted spell scroll.
                *
                * Some addon Global Loot Modifiers create Iron's scrolls
                * directly and do not respect ENABLED=false.
                */
                if (isDisabledSpellScroll(stack)) {
                iterator.remove();

                if (!loggedDisabledScrollRemoval) {
                        loggedDisabledScrollRemoval = true;

                        MagoCompat.LOGGER.info(
                                "[MagoCompat] Disabled spell scroll mob drop removed."
                        );
                }
                }
        }
        }

        private static boolean isDisabledSpellScroll(
                ItemStack stack
        ) {
        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(
                        stack.getItem()
                );

        if (!id(
                "irons_spellbooks",
                "scroll"
        ).equals(itemId)) {
                return false;
        }

        if (!ISpellContainer.isSpellContainer(stack)) {
                return false;
        }

        ISpellContainer container =
                ISpellContainer.get(
                        stack
                );

        if (container == null) {
                return false;
        }

        for (var slot : container.getAllSpells()) {
                String spellId =
                        slot.getSpell()
                                .getSpellId();

                if (MagoSpellPolicy.isDisabled(spellId)) {
                return true;
                }
        }

        return false;
        }


    public static boolean isRetired(
            ItemStack stack
    ) {
        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(
                        stack.getItem()
                );

        return RETIRED_ITEMS.contains(
                itemId
        );
    }


    public static Set<ResourceLocation> retiredItemIds() {
        return RETIRED_ITEMS;
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